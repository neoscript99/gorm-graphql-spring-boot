package neo.script.util

import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.WorkbookUtil
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class ExcelUtil {

    static List<Map<String, Object>> excelToMapList(byte[] excelFile) {
        List<Map<String, Object>> mapList = []
        Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(excelFile))
        Sheet sheet = wb.getSheetAt(0);
        Row firstRow = sheet.getRow(0);
        if (!firstRow)
            return mapList;

        1.upto(sheet.physicalNumberOfRows - 1) {
            Row row = sheet.getRow(it);
            if (!row)
                return;

            Map map = [:]

            0.upto(firstRow.physicalNumberOfCells - 1) {
                //表头空值忽略整列，内容空值忽略一格
                if (firstRow.getCell(it) && row.getCell(it))
                    map[getCellValue(firstRow.getCell(it)).toString()] = getCellValue(row.getCell(it))
            }

            mapList << map
        }
        return mapList
    }

    /**
     * 将一组map对象转化为excel文件
     * <pre>
     * mapListToExcel([name:'名字',age:'年龄','dept.name':'部门'],
     *                [[name:'a',age:10,dept:[name:'总部1']],[name:'b',age:11,dept:[name:'总部2']]])
     * </pre>
     * @param titleMap excel标题行Map，key对应mapList的key值，支持嵌套如dept.name，value为标题文字, 如需排序因使用LinkedHashMap
     * @param mapList map的key和titleList需对应
     * @param sheetName
     * @return
     */
    static byte[] mapListToExcel(
            Map<String, String> titleMap,
            List<Map<String, Object>> mapList,
            String sheetName = 'sheet1') {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(WorkbookUtil.createSafeSheetName(sheetName));
        //标题行
        Row rowTitle = sheet.createRow(0);
        CellStyle titleStyle = createTitleCellStyle(wb)
        titleMap.eachWithIndex { key, value, int col ->
            rowTitle.createCell(col).with {
                setCellStyle(titleStyle)
                setCellValue(value)
            }
        }
        //内容行
        if (mapList) {
            CellStyle contentStyle = createContentCellStyle(wb)
            mapList.eachWithIndex { Map<String, Object> map, int index ->
                Row row = sheet.createRow(index + 1);
                titleMap.eachWithIndex { key, value, int col ->
                    row.createCell(col).with {
                        setCellStyle(contentStyle)
                        setCellValue(MapUtil.getByNestKey(map, key))
                    }
                }
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        wb.write(baos);
        return baos.toByteArray()
    }

    static CellStyle createTitleCellStyle(Workbook wb) {
        CellStyle style = createContentCellStyle(wb)
        style.setFont(createYaHeiFont(wb, true))
        style.setAlignment(HorizontalAlignment.CENTER)
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style
    }

    static CellStyle createContentCellStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle()
        style.setBorderBottom(BorderStyle.THIN)
        style.setBorderTop(BorderStyle.THIN)
        style.setBorderLeft(BorderStyle.THIN)
        style.setBorderRight(BorderStyle.THIN)
        style.setFont(createYaHeiFont(wb, false))
        return style
    }

    static Font createYaHeiFont(Workbook wb, boolean isBold) {
        Font font = wb.createFont()
        font.setFontName("微软雅黑");
        font.setBold(isBold)
        return font
    }

    private static def getCellValue(Cell cell) {
        switch (cell.cellType) {
            case Cell.CELL_TYPE_NUMERIC:
            case Cell.CELL_TYPE_FORMULA:
                if (HSSFDateUtil.isCellDateFormatted(cell))
                    return cell.dateCellValue
                else
                    return cell.numericCellValue
            case Cell.CELL_TYPE_STRING:
                return cell.stringCellValue.trim()
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.booleanCellValue
            default:
                return null
        }
    }
}
