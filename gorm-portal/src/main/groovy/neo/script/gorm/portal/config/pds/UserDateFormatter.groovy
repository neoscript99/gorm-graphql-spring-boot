package neo.script.gorm.portal.config.pds

import neo.script.gorm.general.util.TokenHolder
import org.apache.commons.text.StrSubstitutor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class UserDateFormatter extends QueryFormatter {
    static private Logger log = LoggerFactory.getLogger(UserDateFormatter.class)

    @Override
    String format(String query) {
        def valueMap = new HashMap()
        if (TokenHolder.token)
            valueMap.put('username', TokenHolder.token.username)

        def localDate = LocalDate.now()
        valueMap.put('today', localDate.toString())
        valueMap.put('yesterday', localDate.minusDays(1).format(dateFormat))
        valueMap.put('last_month', localDate.minusDays(localDate.dayOfMonth).format(dateFormat))
        valueMap.put('last_year', localDate.minusDays(localDate.dayOfYear).format(dateFormat))

        def res = StrSubstitutor.replace(query, valueMap);
        log.debug("before format: {}", query)
        log.debug("after format: {}", res)
        return res
    }

    @Override
    Integer order() {
        return 10
    }

    DateTimeFormatter getDateFormat() {
        DateTimeFormatter.ISO_DATE
    }
}
