package com.mairuis.excel.work.row;

import com.mairuis.excel.entity.Location;
import com.mairuis.excel.tools.client.ClientDatabase;
import com.mairuis.excel.tools.client.ClientMatcher;
import com.mairuis.excel.tools.client.MatchInfo;
import com.mairuis.excel.tools.utils.Cells;
import com.mairuis.excel.work.Worker;
import com.mairuis.utils.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
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
            private ClientMatcher matcher;


            @Override
            public void init(Map<String, String> config, Workbook workbook, Sheet sheet) {
                List<String> filterWord = Stream.concat(
                        Stream.of("(", ")", "（", "）", "-", " ", "午餐", "晚餐", "早餐", "中餐", "食堂", "到点晚餐"),
                        Stream.of(Location.values()).map(Location::getValue)
                ).collect(toList());
                try {
                    this.matcher = new ClientMatcher(filterWord
                            , ClientDatabase.createByWorkbook(
                            WorkbookFactory.create(new File(config.get("clientData")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean ignore(Map<String, String> config, Map<String, Integer> headerIndex, Row row) {
                return Cells.isEmpty(row.getCell(headerIndex.get("客户")));
            }

            @Override
            public String visit(Map<String, String> config, Map<String, Integer> headerIndex, Workbook workbook, Sheet sheet, Row row) throws Throwable {
                String clientData = Cells.toString(row.getCell(headerIndex.get("客户")));
                Location location = Cells.isEmpty(row.getCell(headerIndex.get("城市"))) ? null : Location.find(Cells.toString(row.getCell(headerIndex.get("城市"))));
                List<MatchInfo> matchInfo = matcher.match(location, clientData);
                if (!matchInfo.isEmpty()) {
                    MatchInfo similar = matchInfo.get(0);
                    Cells.writeCell(row, headerIndex.get("用友客户"), similar.getClient().getName());
                    Cells.writeCell(row, headerIndex.get("匹配依据"), similar.getType());
                    Cells.writeCell(row, headerIndex.get("客户编码"), similar.getClient().getCode());
                    Cells.writeCell(row, headerIndex.get("客户名距离"), StringUtils.editDistance(similar.getRawClient(), similar.getRawData()));
                    Cells.writeCell(row, headerIndex.get("候选用友客户名"),
                            matchInfo.stream()
                                    .filter(x -> x != similar)
                                    .map(x -> x.getClient().getName())
                                    .reduce("", (a, b) -> a + "||" + b));
                } else {
                    return "失败";
                }
                return "成功";
            }
        });
        this.addHeader("用友客户");
        this.addHeader("客户名距离");
        this.addHeader("匹配依据");
        this.addHeader("客户编码");
        this.addHeader("候选用友客户名");
    }
}
