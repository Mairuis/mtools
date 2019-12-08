package com.mairuis.excel.component;

import com.mairuis.excel.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/8
 */

public class Spilt extends AbstractStrategy {
    private Row srcRow;
    private Row desRow;

    public Spilt(Row srcRow, Row desRow) {
        this.srcRow = srcRow;
        this.desRow = desRow;
    }

    @Override
    public boolean filter(Row src) {
        return src.getCell(2).getStringCellValue().contains("食堂");
    }

    @Override
    protected boolean doMake() throws Throwable {
        List<Cell> cellList = ExcelUtils.cellList(srcRow);
        String value = srcRow.getCell(2).getStringCellValue();
        String[] values = value.split("-");
        if (value.contains("结算款")) {
            if (values.length == 6) {
                //企业订餐-北京-贝壳到店-米feel餐厅-03月01日-03月31日结算款
                if (isDate(values[4]) && isDate(values[5])) {
                    desRow.shiftCellsLeft();
                    desRow.createCell(index += 1).setCellValue(values[1]);
                    desRow.createCell(index += 1).setCellValue(values[2]);
                    desRow.createCell(index += 1).setCellValue(values[4]);
                    desRow.createCell(index += 1).setCellValue(values[5]);
                }
            } else if (values.length == 5) {
                //企业订餐-北京-贝壳到店-03月01日-03月31日结算款
                if (isDate(values[3]) && isDate(values[4])) {
                    desRow.createCell(index += 1).setCellValue(values[1]);
                    desRow.createCell(index += 1).setCellValue(values[2]);
                    desRow.createCell(index += 1).setCellValue(values[4]);
                    desRow.createCell(index += 1).setCellValue(values[5]);
                }
            }

        } else if (value.contains("订餐款")) {

        } else if (value.contains("预付款")) {

        } else if (value.contains("押金")) {

        } else {

        }
        return true;
    }

    private boolean isDate(String value) {
        return value.contains("月") && value.contains("日");
    }
}
