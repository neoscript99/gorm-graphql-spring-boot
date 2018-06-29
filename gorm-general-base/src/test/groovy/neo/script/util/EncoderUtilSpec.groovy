package neo.script.util

import spock.lang.Specification

class EncoderUtilSpec extends Specification {

    def 'md5 guava'() {
        given:
        String code1 = EncoderUtil.md5('123456')
        String code2 = EncoderUtil.md5('xxdd点对点')

        System.out.printf("%s ,length: %d %n", code1, code1.length())
        System.out.printf("%s ,length: %d %n", code2, code2.length())
        expect:
        code1.length() == 32
        code2.length() == 32
    }

    def 'sha256 guava'() {
        given:
        String code1 = EncoderUtil.sha256('123456')
        String code2 = EncoderUtil.sha256('xxdd点对点')

        System.out.printf("%s ,length: %d %n", code1, code1.length())
        System.out.printf("%s ,length: %d %n", code2, code2.length())
        expect:
        code1.length() == 64
        code2.length() == 64
    }
}
