package com.derekco.phonedroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Set;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.derekco.phonedroid.EXAMPLE_MESSAGE";
    public static final String COMMAND_TYPE = "com.derekco.phonedroid.COMMAND_TYPE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void discoverBluetooth(View view) {
        Intent intent = new Intent(this, BluetoothDiscoveryActivity.class);
        startActivity(intent);
    }

    public void sendCommand(View view) {
        Intent intent = new Intent(this, SendCommandActivity.class);
        intent.putExtra(COMMAND_TYPE, "stop");
        startActivity(intent);
    }

    public void sendMessage(View view) {
        // do something on button press
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }
}
