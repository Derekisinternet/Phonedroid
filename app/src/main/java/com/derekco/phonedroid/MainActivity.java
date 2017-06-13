package com.derekco.phonedroid;


import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.derekco.phonedroid.EXAMPLE_MESSAGE";
    public static final String COMMAND_TYPE = "com.derekco.phonedroid.COMMAND_TYPE";
    private final String TAG = "MainActivity";

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ManualControlFragment fragment = new ManualControlFragment();
            transaction.replace(R.id.control_placeholder_fragment, fragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
//        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
//        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
//            case R.id.menu_toggle_log:
//                mLogShown = !mLogShown;
//                ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
//                if (mLogShown) {
//                    output.setDisplayedChild(1);
//                } else {
//                    output.setDisplayedChild(0);
//                }
//                supportInvalidateOptionsMenu();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    // Create a BroadcastReceiver for ACTION_FOUND.
//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                Log.d(TAG, "Discovered device");
//                // Discovery has found a device. Get the BluetoothDevice
//                // object and its info from the Intent.
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                String deviceName = device.getName();
//
//                if (deviceName != null) {
//                    Log.d(TAG, "Device Name: " + deviceName);
//                    Log.d(TAG, String.valueOf(deviceName.equals("ALPHA")));
//                } else {
//                    System.out.println("Problem getting device name.");
//                }
//                // TODO: get rid of hard-code to allow for multiple devices
//                // right now, we're only looking for ALPHA, so . . .
//                if (deviceName.equals("ALPHA")) {
//                    Log.d(TAG, "Starting Bluetooth Connection . . .");
//                    mBluetoothService.connect(device, true);
//                    mConnectedDeviceName = deviceName;
//                }
//            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
//                Log.d(TAG, "Starting Bluetooth Device Discovery.");
//            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                Log.d(TAG, "Bluetooth Discovery Finished");
//            }
//        }
//    };

    /**
     * Handler for communication with the BluetoothService:
    */
    private Handler btHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothConstants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            break;
                        case BluetoothService.STATE_CONNECTING:
//                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
//                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case BluetoothConstants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    break;
                case BluetoothConstants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    break;
                case BluetoothConstants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    TextView header = (TextView) findViewById(R.id.textView);
                    header.setText("Connected to " + mConnectedDeviceName);
                    break;
                case BluetoothConstants.MESSAGE_TOAST:
                    // IDK what to do with this yet, so Log:
                    Log.d(TAG, msg.getData().getString(BluetoothConstants.TOAST));
                    break;
            }
        }
    };

    public void sendMessage(View view) {
        // do something on button press
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        // mBluetoothService.write(message.getBytes());
        editText.setText("");
    }
}
