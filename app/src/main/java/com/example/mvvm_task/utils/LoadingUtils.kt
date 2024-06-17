package com.example.mvvm_task.utils


import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.example.mvvm_task.R

class LoadingUtils(val activity: Activity) {
    lateinit var alertDialog: AlertDialog

    fun showLoading(){
        var dialogView = activity.layoutInflater.inflate(R.layout.loading_dialog,null)
        var builder= AlertDialog.Builder(activity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog.show()
    }

    fun dismiss(){
        alertDialog.dismiss()
    }

}