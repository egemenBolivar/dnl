package com.egemen.dinle.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object mailvalidate {

    fun isEmailValid(email:String):Boolean{
        var expression="^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        var patern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        var matcher: Matcher =patern.matcher(email);
        return matcher.matches();

    }
}