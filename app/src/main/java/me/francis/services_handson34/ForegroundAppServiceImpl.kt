package me.francis.services_handson34

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ForegroundAppServiceImpl : Service() {

    private lateinit var appListService: ForegroundAppService

    override fun onCreate() {
        super.onCreate()
        appListService = ForegroundAppService(applicationContext)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return appListService
    }

}