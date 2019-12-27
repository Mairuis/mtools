package com.mairuis.excel.tools.utils;

import com.alibaba.fastjson.JSON;
import com.mairuis.excel.entity.Location;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Queue;

import static com.mairuis.excel.tools.utils.MatchUtils.getLocationInString;

/**
 * @author Mairuis
 * @since 2019/12/27
 */
public class ClientMatcher {

    public ClientMatcher(List<String> client) {

    }

    public static void testLike() throws IOException {
        List<String> clientList = Files.readAllLines(Paths.get("C:\\Users\\Mairuis\\Desktop\\client.txt"));
        ClientMatcher matcher = new ClientMatcher(clientList);
        List<String> clientDataList = Files.readAllLines(Paths.get("C:\\Users\\Mairuis\\Desktop\\data.txt"));
        for (String c : clientDataList) {
            System.out.println(c + "|" + JSON.toJSONString(match(c)));
        }
    }

    private static Queue<String> match(String src) {
        List<Location> srcLocation = getLocationInString(src);

        return null;
    }

}
