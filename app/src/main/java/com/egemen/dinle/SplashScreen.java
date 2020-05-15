package com.egemen.dinle;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.egemen.dinle.login.login_activity;

public class SplashScreen extends AppCompatActivity {
    public ImageView logo_holder;
    public TextView text_holder;
    public Animation anim_logo;
    public Animation anim_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo_holder = findViewById(R.id.imageView);
        text_holder = findViewById(R.id.textView);
        logo_holder.setVisibility(View.INVISIBLE);
        text_holder.setVisibility(View.INVISIBLE);

        anim_logo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_info_popup);
        anim_txt = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_info_popup);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this, login_activity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, 1200);
    }



    protected void onPause() {
        super.onPause();
        logo_holder.setVisibility(View.INVISIBLE);
        text_holder.setVisibility(View.INVISIBLE);
    }

    protected void onResume() {
        super.onResume();
        logo_holder.startAnimation(anim_logo);
        Handler handler = new Handler();
        Runnable startMain = new Runnable() {
            @Override
            public void run() {
                if (!SplashScreen.this.isFinishing()) {
                    text_holder.startAnimation(anim_txt);
                }
            }
        };
        handler.postDelayed(startMain, 200);
    }




}
