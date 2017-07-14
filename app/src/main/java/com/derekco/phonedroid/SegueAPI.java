package com.derekco.phonedroid;

/**
 * Created by Mastermind on 6/3/17.
 */

public class SegueAPI {

    public static String getForwardCommand() {
        return assembleCommand(SegueAPIConstants.FORWARD);
    }

    public static String getBrakeCommand() {
        return assembleCommand(SegueAPIConstants.BRAKE);
    }

    public static String getReverseCommand() {
        return assembleCommand(SegueAPIConstants.REVERSE);
    }

    public static String getClockwiseCommand() {
        return assembleCommand(SegueAPIConstants.CLOCKWISE);
    }

    public static String getCounterClockwiseCommand() {
        return assembleCommand(SegueAPIConstants.COUNTERCLOCKWISE);
    }

//    public static String getHeadingAdjustmentCommand(int param) {
//
//    }

    public static String getSetSpeedCommand(int speed) {
        return assembleCommandWithParam(SegueAPIConstants.SET_SPEED, speed);

    }

    public static String assembleCommand(String payload) {
        return "<" + payload + ">";
    }
    public static String assembleCommandWithParam(String payload, int param) {
        return "<" + payload + param + ">";
    }
}
