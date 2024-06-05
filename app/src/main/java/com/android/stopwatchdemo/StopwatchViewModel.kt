package com.training.stopwatchboundservice

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class StopwatchViewModel(application: Application) : AndroidViewModel(application) {

    private val _time = MutableLiveData<Long>()
    val time: LiveData<Long> = _time

    private val _isRunning = MutableLiveData<Boolean>()
    val isRunning: LiveData<Boolean> = _isRunning

    private var stopwatchService: StopwatchService? = null
    private var bound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as StopwatchService.StopwatchBinder
            stopwatchService = binder.getService()
            bound = true
            updateTime()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }
    }

    fun bindService(context: Context) {
        Intent(context, StopwatchService::class.java).also { intent ->
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    fun unbindService(context: Context) {
        if (bound) {
            context.unbindService(connection)
            bound = false
        }
    }

    fun startStopwatch() {
        stopwatchService?.startStopwatch()
        _isRunning.value = true
        updateTime()
    }

    fun stopStopwatch() {
        stopwatchService?.stopStopwatch()
        _isRunning.value = false
    }

    fun resetStopwatch() {
        stopwatchService?.resetStopwatch()
        _time.value = 0L
    }

    private fun updateTime() {
        stopwatchService?.let {
            _time.value = it.getTime()
            if (it.isRunning()) {
                viewModelScope.launch {
                    kotlinx.coroutines.delay(1000)
                    updateTime()
                }
            }
        }
    }
}