package com.graduation.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.client.GameClient;
import com.graduation.elements.Bully;
import com.graduation.elements.Player;
import com.graduation.utils.Prompter;
import com.graduation.utils.readMap;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class GameCombat {
    private static Scanner action = new Scanner(System.in);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static int bullyHitPoints = (int)(Math.random() * 25);
    private static JsonNode data;

    public static void initializeCombatScene() {
        Prompter.clearScreen();
        System.out.println(GameClient.getPlayer());
        System.out.println(readMap.convertedMap());
        System.out.println("*******************************");
        System.out.println(Bully.getName() + " has spotted you!");
        fight();
    }

    public static void fight() {
        if (Player.getHealth() <= 0) {
            //Step 1: You get a report card stolen
            String reportCard = Player.getSubjectTaken().remove(0);             //remove first entry, store the string for later
            Player.setSubjectTaken(Player.getSubjectTaken());                         //pass back the List to the method
            //Step 2: Dialogue for the user
            System.out.println("The Bully stole a report card! You must take a " + reportCard + " again.");
            //Step 3: Remove the bully for this level
            Bully.setPresence(false);

        }else{
            Prompter.clearScreen();
            System.out.println(GameClient.getPlayer());
            System.out.println(readMap.convertedMap());
            System.out.println("*******************************");
            System.out.println("Health Points: " + Player.getHealth());
            System.out.println("Bully's health " + Bully.getHealth());
            System.out.println("*******************************");
            System.out.println("Commands: \nRUN\nUSE [item]\nFIGHT");
            System.out.println("*******************************");
            System.out.println(Player.getName() + "'s bookbag items:");
            for (String item : Player.getInventory()) {
                System.out.println(item);
            }
            System.out.println("*******************************");

            System.out.println("Enter move: ");
            String move = action.nextLine();
            String[] moveArray = move.toLowerCase().split(" ");

            switch (moveArray[0]) {
                case "run":
                    //Step 1: RNG to find out if you can run successfully -- you have a 12% chance
                    if((int)(Math.random() * 100) >= 12){
                        System.out.println("You were able to run away successfully!! You left the Bully int he dust. He got snagged by the Principal");
                        GameAction.getAction();
                    }else{
                        //Massive damage if caught tryign to run
                        int rightHook = Player.getHealth() - 50 ;
                        Player.setHealth(rightHook);
                        System.out.println("You didn't get so lucky.. Bully hit you with a right hook to the jaw.");
                        fight();
                    }
                    break;
                case "fight":
                    //Step 1: RNG for number with just your fists && Negate the points from the Bully's health-- allows for a one hit kill
                    int kick = (int)(Math.random() * 100);
                    Bully.setHealth(Bully.getHealth() - kick);
                    //Step 2: Conditional dialogue
                    if(kick > 80) {
                        System.out.println("You kicked him in the right spot! You inflicted " + kick + " damage!!");
                    }else if(kick > 50){
                        System.out.println("That kick really hurt the Bully! You inflicted " + kick + " to the bully!!");
                    }else {
                        System.out.println("Wow that didn't them hurt much... You inflicted " + kick + " to the bully!!");
                    }
                    //Step 3: Recursion if needed
                    if(Bully.getHealth() <= 0){
                        System.out.println("You defeated " + Bully.getName() + " Hip hip Hooray!! Bully says he'll get you next year.");
                        GameAction.getAction();
                    }else{
                        //Bully's turn;
                        bullyAttack();
                        fight();
                    }
                    break;
                case "use":
                    try{
                        if (Player.getInventory().contains(moveArray[1])) {
                            //Step 1: remove the item
                            Player.getInventory().remove(moveArray[1]);
                            //Step 2: RNG for result -- 90% of a good ending, 10% chance the item has no effect
                            if((Math.random() * 100) > 10){
                                //Step 2a: Print the item desc
                                getItemDesc(moveArray[1]);
                                    //***Bully defeated*
                                Bully.setHealth(0);
                                //Toggle the bully presence var
                                Bully.setPresence(false);
                                //Get the next action
                                GameAction.getAction();
                            }else{
                                //Step 2b: Bully was unaffected by the item
                                System.out.println(Bully.getName() + " was unaffected by " + moveArray[1]);
                            }
                            //Step 4: Snap to the Bully response if still health points left
                            if(Bully.getHealth() > 0){
                                System.out.println("Hit3");
                                bullyAttack();
                            }
                        } else {
                            System.out.println("You don't have a " + moveArray[1] + " in your book bag");
                            fight();
                        }
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("That item is not in your book bag!");
                        fight();
                    }
                    break;
                default:
                    System.out.println("You entered an invalid move. Type \"H\" for the instructions");
                    fight();
                    break;
            }
        }
    }

    private static void getItemDesc(String item) {
        try {
            data = mapper.readTree(Files.readAllBytes(Paths.get("Banner/items.json")));
            JsonNode filteredData = data.get(String.valueOf(item));
            System.out.println(filteredData.asText());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void bullyAttack(){
        //Step 1: Set the player health minus the bully strike
        int punch = Player.getHealth() - bullyHitPoints ;
        Player.setHealth(punch);
        //Step 2: Conditional dialogue
        if(bullyHitPoints > 50) {
            System.out.println("Massive damaged! Bully inflicted " + bullyHitPoints + " to you!!");
        }else if(bullyHitPoints > 25){
            System.out.println("That punch hurt! Bully inflicted " + bullyHitPoints + " to you!!");
        }else{
            System.out.println("Wow that didn't hurt much... Bully inflicted " + bullyHitPoints + " to you!!");
        }
    }
}