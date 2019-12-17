package com.mairuis.excel;import com.mairuis.excel.tools.utils.Workbooks;import com.mairuis.excel.work.WorkbookTask;import com.mairuis.utils.StringUtils;import org.apache.poi.ss.usermodel.Workbook;import org.apache.poi.ss.usermodel.WorkbookFactory;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.boot.CommandLineRunner;import org.springframework.context.ApplicationContext;import org.springframework.context.annotation.ComponentScan;import org.springframework.stereotype.Component;import javax.annotation.Resource;import java.io.File;import java.util.Collections;import java.util.Map;/** * Excel工具集 * * @author Mairuis * @date 2019/12/8 */@Component@ComponentScan(value = "com.mairuis.excel")public class ConsoleExcelWorker implements CommandLineRunner {    private static Logger LOGGER = LoggerFactory.getLogger(ConsoleExcelWorker.class);    @Resource    ApplicationContext applicationContext;    public WorkbookTask findWorker(String name) {        int min = Integer.MAX_VALUE;        String likeName = null;        for (WorkbookTask worker : applicationContext.getBeansOfType(WorkbookTask.class).values()) {            String workerName = worker.getClass().getSimpleName();            if (workerName.equals(name)) {                return worker;            } else {                int editDistance = StringUtils.editDistance(name, workerName);                if (editDistance < min) {                    min = editDistance;                    likeName = workerName;                }            }        }        LOGGER.error("未找到名为 " + name + " 工作器，你是不是在找他们 {}", likeName);        return null;    }    @Override    public void run(String... args) throws Exception {        Map<String, String> config = Collections.unmodifiableMap(StringUtils.argsToMap(args));        WorkbookTask workbookTask = findWorker(config.getOrDefault("task", "PrintSheet"));        Workbook srcWorkbook = WorkbookFactory.create(new File(config.get("filePath")), null, false);        Workbook result = workbookTask.work(config, srcWorkbook);        Workbooks.writeToFile(result, config.get("outFilePath"));    }}