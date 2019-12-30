package com.mairuis.excel.tools.utils;

import com.alibaba.fastjson.JSON;
import com.mairuis.excel.entity.Location;
import com.mairuis.utils.StringUtils;
import lombok.Data;
import lombok.experimental.Accessors;

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
    private List<String> client;
    private Map<Location, List<String>> locationClientMap;
    private Map<String, List<Location>> clientLocationMap;

    public ClientMatcher(List<String> client, List<String> filterWord, List<String> ignoreWord) {
        this(client, filterWord, ignoreWord, new HashMap<>());
    }

    public ClientMatcher(List<String> client, List<String> filterWord, List<String> ignoreWord, Map<String, List<Location>> clientLocationMap) {
        this.filterWord = filterWord;
        this.client = client;
        this.clientLocationMap = clientLocationMap;

        this.ignore(client, ignoreWord);
        this.buildLocationMap();
    }

    private List<String> ignore(List<String> client, List<String> ignoreWord) {
        for (Iterator<String> iterator = client.iterator(); iterator.hasNext(); ) {
            String s = iterator.next();
            for (String s1 : ignoreWord) {
                if (s.contains(s1)) {
                    iterator.remove();
                    break;
                }
            }
        }
        return client;
    }

    private void buildLocationMap() {
        this.locationClientMap = new HashMap<>();
        for (String client : client) {
            List<Location> locations = getLocation(client);
            if (locations.isEmpty()) {
                locationClientMap.computeIfAbsent(Location.UNKNOWN, x -> new ArrayList<>()).add(client);
            } else {
                for (Location location : locations) {
                    locationClientMap.computeIfAbsent(location, x -> new ArrayList<>()).add(client);
                }
            }
        }
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

    private PriorityQueue<MatchResult> scan(String clientNameData, int capacity, List<String> clients) {
        PriorityQueue<MatchResult> queue = new PriorityQueue<>(
                Comparator.comparingInt((MatchResult x) -> x.similarity)
                        .thenComparingInt(x -> -(Math.abs(clientNameData.length() - x.rawClient.length())))
        );
        String handledClientNameData = filter(clear(clientNameData));
        for (String clientName : clients) {
            String handledClientName = filter(clear(clientName));
            MatchResult matchResult = new MatchResult()
                    .setRawClient(clientName)
                    .setRawData(clientNameData)
                    .setFilteredClient(handledClientName)
                    .setFilteredData(handledClientNameData)
                    .setSimilarity(StringUtils.longestCommonString(handledClientNameData, handledClientName))
                    .setDistance(StringUtils.editDistance(handledClientName, handledClientNameData));
            if (matchResult.getSimilarity() > 1 && matchResult.getSimilarity() <= handledClientName.length()) {
                queue.add(matchResult);
            }
            //如果只有一个字相符就直接过滤掉
            if (queue.size() + 1 >= capacity) {
                queue.poll();
            }
        }
        return queue;
    }

    private List<MatchResult> match(Location location, String data) {
        List<Location> srcLocation = new ArrayList<>();
        PriorityQueue<MatchResult> queue = new PriorityQueue<>((a, b) -> Integer.compare(b.similarity, a.similarity));
        if (location == null) {
            srcLocation.addAll(getLocation(data));
        }
        for (Location location1 : srcLocation) {
            queue.addAll(scan(data, 5, locationClientMap.get(location1)));
        }
        if (queue.isEmpty()) {
            queue.addAll(scan(data, 3, client));
        }
        return new ArrayList<>(queue);
    }

    private List<Location> getLocation(String src) {
        List<Location> locations = clientLocationMap.get(src);
        if (locations == null || locations.isEmpty()) {
            locations = MatchUtils.getLocationInString(src);
        }
        return locations;
    }

    @Data
    @Accessors(chain = true)
    public static class MatchResult {
        int similarity;
        int distance;
        String rawData;
        String filteredData;
        String rawClient;
        String filteredClient;
    }

    public static void main(String[] args) throws IOException {
        List<String> clientList = Files.readAllLines(Paths.get("C:\\Users\\Mairuis\\Desktop\\client.txt"));
        List<String> clientDataList = Files.readAllLines(Paths.get("C:\\Users\\Mairuis\\Desktop\\data.txt"));
        List<String> filterWord = Stream.concat(
                Stream.of("(", ")", "（", "）", "-", " ", " ", "午餐", "晚餐", "早餐", "中餐", "食堂", "到点晚餐"),
                Stream.of(Location.values()).map(Location::getValue)
        ).collect(toList());
        List<String> ignoreWord = Stream.of(
                "停用", "弃用", "错误", "作废"
        ).collect(toList());
        ClientMatcher matcher = new ClientMatcher(clientList, filterWord, ignoreWord);
        for (String clientData : clientDataList) {
            List<MatchResult> matchResult = matcher.match(null, clientData);
            if (!matchResult.isEmpty()) {
                MatchResult similar = matchResult.get(0);
                System.out.println(new StringBuilder()
                        .append(clientData).append('\t')
                        .append(similar.getRawClient()).append('\t')
                        .append(similar.getDistance()).append('\t')
                        .append(JSON.toJSONString(matchResult.stream()
                                .map(MatchResult::getRawClient)
                                .collect(toList()))).append('\t')
                        .toString());
            }
        }
    }
}
