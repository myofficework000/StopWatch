package com.android.stopwatchdemo.viewmodel

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.stopwatchdemo.model.StopwatchState
import com.android.stopwatchdemo.repository.StopwatchRepository

class StopwatchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StopwatchRepository(application)
    private val _stopwatchState = MutableLiveData<StopwatchState>()
    val stopwatchState: LiveData<StopwatchState> = _stopwatchState

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val elapsedTime = intent?.getLongExtra("elapsedTime", 0L) ?: 0L
            _stopwatchState.value = _stopwatchState.value?.copy(time = elapsedTime)
        }
    }

    init {
        repository.bindService()
        LocalBroadcastManager.getInstance(application).registerReceiver(
            broadcastReceiver, IntentFilter("StopwatchUpdate")
        )
    }

    override fun onCleared() {
        super.onCleared()
        repository.unbindService()
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(broadcastReceiver)
    }

    fun startStopwatch() {
        repository.startStopwatch()
        _stopwatchState.value = StopwatchState(time = repository.getElapsedTime(), isRunning = true)
    }

    fun stopStopwatch() {
        repository.stopStopwatch()
        _stopwatchState.value = StopwatchState(time = repository.getElapsedTime(), isRunning = false)
    }

    fun resetStopwatch() {
        repository.resetStopwatch()
        _stopwatchState.value = StopwatchState(time = 0L, isRunning = false)
    }

    fun updateElapsedTime() {
        _stopwatchState.value = _stopwatchState.value?.copy(time = repository.getElapsedTime())
    }
}