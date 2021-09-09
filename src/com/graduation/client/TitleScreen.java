package com.graduation.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPairGenerator;
import java.util.Scanner;


class TitleScreen {
    //Fields
    private static String banner;
    Scanner titleObj = new Scanner(System.in);
    String keystroke = titleObj.nextLine().toLowerCase();
    String credits = "This game was produced by Hongyi Qu, Pierre Gober, Stephen Yeboah\n" +
            " Jauric Flowers.\n";

    String help = "You must answer a series of questions to get a passing grade.\n" +
            "You need 4 badges in order to graduate high school. Also,\n" +
            "watch out for the bully and collect hidden items.\n" +
            "Actions:\n" +
            "\n" +
            "GO [north, south, east, west, up, down]\n" +
            "\n" +
            "GET [item]\n" +
            "\n" +
            "INV/INVENTORY";

    public void inputOptions(String keystroke) {
        //We will use this later during the game as an option to bring
        System.out.println("Type 'Q' to quit the game." +
                "Type 'Z' to look at the credits. Type 'H' to look at the help tutorial");
        switch (keystroke) {
            case "q":
                System.exit(0);
                break;
            case "z":
                System.out.println(credits);
                break;
            case "h":
                System.out.println(help);
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
//        System.out.println(keystroke);
    }


    //Displays title and instructions
    public static void displayInstructions() {
        System.out.println(TitleScreen.readTXT("Welcome_Grad_Day"));
        System.out.println(TitleScreen.readTXT("Instructions"));

    }

    public static String readTXT(String name) {
        try {
            banner = Files.readString(Path.of("Banner/" + name + ".txt"));
        } catch (IOException e) {

        }
        return banner;
    }

}