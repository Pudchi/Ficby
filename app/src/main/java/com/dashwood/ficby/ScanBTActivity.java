package com.dashwood.ficby;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhaoxiaodan.miband.MiBand;

import java.util.ArrayList;
import java.util.HashMap;

import static com.dashwood.ficby.MainActivity.SOFT_MEDIUM;

public class ScanBTActivity extends AppCompatActivity {

    //ImageView bt_click;
    TextView scanned_text;
    Typeface typeface_zh_medium;
    private MiBand miBand;
    ListView bt_device_list;
    private ArrayList<String> mDeviceList = new ArrayList<String>();

    HashMap<String, BluetoothDevice> devices = new HashMap<String, BluetoothDevice>();

    BluetoothAdapter bluetoothAdapter;
    private final static int REQUEST_ENABLE_BT=1;
    private final static int DISABLE_BT = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bt);



        typeface_zh_medium = Typeface.createFromAsset(getAssets(), SOFT_MEDIUM);
        //bt_click = (ImageView) findViewById(R.id.scan_bt_pic);
        scanned_text = (TextView) findViewById(R.id.scan_text);
        scanned_text.setTypeface(typeface_zh_medium);
        bt_device_list = (ListView) findViewById(R.id.bt_device_list);


        miBand = new MiBand(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();
        //final ArrayAdapter bt_device_adapter = new ArrayAdapter<String>(this, R.layout.bt_device_item);

        //MiBand.startScan(scanCallback);

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

        /*final ScanCallback scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                BluetoothDevice bluetoothDevice = result.getDevice();

                String device_item = bluetoothDevice.getName() + " | " + bluetoothDevice.getAddress();
                if (!devices.containsKey(devices))
                {
                    devices.put(device_item, bluetoothDevice);
                    bt_device_adapter.add(device_item);
                }
            }
        };*/


    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDeviceList.add(device.getName() + "\n" + device.getAddress());
                Log.i("BT", device.getName() + "\n" + device.getAddress());
                bt_device_list.setAdapter(new ArrayAdapter<String>(context,
                        R.layout.list_item, mDeviceList));
            }
        }
    };




}
