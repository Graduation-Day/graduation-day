package com.graduation.pointsystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.client.GameClient;
import com.graduation.utils.*;
import org.jsoup.Jsoup;


import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Question {
    public static final Map<String, Integer> categories = Map.of("maths", 19, "history", 23, "geography", 22, "sports",
            21, "general knowledge", 9, "computers", 18, "mythology", 20, "art", 25, "politics",24,"vehicles", 28);
    private static final Map<Grade, String> difficulties = Map.of(Grade.FRESHMAN, "easy", Grade.SOPHOMORE, "easy",
            Grade.JUNIOR, "medium", Grade.SENIOR, "hard");
    public static int cheatCounter = 0;
    private static QuestionDetail currentQuestion = null;
    private static Map<Character, String> currentAnswer = null;
    public static boolean isMuted = false;

    public static QuestionDetail getCurrentQuestion() {
        return currentQuestion;
    }

    public static Map<Character, String> getCurrentAnswer() {
        return currentAnswer;
    }


    public List<QuestionDetail> getQuestions(String type, Grade grade)
            throws JsonProcessingException, ExecutionException, InterruptedException {
        // make a client object
        HttpClient client = HttpClient.newHttpClient();
        // create a request object
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://opentdb.com/api.php?amount=5&category="
                + categories.get(type.toLowerCase()) + "&difficulty=" + difficulties.get(grade))).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());
        String text = response.thenApply(HttpResponse::body).join();
        ObjectMapper questions = new ObjectMapper();
        // mapping the rest api json to POJO QuestionTemplate class
        QuestionTemplate category = questions.readValue(text, QuestionTemplate.class);
        List<QuestionDetail> lists = category.getResults();
        // return the question details as a list
        return lists;
    }

    public int generateQuestions(String type, Grade level)
            throws Exception {
        Sound questionSound = new Sound();

        System.out.println(readMap.convertedMap());
        System.out.println(ConsoleColor.GREEN_BOLD
                + "                                   *******************************************************************************************");
        System.out.println("                                   " + GameClient.getPlayer());
        System.out.println(
                "                                   ******************************************************************************************* "
                        + ConsoleColor.RESET);

        // Pause the execution for 3 sec
        Thread.sleep(3000);

        List<QuestionDetail> samples = null;
        try {
            samples = getQuestions(type, level);
        } catch (ExecutionException | JsonProcessingException | InterruptedException ex) {
            ex.printStackTrace();
        }
        int counter = 0;
        // loop through the questions
        for (QuestionDetail sample : samples) {
            // assign the current question to currentQuestion class variable
            currentQuestion = sample;
            cheatCounter = 0;
            Map<Character, String> possible_answers = new LinkedHashMap<>();
            System.out.println(ConsoleColor.YELLOW + Jsoup.parse(sample.getQuestion()).text() + ConsoleColor.RESET);
            List<String> answers = new ArrayList<>();
            answers.add(sample.getCorrect_answer());
            for (Object incorrect : sample.getIncorrect_answers()) {
                answers.add(incorrect.toString());

            }
            // randomize the possible answers
            Collections.shuffle(answers);
            char option = 'A';

            // sorting True or False answers to display in consistent order
            if (currentQuestion.getType().equalsIgnoreCase("boolean")) {
                Collections.sort(answers, Collections.reverseOrder());
            }

            for (String possible_answer : answers) {
                // stripping the answer of any html tags
                possible_answers.put(option++, Jsoup.parse(possible_answer).text());
            }
            // assign the current set of answers to the class variable currentAnswer
            currentAnswer = possible_answers;

            for (Map.Entry<Character, String> options : possible_answers.entrySet()) {
                System.out.println(options.getKey() + ") " + options.getValue());
            }

            // read questions from TSS
            TextToSpeech.speak(Jsoup.parse(sample.getQuestion()).text());

            // get user response
            String userChoice = GameClient.getPrompter().prompt(":> ").trim().toUpperCase();
            if (userChoice.matches("QUIT")) {
                return -1;
            }

            char chosen = ' ';
            // while user response does not meet certain criteria, keep asking
            while (userChoice.compareTo("") == 0
                    || !possible_answers.keySet().contains(userChoice.toUpperCase().charAt(0))) {
                System.out.println("You can choose from these options: "
                        + Arrays.toString(possible_answers.keySet().toArray(new Character[0])));
                userChoice = GameClient.getPrompter().prompt(":> ").trim().toUpperCase();
                if (userChoice.matches("QUIT")) {
                    return -1;
                }
            }
            chosen = userChoice.charAt(0);
            if (possible_answers.get(chosen).compareTo(Jsoup.parse(sample.getCorrect_answer()).text()) == 0) {
                questionSound.playSoundClip("Sounds/cheer.wav");
                counter += 1;
                System.out.println(ConsoleColor.GREEN + "Correct. Nice Work!!!" + ConsoleColor.RESET
                        + "\nYour score: " + counter + "/5.\n");
                Thread.sleep(3500);
            } else {
                questionSound.playSoundClip("Sounds/boohiss.wav");
                System.out.println(ConsoleColor.RED + "Wrong : The correct answer is " + sample.getCorrect_answer()
                        + "." + ConsoleColor.RESET + "\nYour score: " + counter + "/5.\n");
                Thread.sleep(3500);
            }
            counter = counter - cheatCounter;

        }
        Prompter.clearScreen();
        return counter;
    }

}
