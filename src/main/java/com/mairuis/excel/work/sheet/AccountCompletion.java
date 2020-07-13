package com.mairuis.excel.work.sheet;

import com.mairuis.excel.tools.utils.Cells;
import com.mairuis.excel.tools.utils.Rows;
import com.mairuis.excel.work.Worker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 账目补全
 *
 * @author Mairuis
 * @date 2020/1/12
 */
@Worker
public class AccountCompletion extends SheetWork {
    @Override
    public Workbook work(Map<String, String> config, Workbook workbook, Sheet sheet) {
        String currentMonth = config.get("currentMonth");

        Rows.ensureColumn(HEADER_NUMBER, sheet, Arrays.asList(currentMonth + "冲账", "处理结果"));
        Stream.of("好客", "巧达", "造物")
                .forEach(currentType -> {
                    Map<String, BigDecimal> balanceMap = new HashMap<>();
                    Map<String, Integer> indexMap = Rows.getIndexMap(sheet.getRow(HEADER_NUMBER));
                    Supplier<Stream<Row>> data = () ->
                            Rows.toList(sheet, CONTENT_START_NUMBER, sheet.getLastRowNum())
                                    .stream()
                                    .filter(Objects::nonNull)
                                    .filter(x -> !Rows.isEmptyRow(x))
                                    .filter(x -> currentType.equals(Cells.toString(x.getCell(indexMap.get("用友账套")))));

                    DecimalFormat decimalFormat = new DecimalFormat("#.00");
                    data.get()
                            .filter(x -> Cells.toString(x.getCell(indexMap.get("所属月份"))) != null)
                            .filter(x -> Cells.toString(x.getCell(indexMap.get("所属月份"))).contains(currentMonth))
                            .forEach(x -> {
                                String supplierName = Cells.toString(x.getCell(indexMap.get("供应商名称")));
                                BigDecimal balance = new BigDecimal(decimalFormat.format(Cells.getDoubleValue(x.getCell(indexMap.get("预付款余额")))));
                                if (balanceMap.containsKey(supplierName) && balanceMap.get(supplierName).compareTo(balance) != 0) {
                                    LOGGER.error("行 {} 供应商 {} 余额错误，余额不是同一值", x.getRowNum(), supplierName);
                                    System.exit(0);
                                }
                                balanceMap.put(supplierName, balance);
                            });
                    data.get()
                            .filter(x -> balanceMap.containsKey(Cells.toString(x.getCell(indexMap.get("供应商名称")))))
                            .filter(x -> new BigDecimal(decimalFormat.format(Cells.getDoubleValue(x.getCell(indexMap.get("余额"))))).intValue() > 0)
                            .collect(Collectors.groupingBy(x -> Cells.toString(x.getCell(indexMap.get("供应商名称")))))
                            .forEach((supplier, b) -> {
                                BigDecimal balance = balanceMap.get(supplier);
                                for (Row x : b) {
                                    BigDecimal price = new BigDecimal(decimalFormat.format(Cells.getDoubleValue(x.getCell(indexMap.get("餐厅结算款")))));
                                    if (Cells.isEmpty(x.getCell(indexMap.get("已冲账金额")))) {
                                        if (balance.compareTo(price) >= 0) {
                                            Cells.writeCell(x, indexMap.get("已冲账金额"), price.floatValue());
                                            Cells.writeCell(x, indexMap.get("冲账日期"), currentMonth);
                                            balance = balance.subtract(price);
                                        } else {
                                            Cells.writeCell(x, indexMap.get("已冲账金额"), balance.floatValue());
                                            Cells.writeCell(x, indexMap.get("冲账日期"), currentMonth);
                                            break;
                                        }
                                    } else {
                                        //XXXX年X月
                                        String rechargeDate = Cells.toString(x.getCell(indexMap.get("冲账日期")));
                                        if (rechargeDate.length() != 7) {
                                            LOGGER.warn("放弃处理 {} 行", x.getRowNum());
                                            break;
                                        } else {
                                            Cell prevRechargeCell = x.getCell(indexMap.get("已冲账金额"));
                                            BigDecimal prevRecharge = new BigDecimal(decimalFormat.format(Cells.getDoubleValue(prevRechargeCell)));
                                            BigDecimal addRecharge = price.subtract(prevRecharge);
                                            if (balance.compareTo(addRecharge) >= 0) {
                                                Cells.writeFormula(prevRechargeCell, decimalFormat.format(prevRecharge) + "+" + decimalFormat.format(addRecharge));
                                                Cells.writeCell(prevRechargeCell, rechargeDate + "-" + currentMonth);
                                                balance = balance.subtract(addRecharge);
                                            } else {
                                                Cells.writeFormula(prevRechargeCell, decimalFormat.format(prevRecharge) + "+" + decimalFormat.format(balance));
                                                Cells.writeCell(prevRechargeCell, rechargeDate + "-" + currentMonth);
                                                break;
                                            }
                                        }
                                    }
                                }
                            });
                });
        return workbook;
    }
}
