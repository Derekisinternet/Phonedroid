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
        assertEquals(-100, SegueAPI.getRightMotorSpeed(0, 100));
    }

    @Test
    public void Joystick45DegreeTest() {
        assertEquals(100, SegueAPI.getLeftMotorSpeed(45, 100));
        assertEquals(0, SegueAPI.getRightMotorSpeed(45, 100));
    }

    @Test
    public void JoystickNinetyDegreeTest() {
        assertEquals(100, SegueAPI.getLeftMotorSpeed(90, 100));
        assertEquals(100, SegueAPI.getRightMotorSpeed(90, 100));
    }

    @Test
    public void Joystick135DegreeTest() {
        assertEquals(0, SegueAPI.getLeftMotorSpeed(135, 100));
        assertEquals(100, SegueAPI.getRightMotorSpeed(135, 100));
    }

    @Test
    public void Joystick180DegreeTest() {
        assertEquals(-100, SegueAPI.getLeftMotorSpeed(180, 100));
        assertEquals(100, SegueAPI.getRightMotorSpeed(180, 100));
    }

    @Test
    public void Joystick225DegreeTest() {
        assertEquals(-100, SegueAPI.getLeftMotorSpeed(225, 100));
        assertEquals(0, SegueAPI.getRightMotorSpeed(225, 100));
    }

    @Test
    public void Joystick270DegreeTest() {
        assertEquals(-100, SegueAPI.getLeftMotorSpeed(270, 100));
        assertEquals(-100, SegueAPI.getRightMotorSpeed(270, 100));
    }

    @Test
    public void Joystick315DegreeTest() {
        assertEquals(0, SegueAPI.getLeftMotorSpeed(315, 100));
        assertEquals(-100, SegueAPI.getRightMotorSpeed(315, 100));
    }

    @Test
    public void JoystickStringTest() {
        String payload = "^i,100,0\\n";
        assertEquals(payload, SegueAPI.getJoystickCommand(45, 100));
    }
}
