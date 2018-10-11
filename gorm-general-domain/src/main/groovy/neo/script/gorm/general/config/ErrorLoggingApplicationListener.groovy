package neo.script.gorm.general.config

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.context.event.ApplicationPreparedEvent
import org.springframework.boot.logging.LogFile
import org.springframework.boot.logging.LoggingApplicationListener
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationEvent
import org.springframework.context.event.GenericApplicationListener
import org.springframework.core.ResolvableType

/**
 * 注册spring boot监听程序，增加错误日志单独文件输出功能
 * @see org.springframework.boot.logging.LoggingApplicationListener
 */
class ErrorLoggingApplicationListener implements GenericApplicationListener {

    public
    final String ERROR_LOG_PATTERN = '%d{yyyy-MM-dd HH:mm:ss.SSS} ${LOG_LEVEL_PATTERN:%5p} ${PID} --- [%t] %-40.40logger{39} : %m%n    %caller{1}${LOG_EXCEPTION_CONVERSION_WORD:%wEx}'

    private static Class<?>[] SOURCE_TYPES = [SpringApplication.class, ApplicationContext.class];

    @Override
    boolean supportsEventType(ResolvableType eventType) {
        ApplicationPreparedEvent.isAssignableFrom(eventType.rawClass)
    }

    @Override
    boolean supportsSourceType(Class<?> sourceType) {
        SOURCE_TYPES.any {
            it.isAssignableFrom(sourceType)
        }
    }

    @Override
    void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationPreparedEvent) {
            def env = event.applicationContext.environment
            LogFile logFile = LogFile.get(env);

            LoggerContext logCtx = LoggerFactory.getILoggerFactory();

            PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
            logEncoder.setContext(logCtx);
            logEncoder.setPattern(env.getProperty('logging.pattern.file') ?: ERROR_LOG_PATTERN);
            logEncoder.start();

            ThresholdFilter thresholdFilter = new ThresholdFilter()
            thresholdFilter.level = 'ERROR'
            thresholdFilter.start()

            RollingFileAppender logFileAppender = new RollingFileAppender();
            logFileAppender.setContext(logCtx);
            logFileAppender.setName("errorOnlyLog");
            logFileAppender.addFilter(thresholdFilter)
            logFileAppender.setEncoder(logEncoder);
            logFileAppender.setAppend(true);
            logFileAppender.setFile(logFile.toString() + '.error');

            TimeBasedRollingPolicy logFilePolicy = new TimeBasedRollingPolicy();
            logFilePolicy.setContext(logCtx);
            logFilePolicy.setParent(logFileAppender);
            logFilePolicy.setFileNamePattern(logFile.toString() + '.error-%d{yyyy-MM-dd}');
            logFilePolicy.setMaxHistory(70);
            logFilePolicy.start();

            logFileAppender.setRollingPolicy(logFilePolicy);
            logFileAppender.start();

            logCtx.getLogger("root").addAppender(logFileAppender);
        }
    }

    /**
     * after LoggingApplicationListener
     * @see org.springframework.boot.logging.LoggingApplicationListener#DEFAULT_ORDER
     */
    @Override
    int getOrder() {
        return LoggingApplicationListener.DEFAULT_ORDER + 1;
    }
}
