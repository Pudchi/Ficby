package com.dashwood.ficby;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BeaconActivity extends AppCompatActivity implements BluetoothAdapter.LeScanCallback{

    private BluetoothAdapter mBTAdapter;
    private DeviceAdapter mDeviceAdapter;
    private boolean mIsScanning;

    public int c=0;
    public double i=0,j=0;

    @Override
    protected void onResume() {
        super.onResume();

        if ((mBTAdapter != null) && (!mBTAdapter.isEnabled())) {
            Toast.makeText(this, "藍牙未開啟", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        TextView ble_list = (TextView)findViewById(R.id.textView);
        ImageView blue_beacon = (ImageView) findViewById(R.id.blue_beacon);
        ImageView green_beacon = (ImageView) findViewById(R.id.green_beacon);
        ImageView purple_beacon = (ImageView) findViewById(R.id.purple_beacon);
        final Button scan = (Button) findViewById(R.id.btn_scan);

        String str = "Blue\nRSSI_MAX:" + mDeviceAdapter.blue_rssimax
                + "\nRSSI_AVG:" + mDeviceAdapter.blue_rssiaverage
                + "\nRSSI_Min:" + mDeviceAdapter.blue_rssimin
                + "\nDIS_MAX:" + mDeviceAdapter.blue_dismax
                + "\nDIS_AVG:" + mDeviceAdapter.blue_disaverage
                + "\nDIS_Min:" + mDeviceAdapter.blue_dismin
                + "\nGreen\nRSSI_MAX:" + mDeviceAdapter.green_rssimax
                + "\nRSSI_AVG:" + mDeviceAdapter.green_rssiaverage
                + "\nRSSI_Min:" + mDeviceAdapter.green_rssimin
                + "\nDIS_MAX:" + mDeviceAdapter.green_dismax
                + "\nDIS_AVG:" + mDeviceAdapter.green_disaverage
                + "\nDIS_Min:" + mDeviceAdapter.green_dismin
                + "\nPurple\nRSSI_MAX:" + mDeviceAdapter.purple_rssimax
                + "\nRSSI_AVG:" + mDeviceAdapter.purple_rssiaverage
                + "\nRSSI_Min:" + mDeviceAdapter.purple_rssimin
                + "\nDIS_MAX:" + mDeviceAdapter.purple_dismax
                + "\nDIS_AVG:" + mDeviceAdapter.purple_disaverage
                + "\nDIS_Min:" + mDeviceAdapter.purple_dismin;
        ble_list.setText(str);
        initialize();

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scan.getText().toString() == "SCAN")
                {
                    if (!mBTAdapter.isEnabled())
                    {
                        Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBluetooth, 1);
                    }
                    startScan();
                    scan.setText("STOP");
                }

                else if (scan.getText().toString() == "STOP")
                {
                    stopScan();
                    scan.setText("SCAN");
                }

            }
        });
    }



    private void initialize() {

        if (!BLEUtil.isBLESupported(this)) {
            Toast.makeText(this, "此裝置不支援低功率藍牙(BLE)", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        BluetoothManager manager = BLEUtil.getManager(this);
        if (manager != null) {
            mBTAdapter = manager.getAdapter();
        }
        if (mBTAdapter == null) {
            Toast.makeText(this, "SORRY, 此裝置不支援藍牙", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ListView deviceListView = (ListView) findViewById(R.id.list);
        mDeviceAdapter = new DeviceAdapter(this, R.layout.beacon_list, new ArrayList<ScannedDevice>());
        deviceListView.setAdapter(mDeviceAdapter);
        stopScan();
    }


    @Override
    public void onLeScan(final BluetoothDevice newDevice, final int newRssi, final byte[] newScanRecord) {
        final DrawView drawview = new DrawView(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String summary = mDeviceAdapter.update(newDevice, newRssi, newScanRecord);
                double x[] = mDeviceAdapter.x;

                if (summary != null) {
                    if (mDeviceAdapter.clc) {
                        TextView textView = (TextView) findViewById(R.id.textView);
                        String str = "Blue\nRSSI_MAX:" + mDeviceAdapter.blue_rssimax
                                + "\nRSSI_AVG:" + mDeviceAdapter.blue_rssiaverage
                                + "\nRSSI_Min:" + mDeviceAdapter.blue_rssimin
                                + "\nDIS_MAX:" + mDeviceAdapter.blue_dismax
                                + "\nDIS_AVG:" + mDeviceAdapter.blue_disaverage
                                + "\nDIS_Min:" + mDeviceAdapter.blue_dismin
                                + "\nbluedis:" + mDeviceAdapter.blue_dis
                                + "\nGreen\nRSSI_MAX:" + mDeviceAdapter.green_rssimax
                                + "\nRSSI_AVG:" + mDeviceAdapter.green_rssiaverage
                                + "\nRSSI_Min:" + mDeviceAdapter.green_rssimin
                                + "\nDIS_MAX:" + mDeviceAdapter.green_dismax
                                + "\nDIS_AVG:" + mDeviceAdapter.green_disaverage
                                + "\nDIS_Min:" + mDeviceAdapter.green_dismin
                                + "\ngreendis:" + mDeviceAdapter.green_dis
                                + "\nPurple\nRSSI_MAX:" + mDeviceAdapter.purple_rssimax
                                + "\nRSSI_AVG:" + mDeviceAdapter.purple_rssiaverage
                                + "\nRSSI_Min:" + mDeviceAdapter.purple_rssimin
                                + "\nDIS_MAX:" + mDeviceAdapter.purple_dismax
                                + "\nDIS_AVG:" + mDeviceAdapter.purple_disaverage
                                + "\nDIS_Min:" + mDeviceAdapter.purple_dismin
                                + "\npurpledis:" + mDeviceAdapter.purple_dis;
                        textView.setText(str);
                        mDeviceAdapter.clc = false;
                    }

                    FrameLayout layout = (FrameLayout) findViewById(R.id.root);

                    if (x != null) {
                        i = i + x[0];
                        j = j + x[1];
                        c++;
                        if (c >= 5) {
                            i /= 5;
                            j /= 5;
                            c = 0;
                            drawview.invalidate();
                            layout.addView(drawview);
                        }
                    }
            }
        }


    });

    }

    private void startScan() {
        if ((mBTAdapter != null) && (!mIsScanning)) {
            mBTAdapter.startLeScan(this);
            mIsScanning = true;

        }
    }

    private void stopScan(){
        if (mBTAdapter != null) {
            mBTAdapter.stopLeScan(this);
        }
        mIsScanning = false;
    }
}
