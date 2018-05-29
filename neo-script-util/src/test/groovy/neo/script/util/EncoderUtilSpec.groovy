package neo.script.util

import spock.lang.Specification

class EncoderUtilSpec extends Specification {
    def 'md5 guava'() {
        given:
        def plainText = '123456'
        when:
        String md5_1 = EncoderUtil.md5(plainText)
        println(md5_1)
        then:
        md5_1.length() > 30
    }
}
