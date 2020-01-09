package com.mairuis.excel.tools.client;

import com.alibaba.fastjson.JSON;
import com.mairuis.excel.entity.Location;
import com.mairuis.excel.tools.utils.MatchUtils;
import com.mairuis.utils.StringUtils;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author Mairuis
 * @since 2019/12/27
 */
public class ClientMatcher {

    private List<String> filterWord;
    private ClientDatabase database;

    public ClientMatcher(List<String> filterWord, ClientDatabase database) {
        this.filterWord = filterWord;
        this.database = database;
    }

    private String filter(String word) {
        for (String filter : filterWord) {
            word = word.replace(filter, "");
        }
        return word;
    }

    private String clear(String word) {
        return StringUtils.clearByToken(word,
                (x) -> x.equals('(') || x.equals('（'),
                (x) -> x.equals(')') || x.equals('）'));
    }

    private PriorityQueue<MatchInfo> scan(String clientNameData, int capacity, Collection<Client> clients, String type) {
        PriorityQueue<MatchInfo> queue = new PriorityQueue<>(
                Comparator.comparingInt((MatchInfo x) -> x.similarity)
                        .thenComparingInt(x -> -(Math.abs(clientNameData.length() - x.rawClient.length())))
        );
        String handledClientNameData = filter(clear(clientNameData));
        for (Client client : clients) {
            String clientName = client.getName();
            String handledClientName = filter(clear(clientName));
            MatchInfo matchInfo = new MatchInfo()
                    .setRawClient(clientName)
                    .setRawData(clientNameData)
                    .setFilteredClient(handledClientName)
                    .setType(type)
                    .setClient(client)
                    .setFilteredData(handledClientNameData)
                    .setSimilarity(StringUtils.longestCommonString(handledClientNameData, handledClientName))
                    .setDistance(StringUtils.editDistance(handledClientName, handledClientNameData));
            //如果只有一个字相符就直接过滤掉
            if (matchInfo.getSimilarity() > 1) {
                queue.add(matchInfo);
            }
            if (queue.size() + 1 >= capacity) {
                queue.poll();
            }
        }
        return queue;
    }

    public List<MatchInfo> match(Location location, String data) {
        if (database.queryMatchCache(data) != null) {
            return new ArrayList<MatchInfo>() {{
                Client client = database.queryMatchCache(data);
                String clientName = client.getName();
                add(new MatchInfo()
                        .setRawClient(clientName)
                        .setRawData(data)
                        .setType("往期记录")
                        .setClient(client)
                        .setFilteredClient("")
                        .setFilteredData("")
                        .setSimilarity(StringUtils.longestCommonString(data, clientName))
                        .setDistance(StringUtils.editDistance(clientName, data)));
            }};
        }
        PriorityQueue<MatchInfo> queue = new PriorityQueue<>((a, b) -> Integer.compare(b.similarity, a.similarity));
        List<Location> dataLocations = new ArrayList<>();
        if (location == null) {
            dataLocations.addAll(MatchUtils.getLocationInString(data));
        } else {
            dataLocations.add(location);
        }
        for (Location dataLocation : dataLocations) {
            queue.addAll(scan(data, 5, database.getClientByLocation(dataLocation), "地区筛选"));
        }
        if (queue.isEmpty() || queue.size() < 5) {
            queue.addAll(scan(data, 5 - queue.size(), database.getAllClient(), "全客户表扫描"));
        }
        return new ArrayList<>(queue);
    }


    public static void main(String[] args) throws IOException {
        List<String> clientDataList = Files.readAllLines(Paths.get("C:\\Users\\Mairuis\\Desktop\\data.txt"));
        List<String> filterWord = Stream.concat(
                Stream.of("(", ")", "（", "）", "-", " ", "午餐", "晚餐", "早餐", "中餐", "食堂", "到点晚餐"),
                Stream.of(Location.values()).map(Location::getValue)
        ).collect(toList());
        ClientMatcher matcher = new ClientMatcher(filterWord
                , ClientDatabase.createByWorkbook(
                WorkbookFactory.create(new File("E:\\Github\\mtools\\data\\客户数据.xlsx"))));
        for (String clientData : clientDataList) {
            List<MatchInfo> matchInfo = matcher.match(null, clientData);
            if (!matchInfo.isEmpty()) {
                MatchInfo similar = matchInfo.get(0);
                System.out.println(clientData + '\t' +
                        similar.getRawClient() + '\t' +
                        similar.getDistance() + '\t' +
                        JSON.toJSONString(matchInfo.stream()
                                .map(MatchInfo::getRawClient)
                                .collect(toList())) +
                        '\t');
            }
        }
    }
}
