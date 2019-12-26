package com.mairuis.excel.tools.utils;

import com.mairuis.excel.entity.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/27
 */
public class StringUtils {


    public List<Location> getLocationInString(String str) {
        List<Location> list = new ArrayList<>();
        for (Location location : Location.values()) {
            if (str.contains(location.getValue())) {
                list.add(location);
            }
        }
        return list;
    }

}
