package com.egemen.dinle.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object dbref {
     var mDatabaseReference: DatabaseReference
     var mStorageReference: StorageReference
     var mRef: DatabaseReference
    init {
        mStorageReference = FirebaseStorage.getInstance().reference
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        mRef = FirebaseDatabase.getInstance().reference
    }



}