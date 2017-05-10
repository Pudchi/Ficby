package com.dashwood.ficby;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HeartrateActivity extends AppCompatActivity {

    ListView heartrate_list;
    LottieAnimationView bar_loop;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseUser user;

    LottieAnimationView eye_loading;

    Typeface typeface_book;

    int[] hr_stable_add = new int[] {86, 88, 84, 81, 89, 106, 93, 108, 96, 80};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartrate);

        typeface_book = TypefaceProvider.getTypeFace(getApplicationContext(), "Circular_book.ttf");

        bar_loop = (LottieAnimationView) findViewById(R.id.four_bar_loop_anim);

        heartrate_list = (ListView) findViewById(R.id.heartrate_list);

        eye_loading = (LottieAnimationView) findViewById(R.id.eye_load);



        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user_tmp = firebaseAuth.getCurrentUser();
                if (user_tmp == null)
                {
                    finish();
                }
            }
        };
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.bt_device_item)
        {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView item = (TextView) super.getView(position, convertView, parent);
                item.setTypeface(typeface_book);

                return item;
            }
        };



        heartrate_list.setAdapter(adapter);

        //"userid/"+user.getDisplayName()
        final DatabaseReference reference_heartrate = FirebaseDatabase.getInstance().getReference("heartrate/"+user.getDisplayName());
        reference_heartrate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() == (long) 0)
                {

                    heartrate_list.setVisibility(View.GONE);
                    eye_loading.setVisibility(View.VISIBLE);
                    eye_loading.playAnimation();
                    Toast.makeText(getApplicationContext(), "同步心率資料至資料庫中...", Toast.LENGTH_SHORT).show();

                    for (int pos = 0; pos < 10; pos++)
                    {
                        reference_heartrate.child(String.valueOf(pos)).setValue(hr_stable_add[pos]);
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            eye_loading.cancelAnimation();
                            finish();
                        }
                    }, 5000);
                }

                else
                {
                    adapter.clear();
                    //for (DataSnapshot ds_hr : dataSnapshot.getChildren())
                    //{
                    //    adapter.add("Heart Rate: " + ds_hr.child("heartrate").getValue().toString());
                    //}

                    Log.d("Childrencount", String.valueOf(dataSnapshot.getChildrenCount()));

                    for (DataSnapshot ds_m2 : dataSnapshot.getChildren())
                    {
                        adapter.add("Heart Rate: " + ds_m2.getValue().toString());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
        bar_loop.cancelAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bar_loop.cancelAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bar_loop.playAnimation();
    }
}
