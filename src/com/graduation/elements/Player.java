package com.graduation.elements;

import com.graduation.pointsystem.PointSystem;
import com.graduation.utils.Grade;
import com.graduation.utils.Prompter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    private static Grade grade;
    private static String name;
    private static double credit;
    private static String location;
    private static int health;
    private static List<String> inventory = new ArrayList<>();
    private static List<String> subjectTaken = new ArrayList<>();

    public int getTotalSubject() {
        return totalSubject;
    }

    public void setTotalSubject(int totalSubject) {
        this.totalSubject = totalSubject;
    }

    private int totalSubject = 0;

    public static List<String> getSubjectTaken() {
        return subjectTaken;
    }

    public static void setSubjectTaken(List<String> newSubjectTaken) {
        subjectTaken = newSubjectTaken;
    }

    public Player(String name, int credit, int health, Grade grade, String location) {
        this.name = name;
        this.credit = credit;
        this.health = health;
        this.grade = grade;
        this.location = location;
        this.inventory = inventory;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public void setInventory(List<String> inventory) {
        this.inventory = inventory;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static int setHealth(int health) {
        return Player.health = health;
    }

    public static int getHealth() {
        return health;
    }

    public static Grade getGrade() {
        return grade;
    }

    public static String getLocation() {
        return location;
    }

    public static double getCredit() {
        return credit;
    }

    public static String getName() {
        return name;
    }

    public static List<String> getInventory() {
        return inventory;
    }

    @Override
    public String toString() {

        return "************    Student's Name: " + getName().toUpperCase() + " | Student HP: " + health + " | Student's CGPA "
                + getCredit() + "    ***********";

    }
}
