package com.dashwood.ficby;

import android.*;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;

import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhaoxiaodan.miband.MiBand;

import java.util.ArrayList;
import java.util.HashMap;

import static com.dashwood.ficby.MainActivity.CIRCULAR_BOOK;
import static com.dashwood.ficby.MainActivity.SOFT_MEDIUM;

public class ScanBTActivity extends AppCompatActivity {

    ImageView bt_click;
    TextView scanned_text, device_text;
    Typeface typeface_zh_medium, typeface_book;
    //MiBand miBand;

    //private ArrayList<String> mDeviceList = new ArrayList<String>();

    //HashMap<String, BluetoothDevice> devices = new HashMap<String, BluetoothDevice>();
    static int pic_click = 0;

    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    String target = "MI1S";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bt);


        typeface_zh_medium = Typeface.createFromAsset(getAssets(), SOFT_MEDIUM);
        typeface_book = Typeface.createFromAsset(getAssets(), CIRCULAR_BOOK);
        bt_click = (ImageView) findViewById(R.id.scan_bt_pic);
        scanned_text = (TextView) findViewById(R.id.scan_text);
        scanned_text.setTypeface(typeface_zh_medium);
        device_text = (TextView) findViewById(R.id.bt_device_text);
        device_text.setTypeface(typeface_book);

        /*bt_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/


        btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();

        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }
        startScanning();

        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect peripherals.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                        }
                    }
                });
                builder.show();
            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            device_text.append("Device Name: " + result.getDevice().getName() + " rssi: " + result.getRssi() + "\n");

            // auto scroll for text view
            final int scrollAmount = device_text.getLayout().getLineTop(device_text.getLineCount()) - device_text.getHeight();
            // if there is no need to scroll, scrollAmount will be <=
            if (scrollAmount > 0)
                device_text.scrollTo(0, scrollAmount);
        }
    };

    public void startScanning() {

        device_text.setText("");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.startScan(leScanCallback);
            }
        });
        new CountDownTimer(10000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                stopScanning();
            }
        }.start();
    }

    public void stopScanning() {

        device_text.append("Band Founded!");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.stopScan(leScanCallback);
                BandActivity.bind_band_result = 1;
                finish();
            }
        });
    }
}
//final ArrayAdapter bt_device_adapter = new ArrayAdapter<String>(this, R.layout.bt_device_item);

        /*bt_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pic_click == 0)
                {
                    MiBand.startScan(scanCallback);
                    pic_click = 1;
                }

                if (pic_click == 1)
                {
                    MiBand.stopScan(scanCallback);
                    pic_click = 0;
                }

            }
        });


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);


        //bt_device_list.setAdapter(bt_device_adapter);
                        /*bt_device_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String item_list = ((TextView) view).getText().toString();
                                if (devices.containsKey(item_list))
                                {
                                    //MiBand.stopScan(scanCallback);

                                    /*BluetoothDevice device = devices.get(item_list);
                                    Intent backtoBand = new Intent(ScanBTActivity.this, BandActivity.class);
                                    backtoBand.putExtra("miband", device);
                                    startActivity(backtoBand);
                                    finish();
                                }
                            }
                        }); */

        /*scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                BluetoothDevice bluetoothDevice = result.getDevice();

                String device_item = bluetoothDevice.getName() + " | " + bluetoothDevice.getAddress();
                if (!devices.containsKey(devices)) {
                    devices.put(device_item, bluetoothDevice);
                }
            }
        };*/

    /*private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDeviceList.add(device.getName() + "\n" + device.getAddress());
                Log.i("BT", device.getName() + "\n" + device.getAddress());
                bt_device_list.setAdapter(new ArrayAdapter<String>(context,
                        R.layout.list_item, mDeviceList));
                bt_device_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        unregisterReceiver(mReceiver);
                        finish();
                    }
                });
            }
        }
    };*/







