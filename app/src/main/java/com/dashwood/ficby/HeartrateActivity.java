package com.dashwood.ficby;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

    Typeface typeface_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartrate);

        typeface_book = TypefaceProvider.getTypeFace(getApplicationContext(), "Circular_book.ttf");

        bar_loop = (LottieAnimationView) findViewById(R.id.four_bar_loop_anim);

        heartrate_list = (ListView) findViewById(R.id.heartrate_list);



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

        DatabaseReference reference_heartrate = FirebaseDatabase.getInstance().getReference("userid/"+user.getDisplayName());
        reference_heartrate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot ds_hr : dataSnapshot.getChildren())
                {
                    adapter.add("Heart Rate: " + ds_hr.child("heartrate").getValue().toString());
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
