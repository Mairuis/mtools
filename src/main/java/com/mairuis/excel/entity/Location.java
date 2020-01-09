package com.mairuis.excel.entity;

/**
 * 描述
 *
 * @author Mairuis
 * @date 2019/12/27
 */
public enum Location {
    北京("北京"),
    滨州("滨州"),
    常州("常州"),
    成都("成都"),
    大连("大连"),
    东莞("东莞"),
    佛山("佛山"),
    福州("福州"),
    广西("广西"),
    广州("广州"),
    杭州("杭州"),
    合肥("合肥"),
    湖州("湖州"),
    惠州("惠州"),
    济南("济南"),
    济宁("济宁"),
    嘉兴("嘉兴"),
    金华("金华"),
    临沂("临沂"),
    南昌("南昌"),
    南京("南京"),
    南宁("南宁"),
    南通("南通"),
    宁波("宁波"),
    青岛("青岛"),
    全国("全国"),
    汕头("汕头"),
    上海("上海"),
    绍兴("绍兴"),
    深圳("深圳"),
    沈阳("沈阳"),
    苏州("苏州"),
    台州("台州"),
    天津("天津"),
    温州("温州"),
    无锡("无锡"),
    武汉("武汉"),
    厦门("厦门"),
    徐州("徐州"),
    烟台("烟台"),
    扬州("扬州"),
    郑州("郑州"),
    中山("中山"),
    珠海("珠海"),
    淄博("淄博"),

    UNKNOWN("未知");

    public static Location find(String location) {
        for (Location location1 : values()) {
            if (location.equals(location1.getValue())) {
                return location1;
            }
        }
        return Location.UNKNOWN;
    }

    private String value;

    public String getValue() {
        return value;
    }

    Location(String value) {
        this.value = value;
    }
}
