package com.android.stopwatchdemo.repository


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.android.stopwatchdemo.service.StopwatchService

class StopwatchRepository(private val context: Context) {

    private var service: StopwatchService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val localBinder = binder as StopwatchService.LocalBinder
            service = localBinder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
            isBound = false
        }
    }

    fun bindService() {
        val intent = Intent(context, StopwatchService::class.java)
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    fun unbindService() {
        if (isBound) {
            context.unbindService(connection)
            isBound = false
        }
    }

    fun startStopwatch() {
        service?.startStopwatch()
    }

    fun stopStopwatch() {
        service?.stopStopwatch()
    }

    fun resetStopwatch() {
        service?.resetStopwatch()
    }

    fun getElapsedTime(): Long {
        return service?.getElapsedTime() ?: 0L
    }
}