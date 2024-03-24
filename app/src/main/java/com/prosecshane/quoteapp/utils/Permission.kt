package com.prosecshane.quoteapp.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Object that manages permissions in a simpler way.
 */
object Permission {
    /**
     * Constants.
     */
    private const val GRANTED = PackageManager.PERMISSION_GRANTED
    private const val PERMISSION_NOT_REQUIRED = -1

    /**
     * Check whether a certain permission is granted.
     *
     * @param context Required [Context].
     * @param permission Permission to check.
     */
    fun isGranted(context: Context, permission: String): Boolean {
        when (permission) {
            android.Manifest.permission.POST_NOTIFICATIONS ->
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        }
        return ContextCompat.checkSelfPermission(context, permission) == GRANTED
    }

    /**
     * Request a certain permission.
     *
     * @param activity Required [Activity] from which the permission request will happen.
     * @param permission Permission to request.
     * @param customRequestCode Custom code for permission request.
     */
    fun request(activity: Activity, permission: String, customRequestCode: Int? = null) {
        val requestCode = customRequestCode ?: getRequestCode(permission)
        if (requestCode == PERMISSION_NOT_REQUIRED) return

        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    }

    /**
     * Get a request code for certain permission.
     *
     * @param permission Permission to convert to request code.
     */
    private fun getRequestCode(permission: String): Int = when (permission) {
        android.Manifest.permission.POST_NOTIFICATIONS ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) 1 else PERMISSION_NOT_REQUIRED
        // Add request codes here if needed!
        else ->
            throw Exception("A Permission Request Code for this permission is not defined. " +
                "Rewrite the Permission object to add a Permission Request Code. " +
                "Mind the constants at the start of the object.")
    }
}
