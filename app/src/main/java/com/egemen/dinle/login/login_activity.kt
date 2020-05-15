package com.egemen.dinle.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.egemen.dinle.R
import com.egemen.dinle.UniversalImageLoader
import com.nostra13.universalimageloader.core.ImageLoader

class login_activity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setTheme(R.style.AppTheme)
        setContentView(R.layout.home_layout)
        initImageLoader()
    }
    private fun initImageLoader() {
        val universalImageLoader = UniversalImageLoader(this)
        ImageLoader.getInstance().init(universalImageLoader.config)


    }
}