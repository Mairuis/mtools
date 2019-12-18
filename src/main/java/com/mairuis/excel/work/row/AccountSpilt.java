package com.mairuis.excel.work.row;

import com.mairuis.excel.tools.utils.Rows;
import com.mairuis.excel.work.AbstractRowWork;
import com.mairuis.excel.work.Worker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * @author Mairuis
 * @date 2019/12/8
 */
@Worker
public class AccountSpilt extends AbstractRowWork {

    private boolean isDate(String value) {
        return value.contains("月") && value.contains("日");
    }

    @Override
    public boolean filter(Map<String, String> config, Row row) {
        String value = row.getCell(2).getStringCellValue();
        return value.contains("食堂") ||
                (!value.contains("结算款")
                        && !value.contains("订餐款")
                        && !value.contains("预付款")
                        && !value.contains("押金")
                        && !value.contains("回款"));
    }

    @Override
    public boolean work(Map<String, String> config, Workbook workbook, Sheet sheet, Row row) {
        String value = row.getCell(2).getStringCellValue().replace(" ", "");
        String[] values = value.split("-");
        Cell[] leftCells = new Cell[]{row.getCell(0), row.getCell(1), row.getCell(2)};
        String[] rightCells = Rows.selectValue(row, 3, Integer.MAX_VALUE);
        if (value.contains("结算款")) {
            if (values.length >= 6) {
                //企业订餐-北京-贝壳到店-米feel餐厅-03月01日-03月31日结算款
                values[5] = values[5].replace("结算款", "");
                if (isDate(values[4]) && isDate(values[5])) {
                    Rows.writeRow(row, leftCells, values[1], values[2], values[4], values[5], rightCells);
                    return true;
                }
            } else if (values.length == 5) {
                //企业订餐-北京-贝壳到店-03月01日-03月31日结算款
                values[4] = values[4].replace("结算款", "");
                if (isDate(values[3]) && isDate(values[4])) {
                    Rows.writeRow(row, leftCells, values[1], values[2], values[3], values[4], rightCells);
                    return true;
                }
            }
        } else if (value.contains("订餐款")) {

        } else if (value.contains("预付款")) {

        } else if (value.contains("押金")) {

        } else if (value.contains("回款")) {

        }
        return false;
    }

    @Override
    public void initialize(Map<String, String> config, Workbook workbook, Sheet src) {

    }
}
