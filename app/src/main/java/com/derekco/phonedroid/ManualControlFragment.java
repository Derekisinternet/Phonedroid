package com.derekco.phonedroid;


import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * Created by Mastermind on 5/29/17.
 */

public class ManualControlFragment extends Fragment {
    private static final String TAG = "MANUAL_CONTROL_FRAG";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Controller Interface:
//    private Button mForwardButton;
//    private Button mStopButton;
//    private Button mReverseButton;
//    private Button mClockwiseButton;
//    private Button mCounterClockwiseButton;
    private SeekBar mSpeedBar;

    private JoystickView joystick;

    // Local Bluetooth Adapter:
    private BluetoothAdapter mBluetoothAdapter = null;

    private String mConnectedDeviceName = null;

    private BluetoothService mBluetoothService = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }

        joystick = new JoystickView(this.getContext());
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                Log.d(TAG, "New Angle: " + angle);
                Log.d(TAG, "New Power: " + power);
                sendJoystickValuesCommand(angle, power);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mBluetoothService == null){
            mBluetoothService = new BluetoothService(getActivity(), mHandler);
//            initControls();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBluetoothService != null) {
            mBluetoothService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        }
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBluetoothService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mBluetoothService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mBluetoothService.start();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manual_control, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        mForwardButton = (Button) view.findViewById(R.id.button_forward);
//        mStopButton = (Button) view.findViewById(R.id.button_stop);
//        mReverseButton = (Button) view.findViewById(R.id.button_reverse);
//        mClockwiseButton = (Button) view.findViewById(R.id.button_right);
//        mCounterClockwiseButton = (Button) view.findViewById(R.id.button_left);
//        mSpeedBar = (SeekBar) view.findViewById(R.id.speedBar);
    }

    private void initControls() {
//        mForwardButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendForwardCommand();
//            }
//        });
//        mStopButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                sendBrakeCommand();
//            }
//        });
//        mReverseButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                sendReverseCommand();
//            }
//        });
//        mClockwiseButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                sendClockwiseCommand();
//            }
//        });
//        mCounterClockwiseButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                sendCounterClockwiseCommand();
//            }
//        });
        mSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sendMessage(SegueAPI.getSetSpeedCommand(progress));
                Log.d(TAG, "Setting speed to " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * reference:
     * 0 degrees - (1,0)
     * 90 degrees - (0,1)
     * 180 degrees - (-1, 0)
     */
    public void sendJoystickValuesCommand(int angle, int power) {
        sendMessage(SegueAPI.getJoystickCommand(angle, power));
        Log.d(TAG, "joystick reading: " + angle + " degrees at " + power + " power");
    }

    public void sendForwardCommand() {
        sendMessage(SegueAPI.getForwardCommand());
        Log.d(TAG, "Forward Button Press: " + SegueAPI.getForwardCommand());
    }

    public void sendBrakeCommand() {
        Log.d(TAG, "Brake Button Press!");
        sendMessage(SegueAPI.getBrakeCommand());
    }

    public void sendReverseCommand() {
        Log.d(TAG, "Reverse Button Press!");
        sendMessage(SegueAPI.getReverseCommand());
    }

    public void sendClockwiseCommand() {
        Log.d(TAG, "Clockwise Button Press!");
        sendMessage(SegueAPI.getClockwiseCommand());
    }

    public void sendCounterClockwiseCommand() {
        Log.d(TAG, "CounterClockwise Button Press!");
        sendMessage(SegueAPI.getCounterClockwiseCommand());
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mBluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBluetoothService.write(send);
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case BluetoothConstants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            setStatus("Connecting to " + mConnectedDeviceName);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            setStatus("Connecting . . .");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            setStatus("Not Connected");
                            break;
                    }
                    break;
                case BluetoothConstants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //TODO: do something with the value. . . .
                    break;
                case BluetoothConstants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //TODO: do something with the value . . .
                    break;
                case BluetoothConstants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(BluetoothConstants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case BluetoothConstants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(BluetoothConstants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    //TODO: something?

                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBluetoothService.connect(device, secure);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bluetooth_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.disconnect: {
                disconnectDevice();
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }

    private void disconnectDevice() {
        if (mBluetoothService.getState() == BluetoothAdapter.STATE_CONNECTED) {
            mBluetoothService.stop();
            Log.d(TAG, "stopping connection.");
        }
    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }
}
