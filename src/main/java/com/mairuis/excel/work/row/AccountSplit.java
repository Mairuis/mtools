package com.mairuis.excel.work.row;

import com.mairuis.excel.entity.AccountType;
import com.mairuis.excel.tools.utils.Cells;
import com.mairuis.excel.tools.utils.Rows;
import com.mairuis.excel.work.AbstractRowWork;
import com.mairuis.excel.work.Worker;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import static com.mairuis.excel.entity.AccountType.结算款;

/**
 * @author Mairuis
 * @date 2019/12/8
 */
@Worker
public class AccountSplit extends AbstractRowWork {
    public AccountSplit() {
        this.addHeader("事项");
        this.addHeader("会计科目");
        this.addHeader("现金流");
        this.addHeader("城市");
        this.addHeader("客户");
        this.addHeader("客户编码");
        this.addHeader("供应商");
        this.addHeader("供应商编码");
        this.addHeader("周期一");
        this.addHeader("周期二");
    }

    private boolean isDate(String value) {
        return datePattern_1.matcher(value).matches() ||
                datePattern_2.matcher(value).matches() ||
                datePattern_3.matcher(value).matches() ||
                datePattern_4.matcher(value).matches();
    }

    @Override
    public boolean filter(Map<String, String> config, Row row) {
        if (row.getCell(2) == null) {
            return true;
        }
        String value = row.getCell(2).getStringCellValue();
        return value.contains("企业订餐-食堂") ||
                (!value.contains("结算款")
                        && !value.contains("订餐款")
                        && !value.contains("预付款")
                        && !value.contains("押金")
                        && !value.contains("回款"));
    }

    @Override
    public boolean work(Map<String, String> config, Workbook workbook, Sheet sheet, Row row) {
        Row headerRow = sheet.getRow(HEADER_NUMBER);
        Map<String, Integer> header = Rows.getIndexMap(headerRow);
        String value = row.getCell(header.get("摘要")).getStringCellValue().replace(" ", "");
        String[] values = value.split("-");
        //"事项","会计科目","现金流","城市","客户","客户编码","供应商","供应商编码","周期一","周期二
        AccountType accountType = AccountType.getType(value);
        switch (accountType) {
            case 结算款: {
                if (values.length >= 7) {
                    //企业订餐-广州-食堂-河池职教中心（午餐）-南宁市民中心-11月27日-11月29日结算款
                    values[6] = values[6].replace(结算款.getName(), "");
                    if (isDate(values[6]) && isDate(values[5])) {
                        Cells.writeCell(row, header.get("城市"), values[1]);
                        Cells.writeCell(row, header.get("周期一"), values[4]);
                        Cells.writeCell(row, header.get("周期二"), values[5]);
                        Cells.writeCell(row, header.get("供应商"), values[3]);
                        Cells.writeCell(row, header.get("事项"), accountType.getName());
                        Cells.writeCell(row, header.get("客户"), values[2]);
                        return true;
                    }
                } else if (values.length >= 6) {
                    //企业订餐-北京-贝壳到店-米feel餐厅-03月01日-03月31日结算款
                    values[5] = values[5].replace(结算款.getName(), "");
                    if (isDate(values[4]) && isDate(values[5])) {
                        Cells.writeCell(row, header.get("城市"), values[1]);
                        Cells.writeCell(row, header.get("周期一"), values[4]);
                        Cells.writeCell(row, header.get("周期二"), values[5]);
                        Cells.writeCell(row, header.get("供应商"), values[3]);
                        Cells.writeCell(row, header.get("客户"), values[2]);
                        Cells.writeCell(row, header.get("事项"), accountType.getName());
                        return true;
                    }
                } else if (values.length == 5) {
                    //企业订餐-北京-贝壳到店-03月01日-03月31日结算款
                    values[4] = values[4].replace("结算款", "");
                    if (isDate(values[3]) && isDate(values[4])) {
                        Cells.writeCell(row, header.get("城市"), values[1]);
                        Cells.writeCell(row, header.get("周期一"), values[3]);
                        Cells.writeCell(row, header.get("周期二"), values[4]);
                        Cells.writeCell(row, header.get("客户"), values[2]);
                        Cells.writeCell(row, header.get("事项"), accountType.getName());
                        return true;
                    }
                }
                break;
            }
            default:
                LOGGER.warn("未处理 {}", accountType);
                break;
        }
        return false;
    }

    @Override
    public void initialize(Map<String, String> config, Workbook workbook, Sheet src) {

    }

    private Pattern datePattern_1 = Pattern.compile("[0-9][0-9]月[0-9][0-9]日");
    private Pattern datePattern_2 = Pattern.compile("[0-9][0-9]月[0-9]日");
    private Pattern datePattern_3 = Pattern.compile("[0-9]月[0-9][0-9]日");
    private Pattern datePattern_4 = Pattern.compile("[0-9]月[0-9]日");
}
