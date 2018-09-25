package ns.gflex.util;


import flex.messaging.log.LineFormattedTarget;
import flex.messaging.log.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Flex日志记录
 * 仅包含blazeds调用相关日志，包含JavaAdapter错误信息
 *
 * @author wangchu
 * @since Jan 24, 2011
 */
public class FlexLog extends LineFormattedTarget {

    @Override
    public void logEvent(LogEvent event) {
        Logger log = LoggerFactory.getLogger("org.apache.flex.blazeds." + event.logger.getCategory());
        switch (event.level) {
            case LogEvent.INFO:
                log.info(event.message, event.throwable);
                break;
            case LogEvent.WARN:
                log.warn(event.message, event.throwable);
                break;
            case LogEvent.ERROR:
            case LogEvent.FATAL:
                log.error(event.message, event.throwable);
                break;
            default:
                log.debug(event.message, event.throwable);
                break;
        }
    }
}
