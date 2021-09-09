package com.graduation.pointsystem;

import com.graduation.utils.Grade;
import com.graduation.utils.Prompter;
import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.*;

public class QuestionTest {

    @Test
    public void testGenerateQuestions() {
        assertEquals(-1,new Question().generateQuestions("", Grade.FRESHMAN));

    }
}