package com.training.stopwatchboundservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper

class StopwatchService : Service() {

    private val binder = StopwatchBinder()
    private var time = 0L
    private var running = false
    private val timerHandler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            if (running) {
                time++
                timerHandler.postDelayed(this, 1000)
            }
        }
    }

    inner class StopwatchBinder : Binder() {
        fun getService(): StopwatchService = this@StopwatchService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun startStopwatch() {
        if (!running) {
            running = true
            timerHandler.post(timerRunnable)
        }
    }

    fun stopStopwatch() {
        running = false
        timerHandler.removeCallbacks(timerRunnable)
    }

    fun resetStopwatch() {
        stopStopwatch()
        time = 0L
    }

    fun getTime(): Long {
        return time
    }

    fun isRunning(): Boolean {
        return running
    }
}
