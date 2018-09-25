package ns.gflex.util

import spock.lang.Specification


/**
 * Created by Neo on 2017-09-15.
 */
class ExcelUtilSpec extends Specification {
    String path

    def setup(){

         path = ExcelUtilSpec.getClassLoader().getResource('').getPath() + 'ExcelUtilExport.xlsx'
        println path
    }
    def 'export to excel'() {
        given:
        FileOutputStream fos = new FileOutputStream(path)

        when:
        byte[] xlsx = ExcelUtil.mapListToExcel([name: '名字', age: '年龄', 'dept.name': '部门'],
                [[name: 'a', age: 10, dept: [name: '总部1']], [name: 'b', age: 11, dept: [name: '总部2']]])
        fos.write(xlsx)
        fos.close()

        then:
        new File(path).size() > 0
    }

    def 'empty list'() {
        given:
        FileOutputStream fos = new FileOutputStream(path)

        when:
        byte[] xlsx = ExcelUtil.mapListToExcel([name: '名字', age: '年龄', 'dept.name': '部门'], [])
        fos.write(xlsx)
        fos.close()

        then:
        new File(path).size() > 0
    }
}
