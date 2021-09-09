package com.graduation.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.actions.GameAction;
import com.graduation.actions.GameCombat;
import com.graduation.elements.Bully;
import com.graduation.elements.Player;
import com.graduation.pointsystem.PointSystem;
import com.graduation.utils.Grade;
import com.graduation.utils.Prompter;

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

    public GameClient(Prompter prompter) {
        this.prompter = prompter;
    }
    public void initialize(){
        player = setPlayer();
        bully = setBully();
        //Step 1 -- Generate the location info from the json
        getLevelDetails("desc");

        //Step 2a -- Some conditional seeing if its is a subject
        if(Player.getLocation().equals("cafeteria") || Player.getLocation().equals("gym") || Player.getLocation().equals("hallway")){
            continueJourney(false);
        }else{
            //Step 2b -- Call method to initialize the question sequence
            PointSystem.teacherQuestions(Player.getLocation().toLowerCase(), Player.getGrade(),player);
        }
    }

    public static void nextLocation(String location){
        //Grab the previous and read the location according to direction within it's JSON properties
        try{

            String nextLoc = prevRoom.get(location).textValue();
            player.setLocation(nextLoc);
            getLevelDetails("desc");

            //Determine if it's a subject room
            if(!notSubject.contains(nextLoc.toLowerCase())){
                PointSystem.teacherQuestions(Player.getLocation().toLowerCase(), Player.getGrade(),player);
            }else{
                //Step 1: random number generator to see if a bully will engage in combat
                int combat = (int)(Math.random() * 100);
                    //You have a 50% chance of a bully not being there.
                if(combat >= 50){
                    System.out.println("Uh oh... " + bully.getName() + " is here. And they spot you. Engaging in combat ");
                    //Engage in combat
                    GameCombat.initializeCombatScene();
                }else {
                    continueJourney(false);
                }
            }
            //Catch if the direction is null
        }catch(NullPointerException e){
            System.out.println("You can't go that direction! Quick Try a different cardinal direction please");
            GameAction.getAction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getLevelDetails(String key){
        try{
            data = mapper.readTree(Files.readAllBytes(Paths.get("Banner/rooms.json")));
            prevRoom = getLastRoom(data, Player.getLocation(), Player.getGrade());
            JsonNode filteredData = getDetails(data, Player.getLocation(), Player.getGrade(), key);
            if(key.equals("item")){
                //If the room does have an item check if player already has it!
                if(player.getInventory().contains(filteredData.asText())){
                    //View to tell the user that they grabbed the room item already
                    System.out.println("There are no more items to grab from this room...\nremember you grabbed the " + filteredData + "\n");
                    continueJourney(false);
                }else{
                    //Method to add the item to the player's bookbag
                    List<String> items = player.getInventory();
                    items.add(filteredData.textValue());
                    System.out.println("Successfully added " + filteredData + " to your backpack!");
                    continueJourney(false);
                }
            }else{
                System.out.println(filteredData);
            }
        }catch(IOException e){

            System.out.println(e);
        }
    }

    //Method to initialize the action to move
    public static void continueJourney(boolean val){
        //Have a conditional that switch when it's a new level
        if(val){
            getLevelDetails("desc");
            PointSystem.teacherQuestions(Player.getLocation().toLowerCase(), Player.getGrade(),player);
        }else{
            System.out.println("Whats your next move?");
            GameAction.getAction();
        }
    }

    //Gets the description of the current room
    private static JsonNode getDetails(JsonNode node, String location, Grade grade, String key) {
         return node.get(String.valueOf(grade)).get(location).get(key);
    }

    //Assigns value to the prevRoom. Assists with keeping track of the location of player
    private static JsonNode getLastRoom(JsonNode node, String location, Grade grade) {
         return node.get(String.valueOf(grade)).get(location);
    }

    public static String getFirstLocation(){
        try{
            //Step 1: Read our JSON file
            data = mapper.readTree(Files.readAllBytes(Paths.get("Banner/rooms.json")));
            //Step 2: Access to my level
            String node = String.valueOf(data.get(String.valueOf(Player.getGrade())));
            //Step 3: Spilt to get my location string
            String strNew = node.replace("{\"", "");
            String[] arrOfStr = strNew.split("\"", 2);
            //Step 4: Send back the first part which is the init location for the level
           return arrOfStr[0];
        }catch(IOException e){
            System.out.println("Something went wrong: " + e);
            return "Computers"; //default value
        }
    }

    //Initialize the bully
    public Bully setBully() {

        return new Bully("bully", 100, true);

    }

    //Initialize the player as a FRESHMAN aka first level
    public Player setPlayer() {
        String userName = prompter.prompt("Please enter your name below: \n");
        return new Player(userName, 0, 100, Grade.FRESHMAN, "Computers");
    }

    public static Player getPlayer() {
        return player;
    }

    public static Prompter getPrompter() {
        return prompter;
    }
}
