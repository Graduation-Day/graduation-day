package com.graduation.pointsystem;

import com.graduation.client.GameClient;
import com.graduation.elements.Bully;
import com.graduation.elements.Player;
import com.graduation.utils.ConsoleColor;
import com.graduation.utils.Grade;
import com.graduation.utils.Prompter;
import com.graduation.utils.Sound;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class PointSystem {

    private static final List<String> notSubject = new ArrayList<>(Arrays.asList("gym", "cafeteria", "hallway"));
    private static final int GRADE = 4;
    private static double player_total_grade = 0;
    private static final List<String> core = new ArrayList<>(
            Arrays.asList("maths", "computers", "geography", "history"));
    private static boolean isNewLevel = false;
    public static Player currentPlayer = null;
    static Sound sound = new Sound();

    private double getScore(int correct) {
        double current_class = 0;
        current_class += ((correct / (double) 5) * GRADE);
        return current_class;
    }

    public double getCumulativeScore(int eachScore, int numberOfSubjects) {
        player_total_grade += (getScore(eachScore));
        return Double.parseDouble(new DecimalFormat("#.##").format(player_total_grade / (double) numberOfSubjects));
    }

    public static String randomSubject() {
        String[] arrElectives = {"art", "politics", "vehicles", "mythology"};
        Random rand = new Random();
        int randInt = rand.nextInt(4);
        return arrElectives[randInt];
    }

    public static List<String> getNotSubject() {
        return notSubject;
    }

    public static void teacherQuestions(String subject, Grade level, Player player) throws Exception {
        Sound sound = new Sound();
        currentPlayer = player;

        // check if a subject already passed and continue loading questions accordingly
        if (!player.getSubjectTaken().contains(subject)) {

            Question questions = new Question();
            if (player.getSubjectTaken().size() == 0) {
                isNewLevel = false;
            }
            sound.playSoundClip("Sounds/gotcha.wav");
            PointSystem pointSystem = new PointSystem();
            // initialize classroom score and class fail counter before starting question
            int score = 0;
            int classFailCount = 0;

            if (!notSubject.contains(subject.toLowerCase())) {
                score = questions.generateQuestions(subject, level);
                if (score == -1) {
                    GameClient.continueJourney(isNewLevel);
                } else {
                    while (pointSystem.getScore(score) < 2) {
                        classFailCount++;
                        if (classFailCount == 3) {
                            System.out.println(ConsoleColor.RED_BOLD + "\n\n            You have failed this class 3 "
                                    + "times and can never graduate. You can't tell your parents, they'll kill you. You will need to re-apply for the school and "
                                    + "re-start the game." + ConsoleColor.RESET);
                            sound.playSoundClip("Sounds/youdied.wav");
                            Thread.sleep(5000);
                            System.exit(0);
                        }
                        System.out.println(ConsoleColor.RED + "\n\n                                               "
                                + "Your class GPA of " + pointSystem.getScore(score) + " is less than 2.0, you need to take " + subject + " again."
                                + ConsoleColor.RESET);
                        sound.playSoundClip("Sounds/ohhhno.wav");
                        System.out.println();
                        score = questions.generateQuestions(subject, level);
                    }
                    // add the passed subject to the list of taken subject
                    player.getSubjectTaken().add(subject);
                    // set the player's GPA
                    player.setCredit(pointSystem.getCumulativeScore(score, player.getSubjectTaken().size()));
                    sound.playSoundClip("Sounds/tada.wav");
                    System.out.println(player.getCredit());

                    // determine if the player has meet the criteria to change its level
                    // from freshman->sophomore->junior->senior
                    // based on a gpa greater than or equal to 2.0 and having taken all the core
                    // subjects i.e. maths,computers,history and geography
                    // reset the taken subject list
                    changePlayerGrade(player);
                }
            }
        } else {
            System.out.println("You have already passed " + subject);
        }
        GameClient.continueJourney(isNewLevel);
    }

    public static void promptElectives() throws Exception {
        boolean run = true;
        while (run) {
            System.out.println(ConsoleColor.GREEN + "Your guidance counselor admires your achievements but notices you skipped on electives all year. He wont let you pass until you take one" + ConsoleColor.RESET);
            System.out.println(ConsoleColor.GREEN + "Please choose an elective from the following Mythology, Art, Vehicles, Politics" + ConsoleColor.RESET);
            String userChoice = GameClient.getPrompter().prompt(":> ").trim().toUpperCase();
            Question questions = new Question();
            if (userChoice.matches("ART")) {
                questions.generateQuestions(userChoice, Grade.SOPHOMORE);
                run = false;
            }
            if (userChoice.matches("MYTHOLOGY")) {
                questions.generateQuestions(userChoice, Grade.SOPHOMORE);
                run = false;
            }
            if (userChoice.matches("VEHICLES")) {
                questions.generateQuestions(userChoice, Grade.SOPHOMORE);
                run = false;
            }
            if (userChoice.matches("POLITICS")) {
                questions.generateQuestions(userChoice, Grade.SOPHOMORE);
                run = false;
            }
            System.out.println("wrong answer please type art, vehicles, politics, mythology");
            Prompter.clearScreen();
        }
    }

    public static void changePlayerGrade(Player player) throws Exception {
        // Step 1: Determine if we can go to the next grade level and it asks the elective

        if (player.getSubjectTaken().containsAll(core) && player.getCredit() >= 2.0) {
            Prompter.clearScreen();
            promptElectives();
            Prompter.clearScreen();
            // display a congratulation message on moving to the next grade
            System.out.println(ConsoleColor.GREEN + "\n\n            CONGRATULATIONS!!!!, you've passed "
                    + player.getGrade() + " level." + ConsoleColor.RESET);
            sound.playSoundClip("Sounds/thatwaseasy.wav");
            Thread.sleep(2000);
            isNewLevel = true;
            switch (player.getGrade()) {
                case FRESHMAN:
                    player.setGrade(Grade.SOPHOMORE);
                    break;
                case SOPHOMORE:
                    player.setGrade(Grade.JUNIOR);
                    break;
                case JUNIOR:
                    player.setGrade(Grade.SENIOR);
                    break;
                case SENIOR:
                    System.out.println(ConsoleColor.GREEN + "\n\n            You have successfully graduated. " +
                            "\n            Your GRADUATION DAY IS FINALLY HERE. GOOD LUCK with your future endeavors."
                            + ConsoleColor.RESET);
                    sound.playSoundClip("Sounds/numberone.wav");
                    Thread.sleep(15000);
                    System.exit(0);
            }

            // Step 2: Clear the subjects that we passed from the player
            player.getSubjectTaken().clear();
            // Step 3: Get the first location of the next level
            player.setLocation(GameClient.getFirstLocation());
            // reset the GPA for the new level to zero

            player.setCredit(0);
            player_total_grade = 0;

            // Step 4: Toggle the bully
            Bully.setPresence(true);
            Bully.setHealth(100);
        }

    }
}
