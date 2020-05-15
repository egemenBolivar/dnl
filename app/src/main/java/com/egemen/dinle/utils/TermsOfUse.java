package com.egemen.dinle.utils;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.egemen.dinle.R;

public class TermsOfUse extends AppCompatActivity {


    //-----------------------------------------------
    // MARK - ON CREATE
    //-----------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_of_use);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
      //  Objects.requireNonNull(getSupportActionBar()).hide();



        //-----------------------------------------------
        // MARK - INITIALIZE WEBVIEW
        //-----------------------------------------------
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/privacypolicy.html");



        //-----------------------------------------------
        // MARK - BACK BUTTON
        //-----------------------------------------------
        Button backButt = findViewById(R.id.tosBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }});


    }// ./ onCreate



}
