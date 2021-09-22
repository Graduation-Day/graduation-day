package com.graduation.actions;

import com.graduation.client.GameClient;
import com.graduation.elements.Player;
import com.graduation.utils.ConsoleColor;
import com.graduation.utils.Prompter;
import com.graduation.utils.Sound;
import com.graduation.utils.readMap;
import jdk.swing.interop.SwingInterOpUtils;

import java.util.Scanner;

public class GameAction {

    private static Scanner action = new Scanner(System.in);
    Sound sound = new Sound();


    public static void getAction() throws Exception {

        Prompter.clearScreen();
        System.out.println(readMap.convertedMap());
        System.out.println(ConsoleColor.GREEN_BOLD
                + "                                   *******************************************************************************************");
        System.out.println("                                   " + GameClient.getPlayer());
        System.out.println(
                "                                   ******************************************************************************************* "
                        + ConsoleColor.RESET);
        showValidCommands();
        String move = GameClient.getPrompter().prompt("Enter a move from above options: \n ");
        String[] moveArray = move.toLowerCase().split(" ");

        // validate userInput for valid command length
        while (moveArray.length != 2) {
            showValidCommands();
            move = GameClient.getPrompter().prompt("Enter a move from above options: \n ");
            moveArray = move.toLowerCase().split(" ");
        }


        switch (moveArray[0]) {
            case "go":
                if (GameClient.getValidDirections().contains(moveArray[1])) {
                    GameClient.nextLocation(moveArray[1]);
                } else {
                    System.out.println(ConsoleColor.RED + "\n\nAht aht.. you didn't enter a valid cardinal direction"
                            + ConsoleColor.RESET);
                    getAction();

                }
                break;
            case "get":
                //Calls the method to initiate the item sequence
                GameClient.getLevelDetails("item");
                break;
            default:
                System.out.println(ConsoleColor.RED + "You entered a an invalid move. Type \"H\" for the instructions."
                        + ConsoleColor.RESET);
                getAction();
                break;
        }

    }

    private static void showValidCommands() {
        if (GameClient.displayRoomInventory() != null) {
            System.out.println("GET " + "item");
        }
        System.out.println("GO " + GameClient.getValidDirections());
    }
}