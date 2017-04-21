package com.dashwood.ficby;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;

public class BTDeviceStore {
    private final Map<String, BluetoothLeDevice> mDeviceMap;


    public BTDeviceStore(){
        mDeviceMap = new HashMap<String, BluetoothLeDevice>();
    }

    public void addDevice(BluetoothLeDevice device){
        if(mDeviceMap.containsKey(device.getAddress())){
            mDeviceMap.get(device.getAddress()).updateRssiReading(device.getTimestamp(), device.getRssi());
        } else {
            mDeviceMap.put(device.getAddress(), device);
        }
    }

    public void clear(){
        mDeviceMap.clear();
    }


    private FileWriter generateFile(File file, String contents){
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.append(contents);
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writer;
    }

    /*public EasyObjectCursor<BluetoothLeDevice> getDeviceCursor(){
        return new EasyObjectCursor<BluetoothLeDevice>(
                BluetoothLeDevice.class,
                getDeviceList(),
                "address");
    }*/

    public List<BluetoothLeDevice> getDeviceList(){
        final List<BluetoothLeDevice> methodResult = new ArrayList<BluetoothLeDevice>(mDeviceMap.values());

        Collections.sort(methodResult, new Comparator<BluetoothLeDevice>() {

            @Override
            public int compare(BluetoothLeDevice arg0, BluetoothLeDevice arg1) {
                return arg0.getAddress().compareToIgnoreCase(arg1.getAddress());
            }
        });

        return methodResult;
    }

}
