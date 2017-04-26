package com.derekco.phonedroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.os.ParcelUuid;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BluetoothDiscoveryActivity extends AppCompatActivity {
    private int REQUEST_ENABLE_BT = 1;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//    if (mBluetoothAdapter == null) {
//        // device doesn't support bluetooth. Act accordingly.
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_discovery);

        // if user hasn't enabled bluetooth on their phone.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // right now, we're only looking for ALPHA, so . . .
                String deviceName = device.getName();
                if (deviceName == "ALPHA") {
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                    mBluetoothAdapter.cancelDiscovery();
                    startConnection(device);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // just in case . . .
        mBluetoothAdapter.cancelDiscovery();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }

    public void discover(View view) {
//        ListView results;
        // if device is already trying to discover, knock it off.
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    public void startConnection(BluetoothDevice device) {
        ParcelUuid[] btUUIDList = device.getUuids();
        //just get the device's first UUID. I'm not sure if this is right.
        UUID uuid = btUUIDList[0].getUuid();
        try {
            device.createRfcommSocketToServiceRecord(uuid);
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
