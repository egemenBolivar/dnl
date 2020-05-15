package com.egemen.dinle.utils

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar


class mSnackbar {
    companion object {
        fun snackbar(view: View, yazi:String, lenght :Int = Snackbar.LENGTH_SHORT) {
            Snackbar.make(view,yazi,lenght).show()

        }
        fun Rsnackbar(view: View, yazi:String, lenght :Int = Snackbar.LENGTH_SHORT){
            var msnackbar: Snackbar = Snackbar.make(view,yazi,lenght)
            var snackBarLayout: View =msnackbar.view
            snackBarLayout.setBackgroundColor(Color.RED)
            msnackbar.show()
        }

    }
}