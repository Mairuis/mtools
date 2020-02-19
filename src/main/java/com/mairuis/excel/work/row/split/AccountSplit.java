package com.mairuis.excel.work.row.split;

import com.mairuis.excel.entity.AccountType;
import com.mairuis.excel.work.Worker;
import com.mairuis.excel.work.row.AbstractRowWork;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mairuis.excel.entity.AccountType.*;

/**
 * @author Mairuis
 * @date 2019/12/8
 */
@Worker
public class AccountSplit extends AbstractRowWork {
    public AccountSplit() {
        super(addHandlers());

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

    public static String[] getDates(String content, Pattern pattern) {
        Matcher matcher = pattern.matcher(content);
        List<String> dates = new ArrayList<>();
        while (matcher.find()) {
            dates.add(matcher.group());
        }
        return dates.toArray(new String[0]);
    }

    private static AbstractAccountRowVisitor[] addHandlers() {
        return new AbstractAccountRowVisitor[]{
                new AbstractAccountRowVisitor(保洁费) {
                    @Override
                    public String visitAccount(Map<String, String> config, Map<String, Integer> headerIndex, Map<String, String> write, Workbook workbook, Sheet sheet, Row row, AccountType accountType, String content) {
                        String[] values = split(content);
                        Pattern pattern = Pattern.compile("[0-9]{1,2}月");
                        String[] dates = getDates(values[values.length - 1], pattern);
                        if (dates.length == 0) {
                            return "日期错误";
                        }
                        if (values[2].equals("顺风") && values.length >= 5 && values.length <= 6) {
                            //企业订餐-深圳-顺丰-顺丰南山（嘀卡4楼食堂午餐）-11月份保洁费
                            //企业订餐-深圳-顺丰-顺丰南山（嘀卡4楼食堂午餐）-永和豆浆（四楼）-11月份保洁费
                            write.put("城市", values[1]);
                            write.put("客户", values[3]);
                            write.put("事项", accountType.getName());
                        } else if (values.length >= 4 && values.length <= 5) {
                            //企业订餐-深圳-顺丰南山（嘀卡4楼食堂午餐）-顺丰南山（嘀卡4楼食堂午餐）-11月份保洁费
                            write.put("城市", values[1]);
                            write.put("客户", values[2]);
                            write.put("事项", accountType.getName());
                        }
                        return null;
                    }
                },
                new AbstractAccountRowVisitor(在线支付) {
                    @Override
                    public String visitAccount(Map<String, String> config, Map<String, Integer> headerIndex, Map<String, String> write, Workbook workbook, Sheet sheet, Row row, AccountType accountType, String content) {
                        String[] values = split(content);
                        if (!values[0].equals("在线支付") || values.length < 4) {
                            return "未知格式";
                        }
                        //在线支付-上海-微米网络-201911
                        write.put("城市", values[1]);
                        write.put("客户", values[2]);
                        write.put("事项", accountType.getName());
                        return null;
                    }
                },
                new AbstractAccountRowVisitor(结算款) {
                    @Override
                    public String visitAccount(Map<String, String> config, Map<String, Integer> headerIndex, Map<String, String> write, Workbook workbook, Sheet sheet, Row row, AccountType accountType, String content) {
                        Pattern pattern = Pattern.compile("[0-9]{1,2}月[0-9]{1,2}日");
                        String[] dates = getDates(content, pattern);
                        String[] values = split(content);
                        if (dates.length != 2) {
                            return "日期错误";
                        }
                        if (values[2].equals("顺丰") && values.length >= 6 && values.length <= 7) {
                            //企业订餐-深圳-顺丰-顺丰南山（嘀卡4楼食堂午餐）-星期五快时尚餐厅-11月01日-11月30日结算款
                            //企业订餐-深圳-顺丰-顺丰南山（嘀卡4楼食堂午餐）-11月01日-11月30日结算款
                            write.put("城市", values[1]);
                            write.put("周期一", dates[0]);
                            write.put("周期二", dates[1]);
                            write.put("客户", values[3]);
                            write.put("事项", accountType.getName());
                        } else if (values[2].equals("食堂") && values.length >= 6 && values.length <= 9) {
                            //企业订餐-地区-食堂-客户前半段-客户后半段-供应商-(未知)X月X日(未知)-(未知)X月X日(未知)
                            //企业订餐-地区-食堂-客户-供应商-(未知)X月X日(未知)-(未知)X月X日(未知)
                            //企业订餐-地区-食堂-客户-(未知)X月X日(未知)-(未知)X月X日(未知)
                            write.put("城市", values[1]);
                            write.put("周期一", dates[0]);
                            write.put("周期二", dates[1]);
                            write.put("客户", values[3]);
                            write.put("事项", accountType.getName());
                        } else if (values.length >= 5 && values.length <= 6) {
                            //企业订餐-地区-客户-供应商-(未知)X月X日(未知)-(未知)X月X日(未知)
                            //企业订餐-地区-客户-(未知)X月X日(未知)-(未知)X月X日(未知)
                            write.put("城市", values[1]);
                            write.put("周期一", dates[0]);
                            write.put("周期二", dates[1]);
                            write.put("客户", values[2]);
                            write.put("事项", accountType.getName());
                        } else {
                            return "未知格式";
                        }
                        return null;
                    }
                },
                new AbstractAccountRowVisitor(订餐款) {
                    @Override
                    public String visitAccount(Map<String, String> config, Map<String, Integer> headerIndex, Map<String, String> write, Workbook workbook, Sheet sheet, Row row, AccountType accountType, String content) {
                        Pattern pattern = Pattern.compile("[0-9]{1,2}月[0-9]{1,2}日");
                        String[] dates = getDates(content, pattern);
                        String[] values = split(content);
                        if (dates.length != 2) {
                            return "失败";
                        }
                        if (values.length == 5) {
                            //企业订餐-上海-客户-08月01日-10月31日订餐款
                            write.put("城市", values[1]);
                            write.put("周期一", dates[0]);
                            write.put("周期二", dates[1]);
                            write.put("事项", accountType.getName());
                            write.put("客户", values[2]);
                        } else if (values.length == 6) {
                            //企业订餐-上海-客户-供应商-08月01日-10月31日订餐款
                            write.put("城市", values[1]);
                            write.put("周期一", dates[0]);
                            write.put("周期二", dates[1]);
                            write.put("事项", accountType.getName());
                            write.put("客户", values[2]);
                        } else {
                            return "失败";
                        }
                        return null;
                    }
                },
                new AbstractAccountRowVisitor(预付款) {
                    @Override
                    public String visitAccount(Map<String, String> config, Map<String, Integer> headerIndex, Map<String, String> write, Workbook workbook, Sheet sheet, Row row, AccountType accountType, String content) {
                        String[] values = split(content);
                        if (values.length == 5) {
                            //企业订餐-地区-客户-供应商-(未知...)
                            write.put("城市", values[1]);
                            write.put("事项", accountType.getName());
                            write.put("客户", values[2]);
                            write.put("供应商", values[3]);
                        } else {
                            return "失败";
                        }
                        return null;
                    }
                },
                new AbstractAccountRowVisitor(押金) {
                    @Override
                    public String visitAccount(Map<String, String> config, Map<String, Integer> headerIndex, Map<String, String> write, Workbook workbook, Sheet sheet, Row row, AccountType accountType, String content) {
                        String[] values = split(content);
                        //企业订餐-地区-客户-供应商-(未知)
                        //企业订餐-地区-供应商-(未知)
                        if (values.length == 4) {
                            write.put("城市", values[1]);
                            write.put("事项", accountType.getName());
                            write.put("供应商", values[2]);
                        } else if (values.length == 5) {
                            write.put("城市", values[1]);
                            write.put("事项", accountType.getName());
                            write.put("供应商", values[3]);
                        } else {
                            return "失败";
                        }
                        return null;
                    }
                }
        };
    }

    public static String[] split(String content) {
        if ((content.contains("（") || content.contains("(")) && (content.contains("）") || content.contains(")"))) {
            boolean start = false;
            for (int i = 0; i < content.length(); i += 1) {
                if (start && content.charAt(i) == '-') {
                    char[] chars = content.toCharArray();
                    chars[i] = '§';
                    content = new String(chars);
                }
                if (content.charAt(i) == '（' || content.charAt(i) == '(') {
                    start = true;
                }
                if (content.charAt(i) == '）' || content.charAt(i) == ')') {
                    start = false;
                }
            }
        }
        String[] split = content.split("-");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].replace("§", "-");
        }
        return split;
    }
}
