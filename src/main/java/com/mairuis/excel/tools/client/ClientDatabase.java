package com.mairuis.excel.tools.client;

import com.mairuis.excel.entity.Location;
import com.mairuis.excel.tools.utils.Cells;
import com.mairuis.excel.tools.utils.Rows;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author Mairuis
 * @since 2020/1/9
 */
public class ClientDatabase {

    private Map<String, Client> memory;
    private List<Client> clients;
    private Map<Location, List<Client>> locationListMap;

    public ClientDatabase(Map<String, Client> memory, List<Client> clients) {
        this.memory = memory;
        this.clients = clients;
        this.locationListMap = new HashMap<>();
        for (Client client : clients) {
            Location location = client.getLocation();
            if (location == null) {
                locationListMap.computeIfAbsent(Location.UNKNOWN, x -> new ArrayList<>()).add(client);
            } else {
                locationListMap.computeIfAbsent(location, x -> new ArrayList<>()).add(client);
            }
        }
    }

    public List<Client> getClientByLocation(Location location) {
        return locationListMap.getOrDefault(location, new ArrayList<>());
    }

    public Client queryMatchCache(String rawData) {
        return memory.get(rawData);
    }

    public List<Client> getAllClient() {
        return clients;
    }

    public static ClientDatabase createByWorkbook(Workbook workbook) {
        List<String> ignoreWord = Stream.of("停用", "弃用", "错误", "作废").collect(toList());
        Map<String, Client> memory = new HashMap<>();
        Map<String, Client> clientMap = new HashMap<>();
        List<Client> clientList = new ArrayList<>();

        Sheet mappingSheet = workbook.getSheet("映射");
        Sheet clientSheet = workbook.getSheet("档案");
        Rows.toList(clientSheet, 1, clientSheet.getLastRowNum())
                .stream()
                .filter(Objects::nonNull)
                .filter(x -> !Rows.isEmptyRow(x))
                .filter(x -> !Cells.isEmpty(x.getCell(2)))
                .filter(x -> !Cells.isEmpty(x.getCell(4)))
                .filter(x -> {
                    String clientName = Cells.toString(x.getCell(4));
                    for (String ignore : ignoreWord) {
                        if (clientName.contains(ignore)) {
                            return false;
                        }
                    }
                    return true;
                })
                .map(x -> new Client().
                        setCode(Cells.toString(x.getCell(2)))
                        .setName(Cells.toString(x.getCell(4))))
                .forEach(x -> {
                    clientMap.put(x.getCode(), x);
                    clientList.add(x);
                });
        Rows.toList(mappingSheet, 1, mappingSheet.getLastRowNum())
                .stream()
                .filter(Objects::nonNull)
                .filter(x -> !Rows.isEmptyRow(x))
                .filter(x -> !Cells.isEmpty(x.getCell(0)))
                .filter(x -> !Cells.isEmpty(x.getCell(1)))
                .filter(x -> !Cells.isEmpty(x.getCell(2)))
                .filter(x -> !Cells.isEmpty(x.getCell(3)))
                .collect(toList())
                .forEach(x -> {
                    String code = Cells.toString(x.getCell(3));
                    String data = Cells.toString(x.getCell(1));
                    String city = Cells.toString(x.getCell(0));
                    if (clientMap.containsKey(code)) {
                        Client client = clientMap.get(code);
                        client.setLocation(Location.find(city));
                        memory.put(data, client);
                    }
                });
        return new ClientDatabase(memory, clientList);
    }
}
