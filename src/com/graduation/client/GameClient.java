package com.graduation.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.actions.GameAction;
import com.graduation.actions.GameCombat;
import com.graduation.elements.Bully;
import com.graduation.elements.Player;
import com.graduation.pointsystem.PointSystem;
import com.graduation.utils.ConsoleColor;
import com.graduation.utils.Grade;
import com.graduation.utils.Prompter;
import com.graduation.utils.Sound;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GameClient {
    private static Prompter prompter;
    private static Player player;
    private static Bully bully;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static JsonNode data;
    private static JsonNode prevRoom;
    private static final List<String> notSubject = new ArrayList<>(Arrays.asList("gym", "cafeteria", "hallway"));
    public static List<String> items;



    private static Sound sound = new Sound();


    public GameClient(Prompter prompter) {
        this.prompter = prompter;
    }

    public void initialize() throws Exception {
        // play game start sound
        Clip southpark = sound.SoundTrack("Sounds/southpark1.wav");
        // create player and bully instance
        player = setPlayer();
        bully = setBully();

        // clear screen
        Prompter.clearScreen();
        southpark.stop();
        //Step 1a -- Generate the location info from the json
        getLevelDetails("desc");
        // Some conditional seeing if its is a subject

        if (notSubject.contains(Player.getLocation())) {
            continueJourney(false);
        } else {
            PointSystem.teacherQuestions(Player.getLocation().toLowerCase(), Player.getGrade(), player);
        }
    }

    public static void nextLocation(String location) throws Exception {
        // Grab the previous and read the location according to direction within it's
        // JSON properties
        try {

            String nextLoc = prevRoom.get(location).textValue();
            player.setLocation(nextLoc);
            Prompter.clearScreen();
            getLevelDetails("desc");
            displayRoomInventory();

            // Determine if it's a subject room
            if (!notSubject.contains(nextLoc.toLowerCase())) {
                PointSystem.teacherQuestions(Player.getLocation().toLowerCase(), Player.getGrade(), player);
            } else {
                // Step 1: random number generator to see if a bully will engage in combat
                int combat = (int) (Math.random() * 100);
                // You have a 50% chance of a bully not being there.
                if (combat >= 50) {
                    System.out.println("Uh oh...  bully is here. And they spot you. Engaging in combat ");
                    // Engage in combat
                    sound.playSoundClip("Sounds/pokemonstartfight.wav");
                    GameCombat.initializeCombatScene();
                } else {
                    continueJourney(false);
                }
            }
            //bully has spotted you
            // Catch if the direction is null
        } catch (NullPointerException e) {
            System.out.println("You can't go that direction! Quick Try a different cardinal direction please");
            GameAction.getAction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JsonNode displayRoomInventory() {
        JsonNode roomInventory = null;
        if (isValidKey(data, Player.getLocation(), Player.getGrade(), "item")) {
            roomInventory = getDetails(data, Player.getLocation(), Player.getGrade(), "item");
            System.out.println("Room Inventory :" + ConsoleColor.GREEN + roomInventory + ConsoleColor.RESET + "\n");
        }
        return roomInventory;
    }

    public static void getLevelDetails(String key) {
        try {
            data = mapper.readTree(Files.readAllBytes(Paths.get("Banner/rooms.json")));
            prevRoom = getLastRoom(data, Player.getLocation(), Player.getGrade());
            JsonNode filteredData = getDetails(data, Player.getLocation(), Player.getGrade(), key);
            if (key.equals("item")) {
                // If the room does have an item check if player already has it!
                if (player.getInventory().contains(filteredData.asText())) {
                    // View to tell the user that they grabbed the room item already
                    System.out.println(
                            ConsoleColor.RED + "There are no items to grab from this room. " + ConsoleColor.RESET);
                    continueJourney(false);
                } else {
                    // Method to add the item to the player's backpack
                    items = player.getInventory();
                    items.add(filteredData.textValue());
                    System.out.println(ConsoleColor.GREEN + "Successfully added " + filteredData + " to your backpack!"
                            + ConsoleColor.RESET);
                    continueJourney(false);
                }
            } else {

                System.out.println("\n\n            " + ConsoleColor.GREEN + filteredData.asText() + ConsoleColor.RESET);
            }
        } catch (IOException e) {
            System.out.println(e);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Method to initialize the action to move
    public static void continueJourney(boolean val) throws Exception {
        //Have a conditional that switch when it's a new level
        if (val) {
            getLevelDetails("desc");
            PointSystem.teacherQuestions(Player.getLocation().toLowerCase(), Player.getGrade(), player);
        } else {
            GameAction.getAction();
        }
    }

    // Gets the description of the current room
    private static JsonNode getDetails(JsonNode node, String location, Grade grade, String key) {
        return node.get(String.valueOf(grade)).get(location).get(key);
    }

    // Assigns value to the prevRoom. Assists with keeping track of the location of
    // player
    private static JsonNode getLastRoom(JsonNode node, String location, Grade grade) {
        return node.get(String.valueOf(grade)).get(location);
    }

    public static String getFirstLocation() {
        try {
            // Step 1: Read our JSON file
            data = mapper.readTree(Files.readAllBytes(Paths.get("Banner/rooms.json")));
            // Step 2: Access to my level
            String node = String.valueOf(data.get(String.valueOf(Player.getGrade())));
            // Step 3: Spilt to get my location string
            String strNew = node.replace("{\"", "");
            String[] arrOfStr = strNew.split("\"", 2);
            // Step 4: Send back the first part which is the init location for the level
            return arrOfStr[0];
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e);
            return "Computers"; // default value
        }
    }

    // Method to check if valid direction at a given level and room
    static boolean isValidKey(JsonNode node, String location, Grade grade, String key) {
        return node.get(String.valueOf(grade)).get(location).has(key);
    }

    // Return valid directions at a given level and room
    public static List<String> getValidDirections() {
        List<String> directions = new ArrayList<>(Arrays.asList("north", "east", "south", "west"));
        List<String> validDirections = new ArrayList<>();
        for (String direction : directions) {
            if (isValidKey(data, Player.getLocation(), Player.getGrade(), direction)) {
                validDirections.add(direction);
            }
        }
        return validDirections;
    }

    // Initialize the bully
    public Bully setBully() {
        return new Bully("bully", 100, true);
    }

    // Initialize the player as a FRESHMAN aka first level with user provided name input
    public Player setPlayer() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ConsoleColor.YELLOW + "Please enter your name below: " + ConsoleColor.RESET);
        String userName = scanner.nextLine();
        // Validate user name is not blank or is not in the list of reserved command keywords
        while (userName.isBlank() || prompter.getCommands().contains(userName.toLowerCase())) {
            System.out.println(ConsoleColor.YELLOW + "Please enter your name below: " + ConsoleColor.RESET);
            userName = scanner.nextLine();
        }
        return new Player(userName, 0, 100, Grade.FRESHMAN, "Computers");
    }

    public static Player getPlayer() {

        return player;
    }

    public static Prompter getPrompter() {
        return prompter;
    }
}
