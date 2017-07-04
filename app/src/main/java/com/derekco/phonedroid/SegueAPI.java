package com.derekco.phonedroid;

import java.util.Arrays;

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

    public static String getNeutralCommand() {return assembleCommand(SegueAPIConstants.NEUTRAL);}

    public static String getSetSpeedCommand(int speed) {
        return assembleCommandWithParam(SegueAPIConstants.SET_SPEED, speed);

    }

    public static String getJoystickCommand(int angle, int power) {
        int paramArray[] = new int[2];
        paramArray[0] = getLeftMotorSpeed(angle, power);
        paramArray[1] = getRightMotorSpeed(angle, power);

        return assembleCommandWithArrayParam(SegueAPIConstants.JOYSTICK, paramArray);
    }

    public static int getLeftMotorSpeed(int angle, int power) {
        int value = 0;

        // 0-90 degrees: forward to clockwise
        // 91-180 degrees: clockwise to reverse
        // -180 to -90 degrees: reverse to counterclockwise
        // -91 to -1 degrees: counterclockwise to forward
        if (angle >= 0 && angle <= 90) { // forward to clockwise rotation
            value = power; // full forward the whole time
        } else if (angle > 90 && angle <= 180) {
            float ratio = (angle - 135)/-45.0f; // from full forward to zero to full reverse
            value = (int) (power * ratio);
        } else if (angle < 0 && angle >= -90) {
            float ratio = (45 + angle)/45.0f; // from full forward to zero to full reverse
            value = (int) (power * ratio);
        } else if (angle < -90 && angle >= -180) {
            value = power * -1; // full reverse the whole time
        }
        return value;
    }

    public static int getRightMotorSpeed(int angle, int power) {
        int value = 0;

        if (angle >= 0 && angle <= 90) {
            float ratio = (45 - angle)/45.0f; // full forward to zero to full reverse
            value = (int) (power * ratio);
        } else if (angle > 90 && angle <= 180) {
            value = power * -1; // full reverse the whole time
        } else if (angle < 0 && angle >= -90) {
            value = power; //full forward the whole time
        } else if (angle < -90 && angle >= -180) {
            float ratio = (angle + 135)/45.0f;  // forward zero to full reverse
            value = (int) (power * ratio);
        }
        return value;
    }

    public static String assembleCommand(String payload) {
        return "<" + payload + ">";
    }
    public static String assembleCommandWithParam(String payload, int param) {
        return "<" + payload + "," + param + ">";
    }
    public static String assembleCommandWithArrayParam(String payload, int[] params) {
        String paramString = "";
        for (int param : params) {
            paramString +=",";
            paramString += param;
        }
        return "<" + payload + paramString + ">";
    }
}
