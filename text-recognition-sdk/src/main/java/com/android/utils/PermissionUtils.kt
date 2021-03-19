package com.android.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*


// Make it a class and provide it by Koin
internal object PermissionUtils {

    private const val PERMISSION_REQUESTS = 100

    fun getRuntimePermissions(activity: Activity) {
        val allNeededPermissions: MutableList<String> = ArrayList()
        for (permission in getRequiredPermissions(activity)) {
            if (!isPermissionGranted(activity, permission)) {
                allNeededPermissions.add(permission)
            }
        }
        if (allNeededPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                allNeededPermissions.toTypedArray(),
                PERMISSION_REQUESTS
            )
        }
    }

    fun allPermissionsGranted(activity: Activity): Boolean {
        for (permission in getRequiredPermissions(activity)) {
            if (!isPermissionGranted(activity, permission)) {
                return false
            }
        }
        return true
    }

    private fun getRequiredPermissions(activity: Activity): List<String> {
        return try {
            val info = activity.packageManager
                .getPackageInfo(activity.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions.toList()
            if (ps.isNotEmpty()) {
                ps
            } else {
                listOf()
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false
    }

}