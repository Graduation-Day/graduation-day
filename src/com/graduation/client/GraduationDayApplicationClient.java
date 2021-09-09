package com.graduation.client;

import java.util.Scanner;
import com.graduation.utils.Prompter;

public class GraduationDayApplicationClient {
    public static void main(String[] args) {
        System.out.println("\nGRADUATION DAY");
        System.out.println( "Team Members:\n" +
                " Hongyi Qu\n" +
                " Jauric Flowers\n" +
                " Pierre Gober\n" +
                " Stephen Yeboah\n\n");

        TitleScreen.displayInstructions();

        Prompter prompter = new Prompter(new Scanner(System.in));
        GameClient game = new GameClient(prompter);
        game.initialize();
    }
}


