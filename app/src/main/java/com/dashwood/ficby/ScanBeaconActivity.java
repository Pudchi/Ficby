package com.dashwood.ficby;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.dashwood.ficby.MainActivity.CIRCULAR_BOOK;

public class ScanBeaconActivity extends AppCompatActivity {

    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    TextView device_text, scan_status;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    static int scan_btn_flag = 0;
    ImageView bt_pic_btn, beacon_blue, beacon_green, beacon_purple;
    static String beacon_info = "";
    Typeface typeface_book;
    String mac = "";

    private static final long SCAN_PERIOD = 6000; //6 seconds
    Handler btHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_beacon);

        typeface_book = Typeface.createFromAsset(getAssets(), CIRCULAR_BOOK);

        btHandler = new Handler();

        scan_status = (TextView) findViewById(R.id.scan_text);
        device_text = (TextView) findViewById(R.id.ble_text);
        bt_pic_btn = (ImageView) findViewById(R.id.scan_bt_pic);
        beacon_blue = (ImageView) findViewById(R.id.blue_beacon);
        beacon_green = (ImageView) findViewById(R.id.green_beacon);
        beacon_purple = (ImageView) findViewById(R.id.purple_beacon);
        device_text.setMovementMethod(new ScrollingMovementMethod());

        bt_pic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanLeDevice(true);
                /*if (scan_btn_flag == 0)
                {
                    scan_status.setText("開始掃描");
                    scan_btn_flag = 1;
                    startScanning();
                }
                else if (scan_btn_flag == 1)
                {
                    scan_status.setText("停止掃描");
                    scan_btn_flag = 0;
                    stopScanning();
                }*/

            }
        });


        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();


        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }


        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
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
                    System.out.println("Coarse location permission granted");
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

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            btHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btAdapter.stopLeScan(mleScanCallback);
                }
            }, SCAN_PERIOD);

            btAdapter.startLeScan(mleScanCallback);
        } else {
            btAdapter.stopLeScan(mleScanCallback);
        }

    }

    private BluetoothAdapter.LeScanCallback mleScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            Log.d("TAG","BLE device : " + device.getName());
            int startByte = 2;
            boolean patternFound = false;
            // 尋找ibeacon
            // 先依序尋找第2到第8陣列的元素
            while (startByte <= 5) {
                // Identifies an iBeacon
                if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 &&
                        // Identifies correct data length
                        ((int) scanRecord[startByte + 3] & 0xff) == 0x15)
                {
                    patternFound = true;
                    break;
                }
                startByte++;
            }

            // 如果找到了的话
            if (patternFound) {
                btAdapter.stopLeScan(mleScanCallback);
                // 轉換16進制
                byte[] uuidBytes = new byte[16];
                // 來源、起始位置
                System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                String hexString = bytesToHex(uuidBytes);

                // UUID
                String uuid = hexString.substring(0, 8) + "-"
                        + hexString.substring(8, 12) + "-"
                        + hexString.substring(12, 16) + "-"
                        + hexString.substring(16, 20) + "-"
                        + hexString.substring(20, 32);

                // Major
                int major = (scanRecord[startByte + 20] & 0xff) * 0x100
                        + (scanRecord[startByte + 21] & 0xff);

                // Minor
                int minor = (scanRecord[startByte + 22] & 0xff) * 0x100
                        + (scanRecord[startByte + 23] & 0xff);

                mac = device.getAddress();
                // txPower
                int txPower = (scanRecord[startByte + 24]);
                //double distance = calculateAccuracy(txPower,rssi);


                 beacon_info = "Mac：" + mac
                         + "\nUUID：" + uuid + "\nMajor：" + major + "\nMinor："
                         + minor + "\nTxPower：" + txPower + "\nRSSI：" + rssi + "\n\nDistance："+calculateAccuracy(txPower,rssi);


                device_text.setText(beacon_info);
                device_text.setTypeface(typeface_book);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mac.equals("C7:24:EE:DB:FF:59"))  //blue
                        {
                            beacon_blue.setVisibility(View.VISIBLE);
                            beacon_green.setVisibility(View.GONE);
                            beacon_purple.setVisibility(View.GONE);
                        }

                        if (mac.equals("FA:AD:9D:BC:5B:BB"))  //green
                        {
                            beacon_blue.setVisibility(View.GONE);
                            beacon_green.setVisibility(View.VISIBLE);
                            beacon_purple.setVisibility(View.GONE);
                        }

                        if (mac.equals("C5:C1:C3:5C:5F:31"))  //purple
                        {
                            beacon_blue.setVisibility(View.GONE);
                            beacon_green.setVisibility(View.GONE);
                            beacon_purple.setVisibility(View.VISIBLE);
                        }
                    }
                });


                Log.d("BT", "Mac：" + mac
                        + " \nUUID：" + uuid + "\nMajor：" + major + "\nMinor："
                        + minor + "\nTxPower：" + txPower + "\nrssi：" + rssi);

                Log.d("BT","distance："+calculateAccuracy(txPower,rssi));

            }
        }


    };

    /*private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            device_text.append("Device Name: " + result.getDevice().getName() + " rssi: " + result.getRssi() + "\n");

            // auto scroll for text view
            final int scrollAmount = device_text.getLayout().getLineTop(device_text.getLineCount()) - device_text.getHeight();
            // if there is no need to scroll, scrollAmount will be <=0
            if (scrollAmount > 0)
                device_text.scrollTo(0, scrollAmount);
        }

    };

    public void startScanning() {
        System.out.println("start scanning");
        device_text.setText("");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.startScan(scanCallback);
            }
        });
    }

    public void stopScanning() {
        System.out.println("stopping scanning");
        device_text.append("Stopped Scanning");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.stopScan(scanCallback);
            }
        });
    }*/

    public String bytesToHex(byte[] bytes) {

        char[] hexArray = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0)
        {
            return -1.0;
        }

        double ratio = rssi * 1.0 / txPower;

        if (ratio < 1.0)
        {
            return Math.pow(ratio, 10);
        }
        else
        {
            double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    }
}