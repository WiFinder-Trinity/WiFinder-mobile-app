package fr.wifinder.wifinderandroid.services

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

class PermissionsService(context: Activity) {
    private val context = context

    fun askChangeWifiPermission(): Boolean{
        return if (ContextCompat.checkSelfPermission(context.applicationContext,
                        android.Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(context,
                    arrayOf(android.Manifest.permission.CHANGE_WIFI_STATE),1 )
            false
        }
    }

    fun askAccessWifiPermission(): Boolean{
        return if(ContextCompat.checkSelfPermission(context.applicationContext,
                        android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(context, arrayOf(android.Manifest.permission.ACCESS_WIFI_STATE), 1)
            false
        }
    }

    fun askFineLocationPermission(): Boolean{
        return if(ContextCompat.checkSelfPermission(context.applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(context, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            false
        }
    }
}