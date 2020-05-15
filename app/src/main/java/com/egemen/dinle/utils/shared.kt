package com.egemen.dinle.utils

import android.content.Context
import com.egemen.dinle.enums.def

object shared {


    fun gStr(context:Context,def:Enum<def>):String{
        val shrd = context.getSharedPreferences("com.egemen.dinle", Context.MODE_PRIVATE)
        return  shrd.getString(def.toString(), "null")!!
    }
    fun pStr(context:Context,def:Enum<def>,veri:String){
        val shrd = context.getSharedPreferences("com.egemen.dinle", Context.MODE_PRIVATE)
        val editor = shrd.edit()
        editor.putString(def.toString(), veri )
        editor.commit()
    }



}