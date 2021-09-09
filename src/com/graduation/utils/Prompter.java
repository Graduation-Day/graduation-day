package com.graduation.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.client.GameClient;
import com.graduation.elements.Player;
import com.graduation.pointsystem.PointSystem;
import com.graduation.pointsystem.Question;
import org.jsoup.Jsoup;
import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * <p>
 * General-purpose prompter mechanism that delegates to a {@code Scanner}, which
 * must be provided in the constructor.  The {@code Scanner} passed to the constructor
 * must be initialized by the client, and allows for console-based or file-based input.
 * </p>
 *
 * <p>
 * Console-based input would typically be used for running the actual application.
 * File-based input can be used to automate unit testing without human involvement.
 * </p>
 *
 * <pre>
 * <code>
 *     // To use with a human user at the console:
 *     Prompter prompter = new Prompter(new Scanner(System.in));
 *
 *     // To facilitate automated unit testing without human involvement:
 *     Prompter prompter = new Prompter(new Scanner(new File("responses.txt")));
 * </code>
 * </pre>
 *
 * @author Jay Rostosky
 * @version 1.0
 */
public class Prompter {
    private Scanner scanner;

    /**
     * Creates a {@code Scanner}-based prompter object, that delegates to the {@code Scanner}
     * for all input.  All input is read (and returned) as a single line of text.
     *
     * @param scanner delegate object used by this prompter for reading input.
     */
    public Prompter(Scanner scanner) {
        this.scanner = scanner;
    }

    public static int getRandomNumber(int n) {
        Random rand = new Random();
        return rand.nextInt(n) + 1;
    }

    /**
     * Outputs provided text.  Simply calls {@code System.out.println(info)}.
     *
     * @param info informative text to show the user, not a prompt message.
     * @return the provided text - most applications will ignore this return value.
     */
    public String info(String info) {
        System.out.println(info);
        return info;
    }

    /**
     * Prompts for input, and returns the line of text entered, as a string.
     *
     * @param promptText prompt message.
     * @return the line of text that was input, as a string.
     */
    public String prompt(String promptText, String init) {
        System.out.print(ConsoleColor.YELLOW_BOLD + promptText + ConsoleColor.RESET);
        String response = scanner.nextLine();
        return response;
    }

    public String prompt(String promptText) {
        String response;
        while (true) {
            System.out.print(ConsoleColor.YELLOW_BOLD + promptText + ConsoleColor.RESET);
            response = scanner.nextLine().toLowerCase();
            if (response.matches("s")) {
                //add function to show player status
                System.out.println(GameClient.getPlayer().getGrade().toString());
                System.out.println(readMap.convertedMap());
                String subjectList = "Subjects Token: ";
                for (String subject : Player.getSubjectTaken()) {
                    subjectList += subject + "; ";
                }
                System.out.println(subjectList);
                System.out.println(" ");
                //blank line
                //give player a helpful message

                //display the current question to remind the user to answer it
                if (Question.getCurrentQuestion() != null) {
                    System.out.println(Jsoup.parse(Question.getCurrentQuestion().getQuestion()).text());
                    for (Map.Entry<Character, String> options : Question.getCurrentAnswer().entrySet()) {
                        System.out.println(options.getKey() + ") " + options.getValue());
                    }
                }
            } else if (response.matches("h")) {
                System.out.println(
                        "Use the following actions:" +
                                "GO [north, south, east, west, up, down]\n" +
                                "GET/USE [item]\n" +
                                "Look");
                if (Question.getCurrentQuestion().getQuestion() != null) {
                    System.out.println(Jsoup.parse(Question.getCurrentQuestion().getQuestion()).text());
                    for (Map.Entry<Character, String> options : Question.getCurrentAnswer().entrySet()) {
                        System.out.println(options.getKey() + ") " + options.getValue());
                    }
                }
                //blank line
                System.out.println();
                //quit the game by inputting Q/q
            } else if (response.matches("q")) {
                System.out.println("Do you want to save before exiting? (yes/no)");
                response = scanner.nextLine().trim().toLowerCase();
                if (response.matches("yes|y")) {
                    saveCurrentState();
                }
                System.exit(0);

            } else if (response.matches("cheat")) {
                //if random integer between 1-10 is even then the user will get the question wrong
                if (((getRandomNumber(10) % 2) == 0)) {
                    System.out.println("You have been caught and your answer is incorrect.");
                    Question.cheatCounter++;

                } else {
                    System.out.println(Question.getCurrentQuestion().getCorrect_answer());
                }
                //hacking a room
            } else if (response.matches("hack")) {
                //get the current room
                hackClass();
                return "quit";
            } else if (response.matches("quit")) {
                //get the current room
                return "quit";
            } else {
                return response;
            }
        }
    }

    private void hackClass() {
        String currentLocation = PointSystem.currentPlayer.getLocation().toLowerCase();
        //check if the current room is not a non-subject room
        if (!PointSystem.getNotSubject().contains(currentLocation)) {
            //check if the list of subject taken contains the current room
            if (PointSystem.currentPlayer.getSubjectTaken().contains(currentLocation)) {
                System.out.println("You have already taken " + currentLocation);
            } else {
                PointSystem.currentPlayer.getSubjectTaken().add(currentLocation);
                //default 2.4 GPA if you hack
                PointSystem.currentPlayer.setCredit(new PointSystem().getCumulativeScore(3, PointSystem.currentPlayer.getSubjectTaken().size()));
                PointSystem.changePlayerGrade(PointSystem.currentPlayer);
            }


        }
    }

    public static void clearScreen() {
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder process = (os.contains("windows")) ?
                new ProcessBuilder("cmd", "/c", "cls") :
                new ProcessBuilder("clear");
        try {
            process.inheritIO().start().waitFor();
        } catch (InterruptedException | IOException ignored) {
            System.out.println(ignored);
        }
    }

    private void saveCurrentState() {
        ObjectMapper save = new ObjectMapper();
        try {
            save.writeValue(new File("storage.txt"), save.writeValueAsString(PointSystem.currentPlayer));
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Prompts for input, validates it against the regex pattern provided, and returns
     * the line of text entered, as a string.  If the input does not match the pattern,
     * the error message text is displayed, and then the prompt text is displayed again.
     * The input is validated against the regex pattern via {@code String.matches()}.
     * </p>
     *
     * <p>
     * The prompt text and error message text are output verbatim, exactly as provided.
     * To add a blank line or two between the user input line, the error message line,
     * and the follow-on user prompt, just include {@code "\n"} in the {@code retryText} value,
     * as appropriate.  For example:
     * </p>
     *
     * <pre>
     * <code>
     *     prompter.prompt("Please enter your age: ", "\\d+", "\nThat is not a valid age!\n");
     * </code>
     * </pre>
     *
     * @param promptText prompt message.
     * @param pattern    regex pattern, used to validate the input string.
     * @param retryText  error message displayed when input string does not match regex pattern.
     * @return the line of text that was input, as a string.
     */


}