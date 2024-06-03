package com.android.stopwatchdemo.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class StopwatchService : Service() {

    private val binder = LocalBinder()
    private var isRunning = false
    private var elapsedTime = 0L
    private var startTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    inner class LocalBinder : Binder() {
        fun getService(): StopwatchService = this@StopwatchService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun startStopwatch() {
        if (!isRunning) {
            isRunning = true
            startTime = SystemClock.elapsedRealtime() - elapsedTime
            handler.post(runnable)
        }
    }

    fun stopStopwatch() {
        if (isRunning) {
            isRunning = false
            handler.removeCallbacks(runnable)
            elapsedTime = SystemClock.elapsedRealtime() - startTime
        }
    }

    fun resetStopwatch() {
        isRunning = false
        handler.removeCallbacks(runnable)
        elapsedTime = 0L
    }

    fun getElapsedTime(): Long {
        return if (isRunning) {
            SystemClock.elapsedRealtime() - startTime
        } else {
            elapsedTime
        }
    }

    override fun onCreate() {
        super.onCreate()
        runnable = object : Runnable {
            override fun run() {
                elapsedTime = SystemClock.elapsedRealtime() - startTime
                val intent = Intent("StopwatchUpdate")
                intent.putExtra("elapsedTime", elapsedTime)
                LocalBroadcastManager.getInstance(this@StopwatchService).sendBroadcast(intent)
                handler.postDelayed(this, 10)
            }
        }
    }
}