package com.derekco.phonedroid;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.ParcelUuid;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothDiscoveryActivity extends AppCompatActivity {
    private int REQUEST_ENABLE_BT = 1;
    private ListView deviceListView;

    private ProgressDialog mDiscoveryDialogue;
    private BluetoothAdapter mBluetoothAdapter;
//    if (mBluetoothAdapter == null) {
//        // device doesn't support bluetooth. Act accordingly.
//    }
    ArrayList<String> discoveryArray = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_discovery);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, discoveryArray);

        deviceListView = (ListView) findViewById(R.id.deviceListView);
//        listView.setAdapter(adapter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // if user hasn't enabled bluetooth on their phone.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Create a dialogue box for Bluetooth Device Discovery:
        mDiscoveryDialogue = new ProgressDialog(this);
        mDiscoveryDialogue.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDiscoveryDialogue.setMessage("Scanning for Devices . . . ");
        mDiscoveryDialogue.setCancelable(false);
        mDiscoveryDialogue.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mBluetoothAdapter.cancelDiscovery();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        // just in case . . .
        mBluetoothAdapter.cancelDiscovery();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }

    public void discover(View view) {
        // if device is already trying to discover, knock it off.
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }



        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);

        //Thread discoThread = new Thread()
        mBluetoothAdapter.startDiscovery();
        Log.d("BluetoothClass", "starting discovery . . .");
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d("BluetoothClass", "Discovered device");
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();

                if (deviceName != null) {
                    Log.d("BluetoothClass", "Device Name: " + deviceName);
                    Log.d("BluetoothClass", String.valueOf(deviceName.equals("ALPHA")));
                } else {
                    System.out.println("Problem getting device name.");
                }
                // TODO: get rid of hard-code to allow for multiple devices
                // right now, we're only looking for ALPHA, so . . .
                if (deviceName.equals("ALPHA")) {
                    Log.d("BluetoothClass", "Starting Connection . . .");
                    startConnection(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDiscoveryDialogue.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mDiscoveryDialogue.dismiss();
            }
        }
    };

    public void startConnection(BluetoothDevice device) {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        mDiscoveryDialogue.setMessage("Connecting to "+ device.getName());

        ParcelUuid[] btUUIDList = device.getUuids();
        //just get the device's first UUID. I'm not sure if this is right.
        UUID uuid = btUUIDList[0].getUuid();
        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            if (socket.isConnected()) {
                Log.d("BluetoothClass", "Connected to " + device.getName());
            }
            //TODO: figure out best practices for response codes . . .
            finish();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void queryPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    }
}
