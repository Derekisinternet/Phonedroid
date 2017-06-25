package com.derekco.phonedroid;


import android.content.Context;
import android.support.v4.app.FragmentActivity;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by Mastermind on 6/22/17.
 */

public class JoystickViewTest {

    FragmentActivity context;
    JoystickView joystick;

    JoystickView.OnMoveListener listener = new JoystickView.OnMoveListener() {
        @Override
        public void onMove(int angle, int power, int direction) {
            System.out.println("New Angle: " + angle);
            System.out.println("New Power: " + power);
        }
    };

    @Before
    public void initialize() {
        context = new FragmentActivity();
        joystick = new JoystickView(context, listener);

    }

    @Test
    public void testOnMoveListenerSet() {
        assertTrue(joystick.listenerNotNull());
    }

    @Test
    public void testOnMoveEvent() {

    }
}
