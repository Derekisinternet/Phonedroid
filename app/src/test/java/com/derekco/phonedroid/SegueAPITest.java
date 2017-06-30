package com.derekco.phonedroid;


import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mastermind on 6/18/17.
 */

public class SegueAPITest {
    @Test
    public void JoystickZeroDegreeTest() {
        assertEquals(100, SegueAPI.getLeftMotorSpeed(0, 100));
        assertEquals(100, SegueAPI.getRightMotorSpeed(0, 100));
    }

    @Test
    public void ZeroTo45Test() {
        System.out.println("Right Motor Speed Ratio\nDegrees: Power");
        for (int i = 0; i < 45; i++) {
            System.out.println(i + ": " + SegueAPI.getRightMotorSpeed(i, 100));
        }
    }

    @Test
    public void ZeroToNegative45Test() {
        System.out.println("Left Motor Speed Ratio\nDegrees: Power");
        for (int i = 0; i > -45; i--) {
            System.out.println(i + ": " + SegueAPI.getLeftMotorSpeed(i, 100));
        }
    }

    @Test
    public void Joystick45DegreeTest() {
        assertEquals(100, SegueAPI.getLeftMotorSpeed(45, 100));
        assertEquals(0, SegueAPI.getRightMotorSpeed(45, 100));
    }

    @Test
    public void JoystickNinetyDegreeTest() {
        assertEquals(100, SegueAPI.getLeftMotorSpeed(90, 100));
        assertEquals(-100, SegueAPI.getRightMotorSpeed(90, 100));
    }

    @Test
    public void Joystick135DegreeTest() {
        assertEquals(0, SegueAPI.getLeftMotorSpeed(135, 100));
        assertEquals(-100, SegueAPI.getRightMotorSpeed(135, 100));
    }

    @Test
    public void Joystick180DegreeTest() {
        assertEquals(-100, SegueAPI.getLeftMotorSpeed(180, 100));
        assertEquals(-100, SegueAPI.getRightMotorSpeed(180, 100));
    }



    @Test
    public void JoystickStringTest() {
        String payload = "^i,100,0\\n";
        assertEquals(payload, SegueAPI.getJoystickCommand(45, 100));
    }
}
