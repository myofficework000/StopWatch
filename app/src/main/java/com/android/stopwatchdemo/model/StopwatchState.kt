package com.android.stopwatchdemo.model

data class StopwatchState(
    val time: Long = 0L,
    val isRunning: Boolean = false
)