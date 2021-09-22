package com.graduation.client;

import com.graduation.pointsystem.Question;
import com.graduation.utils.ConsoleColor;
import com.graduation.utils.Grade;
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

    //Displays title and instructions
    public static void displayInstructions() throws Exception {

        System.out.println(ConsoleColor.GREEN + TitleScreen.readTXT("Welcome_Grad_Day") + ConsoleColor.RESET);

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