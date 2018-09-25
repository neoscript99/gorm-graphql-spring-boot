package ns.gflex.util

import spock.lang.Specification

/**
 * Created by Neo on 2017-09-19.
 */
class GroovyRegularExpressionsSpec extends Specification {
    def 'test pattern'() {
        given:
        String twister = 'she sells sea shells at the sea shore of seychelles'
        // twister must contain a substring of size 3
        // that starts with s and ends with a
        def finder = (twister =~ /s.a/)
        // twister must contain only words delimited by single spaces
        def WORD = /\w+/
        def matches = (twister ==~ /($WORD $WORD)*/)
        def wordsByX = twister.replaceAll(WORD, 'x')
        def words = twister.split(/ /)

        expect:'=~ 为包含，并返回查找结果 java.util.regex.Matcher ； ==~ 检查全字符串匹配'
        twister =~ /s.a/
        !twister.matches(/s.a/)
        !(twister ==~ /s.a/)

        and:
        finder instanceof java.util.regex.Matcher
        twister ==~ /(\w+ \w+)*/
        matches instanceof java.lang.Boolean
        !(twister ==~ /s.e/)
        wordsByX == 'x x x x x x x x x x'
        words.size() == 10
        words[0] == 'she'
    }
}
