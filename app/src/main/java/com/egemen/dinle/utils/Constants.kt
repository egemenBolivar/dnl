package com.egemen.dinle.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object Constants {

    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid.toString()

}