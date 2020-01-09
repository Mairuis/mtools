package com.mairuis.excel.tools.client;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Mairuis
 * @since 2020/1/9
 */
@Data
@Accessors(chain = true)
public class MatchInfo {
    int similarity;
    int distance;
    String rawData;
    String filteredData;
    String rawClient;
    String filteredClient;
    String type;
    Client client;
}