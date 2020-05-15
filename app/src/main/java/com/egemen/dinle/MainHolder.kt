package com.egemen.dinle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainHolder: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) { //Uzmanlar ve Mesajlar için ana Activity
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.mainholder_activty)


    }
}