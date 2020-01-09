package com.mairuis.excel.tools.client;

import com.mairuis.excel.entity.Location;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Mairuis
 * @since 2020/1/9
 */
@Data
@Accessors(chain = true)
public class Client {
    private Location location;
    private String name;
    private String code;
}