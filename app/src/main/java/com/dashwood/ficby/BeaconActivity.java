package com.dashwood.ficby;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class BeaconActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Typeface typeface_zh_medium;
    TextView location_text, goto_beacon_hint;
    Button turn_format, get_location;
    ImageView goto_beacon;
    LottieAnimationView locate_animation;


    double latitude;
    double longitude;
    String TAG_Warn;
    String locate = "";
    String result_address = "";
    static int loc_update_hit = 0;
    static int loc_flag = 0;
    static int format_button_flag = 0;
    public int c = 0;
    public double i = 0, j = 0;


    public static String cant_get_location = "   Sorry... 無法定位\n請更改手機定位設定!";
    public static String no_internet = "請檢查手機是否連上網路\n或是手機定位設定!";
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    static int click_once = 0;
    private static int UPDATE_INTERVAL = 5000; // 5s
    private static int FATEST_INTERVAL = 1000; // 1s
    private static int DISPLACEMENT = 5; // 5m

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;




    Runnable Loc_update = new Runnable() {
        @Override
        public void run() {
            try {
                /*createLocationRequest();*/
                PeriodicLocationUpdates();
            } catch (Exception e) {
                e.getMessage();
            }
        }
    };
    private LocationManager locationManager;


    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BeaconActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(), "Location Permission Granted", Toast.LENGTH_SHORT).show();


                } else {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("未取得位置權限, App 功能可能不如預期");
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



    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d("Distance", "Latitude:" + mLastLocation.getLatitude()
                    + ", Longtitude:" + mLastLocation.getLongitude());

            latitude = mLastLocation.getLatitude(); //緯度
            longitude = mLastLocation.getLongitude();  //經度
            locate = "經度:" + longitude + "\n緯度:" + latitude;

            if (locate.length() < 5) {
                loc_flag = 1;
            } else {
                loc_flag = 0;
            }
        }
    }

    private void PeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;

            startLocationUpdates();
            Log.d(TAG_Warn, "Periodic location updates started!");
        } else {
            mRequestingLocationUpdates = false;

            stopLocationUpdates();

            Log.d(TAG_Warn, "Periodic location updates stopped!");
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) getApplicationContext());
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi
                .removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) getApplicationContext());
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null)
        {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locate_animation.cancelAnimation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        locate_animation.cancelAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //if ((mBTAdapter != null) && (!mBTAdapter.isEnabled())) {
        //    Toast.makeText(this, "藍牙未開啟", Toast.LENGTH_SHORT).show();
        //}

        mGoogleApiClient.connect();
        checkLocationPermission();
        locate_animation.playAnimation();
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        //TextView ble_list = (TextView)findViewById(R.id.beacon_list_text);
        typeface_zh_medium = TypefaceProvider.getTypeFace(getApplicationContext(), "Noto_Sans_Soft_Medium.ttf");


        get_location = (Button) findViewById(R.id.btn_locate);
        get_location.setTypeface(typeface_zh_medium);
        turn_format = (Button) findViewById(R.id.btn_change_format);
        turn_format.setTypeface(typeface_zh_medium);
        location_text = (TextView) findViewById(R.id.locate_text);
        location_text.setTypeface(typeface_zh_medium);
        goto_beacon = (ImageView) findViewById(R.id.beacon_pic);
        goto_beacon_hint = (TextView) findViewById(R.id.goto_beacon_text);
        goto_beacon_hint.setTypeface(typeface_zh_medium);
        locate_animation = (LottieAnimationView) findViewById(R.id.locate_animation);

        buildGoogleApiClient();

        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect peripherals.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
                        }
                    }
                });
                builder.show();
            }
        }

        createLocationRequest();
        getLocation();

        goto_beacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BeaconActivity.this, ScanBeaconActivity.class));

            }
        });

        get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_location_view(2000);

                new Thread(Loc_update).start();
                Log.d("Thread: ", "Update_loc Thread start!");

                loc_update_hit = 1;
                click_once++;

            }
        });

        turn_format.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loc_update_hit == 1 && is_Connecting_to_internet())
                {
                    //turn_format.setText("經緯度");

                    loc_update_hit = 0 ;
                    turn_loc_view(3000);

                } else if (loc_update_hit == 0 && is_Connecting_to_internet())
                {
                    //turn_format.setText("地址");

                    Toast.makeText(getApplicationContext(), "請先獲得經緯度位置才能轉換地址!", Toast.LENGTH_LONG).show();

                } else {
                    String result_nw = "Network Problem";
                    Message nw = new Message();
                    nw.what = 5000;
                    Bundle d_nw = new Bundle();

                    try {
                        getLocation();
                    } catch (Exception e_loc) {
                        e_loc.getMessage();
                    }

                    d_nw.putString("Result", result_nw);

                    nw.setData(d_nw);
                    loc_handler.sendMessage(nw);
                }

            }
            });
    }

    public boolean is_Connecting_to_internet() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            Toast.makeText(getApplication(), "請檢查是否連上網路", Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;

    }

    public String get_loc_turnto_address(Location loc) throws IOException {
        if(loc != null)
        {
            try
            {

                Geocoder gc_loc = new Geocoder(getApplicationContext(), Locale.TRADITIONAL_CHINESE);
                List<Address> lastAddress = gc_loc.getFromLocation(latitude, longitude, 5);
                result_address = lastAddress.get(0).getAddressLine(0);
            } catch (IOException e_t)
            {
                e_t.getMessage();

            }
            catch (IllegalArgumentException arg_e)
            {
                arg_e.getMessage();
            }
        }
        return result_address;

    }

    private void get_location_view(final int what_value)
    {
        Runnable update_loc_view = new Runnable()
        {
            @Override
            public void run()
            {
                String result = "Success";
                Message me = new Message();
                me.what = what_value;
                Bundle d = new Bundle();

                try
                {
                    getLocation();
                } catch (Exception e_loc)
                {
                    e_loc.getMessage();
                }

                d.putString("Result", result);
                me.setData(d);
                loc_handler.sendMessage(me);
            }
        };
        new Thread(update_loc_view).start();
    }

    private void turn_loc_view(final int value)
    {
        Runnable update_format_loc = new Runnable() {
            @Override
            public void run()
            {
                String result = "Success";
                Message me = new Message();
                me.what = value;
                Bundle d = new Bundle();

                try
                {
                    get_loc_turnto_address(mLastLocation);
                } catch (Exception e_loc)
                {
                    e_loc.getMessage();
                }

                d.putString("Result", result);
                me.setData(d);
                loc_handler.sendMessage(me);
            }
        };
        new Thread(update_format_loc).start();
    }

    android.os.Handler loc_handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    String st = msg.getData().getString("Result");


                    if (loc_flag == 0) {
                        location_text.setText(locate);
                    } else {
                        location_text.setText(cant_get_location);
                    }
                    break;

                case 3000:
                    String st_second = msg.getData().getString("Result");


                    String two_line_address_1 = result_address.substring(0, 3);
                    String two_line_address_2 = result_address.substring(3, 10);
                    String two_line_address_3 = result_address.substring(10);

                    location_text.setText(two_line_address_1 + "\n" + two_line_address_2 + "\n" + two_line_address_3);


                    break;

                case 5000:

                    String st_nw = msg.getData().getString("Result");

                    location_text.setText(no_internet);


                default:
                    break;

            }
        }
    };


    /*private void initialize(final int value)
    {
        Runnable init = new Runnable() {
            @Override
            public void run() {

                String result = "Success";
                Message me = new Message();
                me.what = value;
                Bundle d = new Bundle();

                if (!BLEUtil.isBLESupported(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "此裝置不支援低功率藍牙(BLE)", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                try {
                    BluetoothManager manager = BLEUtil.getManager(getApplicationContext());
                    if (manager != null) {
                        mBTAdapter = manager.getAdapter();
                    }
                    if (mBTAdapter == null) {
                        Toast.makeText(getApplicationContext(), "SORRY, 此裝置不支援藍牙", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }catch (Exception bt)
                {
                    bt.getMessage();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDeviceAdapter = new DeviceAdapter(getApplicationContext(), R.layout.beacon_list, new ArrayList<ScannedDevice>());
                            }
                        });
                    }
                }).start();





                d.putString("Result", result);
                me.setData(d);
                scan_handler.sendMessage(me);

            }
        };
        new Thread(init).start();

    }

    /*android.os.Handler scan_handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 1000:
                    deviceListView.setAdapter(mDeviceAdapter);
                    stopScan();
                    Toast.makeText(getApplicationContext(), "Scan Initialized", Toast.LENGTH_SHORT).show();
                    break;

                case 4000:
                    startScan();
                    break;

                case 6000:
                    stopScan();
                    break;

                default:
                    break;
            }
        }
    };



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
                        textView = (TextView) findViewById(R.id.beacon_list_text);
                        final String str = "Blue\nRSSI_MAX:" + mDeviceAdapter.blue_rssimax
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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(str);
                            }
                        });

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

    /*private void startScan() {
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
    }*/

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;

        getLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Failed", connectionResult.toString());
    }
}
