package me.francis.services_handson34

import android.content.Context
import android.content.pm.PackageManager

class InstalledAppsService(val context: Context) : IInstalledAppsService.Stub() {

    override fun getInstalledApps(): List<String?>? {
        val packageManager = context.packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        return installedApps.map { packageManager.getApplicationLabel(it).toString() }
    }
}
