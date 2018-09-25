package ns.gflex.util

import org.apache.commons.cli.Option
import org.apache.commons.cli.OptionBuilder
import spock.lang.Specification

/**
 * Created by Neo on 2017-09-26.
 */
class CliBuilderSpec extends Specification {
    def 'simple args'() {
        given:
        def cli = new CliBuilder(usage: 'ls')
        cli.a('display all files')
        cli.l('use a long listing format')
        cli.t('sort by modification time')
        cli.usage()

        when:
        def options = cli.parse(['-alt', '*.groovy'])


        then:
        options // would be null (false) on failure
        options.arguments() == ['*.groovy']
        options.a && options.l && options.t
    }

    def 'long option'() {
        given:
        def cli = new CliBuilder(usage: 'curl [options] <url>').with {
            b(longOpt: 'basic', 'Use HTTP Basic Authentication')
            d(longOpt: 'data', args: 1, argName: 'data', 'HTTP POST data')
            G(longOpt: 'get', 'Send the -d data with a HTTP GET')
            q('If used as the first parameter disables .curlrc')
            D(longOpt: 'sep', args: 2, valueSeparator: '=', argName: 'property=value', 'use value for given property')
            delegate
        }
        cli << Option.builder('u').longOpt('url').hasArg().argName('URL').desc('Set URL to work with').build()

        cli.usage()

        when:
        def options = cli.parse(['--basic', '-u', 'url.is.groovy', '-d"d is ABS"', '-Da=1', '-Db=2', '--sep', 'c=3', 'gogogo'])
        println(options.arguments())
        println "$options.Ds | $options.d | $options.url | $options.URL"

        then:
        options // would be null (false) on failure

    }

    def 'optionalArg'() {
        given:
        def cli = new CliBuilder(usage: 'curl [options] <url>').with {
            _(longOpt: 'data', args: Option.UNLIMITED_VALUES, optionalArg: true, argName: 'data', 'HTTP POST data')
            delegate
        }
        cli << Option.builder('u').longOpt('url').optionalArg(true).hasArg()
                .argName('URL').desc('Set URL to work with')
                .build()
        cli.usage()

        when:
        def options = cli.parse(['-u', 'argu', '--data', 'argd1', 'argd2', '--url', 'arg1'])
        println(options.arguments())
        println options.us
        println options.datas
        println(options.datas.class)

        then:
        options // would be null (false) on failure

    }
}
