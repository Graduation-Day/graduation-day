package com.graduation.utils;

import com.graduation.client.GameClient;
import com.graduation.elements.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class readMap {

    private static String map;

    public static String convertedMap() throws Exception {
        String newMap = null;
        HashMap<String, String> mapCode = new HashMap<>();
        mapCode.put("Gym", "  ");
        mapCode.put("Geography", "  ");
        // mapCode.put("Player", GameClient.getPlayer().getName());
        mapCode.put("Hallway", "  ");
        // mapCode.put("Credit", String.valueOf(GameClient.getPlayer().getCredit()));
        mapCode.put("Maths", "  ");
        mapCode.put("Cafeteria", "  ");
        mapCode.put("History", "  ");
        mapCode.put("Computers", "  ");

        // String character = "\u00A0 o \n<|>\n_^_";
//        String c = "\uD83D\uDE00";
        String c = "ME";

        String character_me = ConsoleColor.YELLOW_BOLD + c + ConsoleColor.RESET;
        TextFileReader tfr = new TextFileReader();
        System.out.println("\n\n" + ConsoleColor.GREEN
                + "                                                        "+tfr.readDashBoard().get(0)+ "\n "
                + "                                                       "+tfr.readDashBoard().get(1) + " "
                + tfr.readDashBoard().get(2) + " " + GameClient.getPlayer().getLocation().toUpperCase()
                + "  "+tfr.readDashBoard().get(3) +" " + GameClient.getPlayer().getGrade() + tfr.readDashBoard().get(1)
                + "\n                                                        "+ tfr.readDashBoard().get(0) + ConsoleColor.RESET);

        // String character = "\u00A0 o \n<|>\n_^_";

        // System.out.println(GameClient.getFirstLocation());

        if (Player.getGrade() == Grade.FRESHMAN) {
            map = importTXT("Banner/map-" + GameClient.getPlayer().getGrade().toString() + ".txt");
            mapCode.replace(GameClient.getPlayer().getLocation(), character_me);

            newMap = String.format(map, mapCode.get("Gym"), mapCode.get("Geography"), mapCode.get("Hallway"),
                    mapCode.get("Maths"), mapCode.get("Cafeteria"), mapCode.get("History"), mapCode.get("Computers"));
            // mapCode.get("Player"), mapCode.get("Credit"),
        } else if (Player.getGrade() == Grade.SOPHOMORE) {
            map = importTXT("Banner/map-" + GameClient.getPlayer().getGrade().toString() + ".txt");
            mapCode.replace(GameClient.getPlayer().getLocation(), character_me);
            newMap = String.format(map, mapCode.get("Gym"), mapCode.get("Geography"),
                    mapCode.get("History"), mapCode.get("Hallway"), mapCode.get("Cafeteria"),
                    mapCode.get("Maths"), mapCode.get("Computers"));
        } else if (Player.getGrade() == Grade.JUNIOR) {
            map = importTXT("Banner/map-" + GameClient.getPlayer().getGrade().toString() + ".txt");
            mapCode.replace(GameClient.getPlayer().getLocation(), character_me);
            newMap = String.format(map, mapCode.get("Computers"),  mapCode.get("Hallway"),
                    mapCode.get("Gym"),  mapCode.get("Cafeteria"), mapCode.get("Maths"),
                    mapCode.get("Geography"), mapCode.get("History"));
        } else if (Player.getGrade() == Grade.SENIOR) {
            map = importTXT("Banner/map-" + GameClient.getPlayer().getGrade().toString() + ".txt");
            mapCode.replace(GameClient.getPlayer().getLocation(), character_me);
            newMap = String.format(map, mapCode.get("Computers"), mapCode.get("History"),
                    mapCode.get("Gym"), mapCode.get("Hallway"), mapCode.get("Geography"),
                    mapCode.get("Cafeteria"), mapCode.get("Maths"));
        }
        return newMap;
    }

    public static String importTXT(String path) {
        String result = null;
        try {
            result = Files.readString(Path.of(path));
        } catch (IOException e) {
        }
        return result;
    }
}
