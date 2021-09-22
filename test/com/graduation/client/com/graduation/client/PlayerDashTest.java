
package com.graduation.client;

import com.graduation.client.GameClient;

//import com.graduation.pointsystem.Elective;
import com.graduation.utils.TextFileReader;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerDashTest {

    @Test
    public void tesxtFileNotNullTest() throws Exception {
        TextFileReader tfr = new TextFileReader();
        String actual = tfr.readDashBoard().get(0);
        assertNotNull(actual);
    }

    @Test
    public void textFileEqaulsTest() throws Exception{
        TextFileReader tfr = new TextFileReader();
        String actual = tfr.readDashBoard().get(0);
        System.out.println(actual);
        assertEquals("***********************************************",actual);
    }

    @Test
    public void textFileEqActualTwoTest() throws Exception {
        TextFileReader tfr = new TextFileReader();
        String actual= tfr.readDashBoard().get(1);
        System.out.println(actual);
        assertEquals("**",actual);
    }

    @Test
    public void textFileEqActualThreeTest() throws Exception {
        TextFileReader tfr = new TextFileReader();
        String actual = tfr.readDashBoard().get(2);
        System.out.println(actual);
        assertEquals("ROOM:",actual);
    }

    @Test
    public void textFileEqActualFourTest() throws Exception {
        TextFileReader tfr = new TextFileReader();
        String actual = tfr.readDashBoard().get(3);
        System.out.println(actual);
        assertEquals("|| GRADE LEVEL:",actual);

    }

    @Test
    public void textFileEqActualFiveTest() throws Exception {
        TextFileReader tfr = new TextFileReader();
        String actual = tfr.readDashBoard().get(4);
        System.out.println(actual);
        assertEquals("***********************************************",actual);

    }

    @Test
    public  void textFileReaderCheckForNumbers() throws Exception{
        TextFileReader tfr = new TextFileReader();
        String actual = tfr.readDashBoard().get(0);
        assertNotEquals(23,actual);

    }

//    @Test
//    public void testValidation() throws Exception {
//        Elective e = new Elective("history");
//        e.checkerOfUserInput();
//    }





}
