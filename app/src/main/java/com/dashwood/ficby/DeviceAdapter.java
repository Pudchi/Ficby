package com.dashwood.ficby;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DeviceAdapter extends ArrayAdapter<ScannedDevice> {
    private static final String PREFIX_RSSI = "RSSI:";
    private static final String PREFIX_LASTUPDATED = "Last Udpated:";
    private List<ScannedDevice> mList;
    private LayoutInflater mInflater;
    private int mResId;
    public double[] x;

    public DeviceAdapter(Context context, int resId, List<ScannedDevice> objects) {
        super(context, resId, objects);
        mResId = resId;
        mList = objects;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ScannedDevice item = (ScannedDevice) getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(mResId, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.device_name);
        name.setText(item.getDisplayName());
        TextView address = (TextView) convertView.findViewById(R.id.device_address);
        address.setText(item.getDevice().getAddress());
        TextView rssi = (TextView) convertView.findViewById(R.id.device_rssi);
        rssi.setText(PREFIX_RSSI + Integer.toString(item.getRssi()));
        TextView lastupdated = (TextView) convertView.findViewById(R.id.device_lastupdated);
        lastupdated.setText(PREFIX_LASTUPDATED + DateUtil.get_yyyyMMddHHmmss(item.getLastUpdatedMs()));

        TextView ibeaconInfo = (TextView) convertView.findViewById(R.id.device_ibeacon_info);
        Resources res = convertView.getContext().getResources();
        if (item.getIBeacon() != null) {
            ibeaconInfo.setText("THIS IS iBeacon!" + "\n" + item.getIBeacon().toString());
            x=item.getPosition(item);
        } else {
            ibeaconInfo.setText("NOT iBeacon!");
        }
        TextView scanRecord = (TextView) convertView.findViewById(R.id.device_scanrecord);
        scanRecord.setText(item.getScanRecordHexString());

        return super.getView(position, convertView, parent);
    }

    public static double blue_rssiaverage = -1;
    public static double blue_rssimax = -1;
    public static double blue_rssimin = -1;
    public static double blue_times = 0;
    public static double blue_disaverage = -1;
    public static double blue_dismax = -1;
    public static double blue_dismin = -1;
    public static double blue_dis = 0;
    public static double green_rssiaverage = -1;
    public static double green_rssimax = -1;
    public static double green_rssimin = -1;
    public static double green_times = 0;
    public static double green_disaverage = -1;
    public static double green_dismax = -1;
    public static double green_dismin = -1;
    public static double green_dis = 0;
    public static double purple_rssiaverage = -1;
    public static double purple_rssimax = -1;
    public static double purple_rssimin = -1;
    public static double purple_times = 0;
    public static double purple_disaverage = -1;
    public static double purple_dismax = -1;
    public static double purple_dismin = -1;
    public static double purple_dis = 0;
    public static boolean clc = false ;
    public String update(BluetoothDevice newDevice, int rssi, byte[] scanRecord) {
        if ((newDevice == null) || (newDevice.getAddress() == null)) {
            return "";
        }
        long now = System.currentTimeMillis();

        boolean contains = false;
        for (ScannedDevice device : mList) {
            if (newDevice.getAddress().equals(device.getDevice().getAddress())) {
                contains = true;
                // update
                device.setRssi(rssi);
                device.setLastUpdatedMs(now);
                device.setScanRecord(scanRecord);
                if(device.getDevice().getAddress().equals("E3:7A:AA:95:09:0A")) {
                    clc = true;
                    blue_disaverage = device.blue_disaverage;
                    blue_dismax = device.blue_dismax;
                    blue_dismin = device.blue_dismin;
                    blue_times = device.blue_times;
                    blue_rssiaverage = device.blue_rssiaverage;
                    blue_rssimin = device.blue_rssimin;
                    blue_rssimax = device.blue_rssimax;
                    blue_dis=device.blue_dis;
                }else if(device.getDevice().getAddress().equals("E6:18:AB:E5:61:70")){
                    clc = true;
                    green_disaverage = device.green_disaverage;
                    green_dismax = device.green_dismax;
                    green_dismin = device.green_dismin;
                    green_times = device.green_times;
                    green_rssiaverage = device.green_rssiaverage;
                    green_rssimin = device.green_rssimin;
                    green_rssimax = device.green_rssimax;
                    green_dis=device.green_dis;
                }else if(device.getDevice().getAddress().equals("DE:A7:2D:53:BA:E8")){
                    clc = true;
                    purple_disaverage = device.purple_disaverage;
                    purple_dismax = device.purple_dismax;
                    purple_dismin = device.purple_dismin;
                    purple_times = device.purple_times;
                    purple_rssiaverage = device.purple_rssiaverage;
                    purple_rssimin = device.purple_rssimin;
                    purple_rssimax = device.purple_rssimax;
                    purple_dis=device.purple_dis;
                }
                break;
            }
        }
        if (!contains) {
            // add new BluetoothDevice
            mList.add(new ScannedDevice(newDevice, rssi, scanRecord, now));
        }

        // sort by RSSI
        Collections.sort(mList, new Comparator<ScannedDevice>() {
            @Override
            public int compare(ScannedDevice lhs, ScannedDevice rhs) {
                if (lhs.getRssi() == 0) {
                    return 1;
                } else if (rhs.getRssi() == 0) {
                    return -1;
                }
                if (lhs.getRssi() > rhs.getRssi()) {
                    return -1;
                } else if (lhs.getRssi() < rhs.getRssi()) {
                    return 1;
                }
                return 0;
            }
        });

        notifyDataSetChanged();

        // create summary
        int totalCount = 0;
        int iBeaconCount = 0;
        if (mList != null) {
            totalCount = mList.size();
            for (ScannedDevice device : mList) {
                if (device.getIBeacon() != null) {
                    iBeaconCount++;
                }
            }
        }
        String summary="not ready";
        if(x!=null) {
            summary = "iBeacon:" + Integer.toString(iBeaconCount) + " (Total:" + Integer.toString(totalCount) + ")";
            //fun();
        }
        return summary;
    }
}
