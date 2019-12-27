package com.mairuis.excel.tools.utils;

import com.alibaba.fastjson.JSON;
import com.mairuis.excel.entity.Location;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/27
 */
public class MatchUtils {


    public static List<Location> getLocationInString(String str) {
        List<Location> list = new ArrayList<>();
        for (Location location : Location.values()) {
            if (str.contains(location.getValue())) {
                list.add(location);
            }
        }
        return list;
    }


}
