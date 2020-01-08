package com.mairuis.excel.work.row;

import ch.qos.logback.core.net.server.Client;
import com.mairuis.excel.entity.Location;
import com.mairuis.excel.tools.utils.Cells;
import com.mairuis.excel.tools.utils.ClientMatcher;
import com.mairuis.excel.work.Worker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2020/1/6
 */
@Worker
public class AccountMatch extends AbstractRowWork {

    public AccountMatch() {
        super(new RowVisitor() {
            @Override
            public boolean ignore(Map<String, String> config, Map<String, Integer> headerIndex, Row row) {
                return Cells.isEmpty(row.getCell(headerIndex.get("用友客户")));
            }

            @Override
            public String visit(Map<String, String> config, Map<String, Integer> headerIndex, Workbook workbook, Sheet sheet, Row row) throws Throwable {
                List<String> clientList = Files.readAllLines(Paths.get("C:\\Users\\Mairuis\\Desktop\\client.txt"));
                List<String> clientDataList = Files.readAllLines(Paths.get("C:\\Users\\Mairuis\\Desktop\\data.txt"));
                List<String> filterWord = Stream.concat(
                        Stream.of("(", ")", "（", "）", "-", " ", "午餐", "晚餐", "早餐", "中餐", "食堂", "到点晚餐"),
                        Stream.of(Location.values()).map(Location::getValue)
                ).collect(toList());
                List<String> ignoreWord = Stream.of(
                        "停用", "弃用", "错误", "作废"
                ).collect(toList());
                ClientMatcher matcher = new ClientMatcher(clientList, filterWord, ignoreWord);
                Cell cell = Cells.getOrCreate(row, headerIndex.get("用友客户"));

                return "成功";
            }
        });
    }


    @Override
    public void initialize(Map<String, String> config, Workbook workbook, Sheet src) {

    }
}
