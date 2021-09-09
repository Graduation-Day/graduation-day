package com.graduation.client;

import com.indvd00m.ascii.render.Render;
import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.api.IContextBuilder;
import com.indvd00m.ascii.render.api.IRender;
import com.indvd00m.ascii.render.elements.PseudoText;

import java.awt.*;
import java.awt.image.BufferedImage;
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


    String help = TitleScreen.readTXT("help");


    public void inputOptions(String keystroke) {
        //We will use this later during the game as an option to bring
        System.out.println("Type 'Q' to quit the game." +
                "Type 'Z' to look at the credits. Type 'H' to look at the help tutorial");
        switch (keystroke) {
            case "q":
                System.exit(0);
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
    public static void displayInstructions()  {
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