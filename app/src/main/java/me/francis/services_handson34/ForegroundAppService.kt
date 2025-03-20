package me.francis.services_handson34

import android.content.Context
import android.content.pm.PackageManager

class ForegroundAppService(val context: Context) : IForegroundAppsService.Stub() {

    override fun getForegroundApps(): MutableList<String> {
        val packageManager = context.packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        return installedApps.map { packageManager.getApplicationLabel(it).toString() }.toMutableList()
    }

}