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

    public static String getJoystickCommand(int angle, int power) {
        int motorL = getLeftMotorSpeed(angle, power);
        int motorR = getRightMotorSpeed(angle, power);
        return assembleCommandWithParam(SegueAPIConstants.JOYSTICK, angle);
    }

    private static int getLeftMotorSpeed(int angle, int power) {
        int value = 0;
        // input sanitization, cause I'm paranoid:
        if (angle > 360) {
            angle = angle - 360;
        } else if (angle < -360) {
            angle = angle + 360;
        }

        if (angle >= 0 && angle <= 90) {
            value = power;
        } else if ( angle > 90 && angle <= 135) {
            float ratio = (135 - angle)/45; // from full forward to zero
            value = (int) (power * ratio);
        } else if (angle > 135 && angle <= 180) {
            float ratio = (angle -135)/-45; // from zero to full reverse
            value = (int) (power * ratio);
        } else if (angle > 180 && angle <= 270) {
            value = power * -1;
        } else if (angle > 270 && angle <= 315) {
            float ratio = (270 - angle)/-270; // full reverse to zero
            value = (int) (power * ratio);
        } else if (angle > 270 && angle < 360) {
            float ratio = (angle - 270)/45; // zero to full forward
        }
        return value;
    }

    private static int getRightMotorSpeed(int angle, int power) {
        int value = 0;
        // input sanitization, cause I'm paranoid:
        if (angle > 360) {
            angle = angle - 360;
        } else if (angle < -360) {
            angle = angle + 360;
        }
        if (angle >= 0 && angle <=45) {
            float ratio = (angle - 45)/45; // full reverse to zero
            value = (int) (power * ratio);
        } else if (angle > 45 && angle <= 180) {
            value = power; // full forward
        } else if (angle > 180 && angle <= 225) {
            float ratio =  (225 - angle)/45; //full forward to zero
            value = (int) (power * ratio);
        } else if (angle > 225 && angle <= 270) {
            float ratio = (angle - 225)/-45;  // zero to full reverse
            value = (int) (power * ratio);
        } else if (angle > 270 && angle < 360) {
            value = power * -1;
        }
        return value;
    }

    public static String assembleCommand(String payload) {
        return "<" + payload + ">";
    }
    public static String assembleCommandWithParam(String payload, int param) {
        return "<" + payload + param + ">";
    }
}
