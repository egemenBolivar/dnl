package com.egemen.dinle.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.lang.Exception

object connectioncontrol {


    fun isConnected(context: Context):Boolean{
        var isConnected = false
        try{
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        isConnected = activeNetwork?.isConnected == true
        yaz.yaz("ısConnected "+isConnected)

        }catch (e :Exception){e.printStackTrace()}
       finally {
           return isConnected
       }
    }
}