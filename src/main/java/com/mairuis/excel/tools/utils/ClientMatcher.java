package com.mairuis.excel.tools.utils;

import com.alibaba.fastjson.JSON;
import com.mairuis.excel.entity.Location;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mairuis.excel.tools.utils.MatchUtils.getLocationInString;

/**
 * @author Mairuis
 * @since 2019/12/27
 */
public class ClientMatcher {

    private List<String> filterWord;
    private List<String> client;
    private Map<Location, List<String>> locationClientMap;
    private Map<String, List<Location>> clientLocationMap;

    public ClientMatcher(List<String> filterWord, List<String> client) {
        this.filterWord = filterWord;
        this.client = client;

        this.buildLocationMap(client);
    }

    private void buildLocationMap(List<String> clientList) {
        this.locationClientMap = new HashMap<>();
        this.clientLocationMap = new HashMap<>();
        for (String client : clientList) {
            List<Location> locations = getLocationInString(client);
            String clientWord = filter(client);
            if (locations.isEmpty()) {
                locationClientMap.computeIfAbsent(Location.UNKNOWN, x -> new ArrayList<>()).add(clientWord);
                clientLocationMap.put(clientWord, new ArrayList<>());
            } else {
                for (Location location : locations) {
                    locationClientMap.computeIfAbsent(location, x -> new ArrayList<>()).add(clientWord);
                    clientLocationMap.computeIfAbsent(clientWord, x -> new ArrayList<>()).add(location);
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

    private Queue<String> match(String src) {
        List<Location> srcLocation = getLocationInString(src);
        String string = filter(src);

        return null;
    }

    public static void main(String[] args) throws IOException {
        List<String> clientList = Files.readAllLines(Paths.get("C:\\Users\\Mairuis\\Desktop\\client.txt"));
        List<String> clientDataList = Files.readAllLines(Paths.get("C:\\Users\\Mairuis\\Desktop\\data.txt"));
        List<String> filterWord = Stream.concat(
                Stream.of("(", ")", "（", "）", "-", " ", " "),
                Stream.of(Location.values()).map(Location::getValue)
        ).collect(Collectors.toList());
        ClientMatcher matcher = new ClientMatcher(clientList, filterWord);
        for (String clientData : clientDataList) {
            System.out.println(clientData + "|" + JSON.toJSONString(matcher.match(clientData)));
        }
    }

}
