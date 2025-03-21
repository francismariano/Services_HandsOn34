package me.francis.services_handson34

import android.app.Service
import android.content.Intent
import android.os.IBinder

class InstalledAppsServiceImpl : Service() {

    private lateinit var appListService: InstalledAppsService

    override fun onCreate() {
        super.onCreate()
        appListService = InstalledAppsService(applicationContext)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return appListService
    }
}
