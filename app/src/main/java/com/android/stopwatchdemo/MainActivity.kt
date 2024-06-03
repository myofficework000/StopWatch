package com.android.stopwatchdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.android.stopwatchdemo.ui.StopwatchScreen
import com.android.stopwatchdemo.ui.theme.StopwatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopwatchTheme {
                StopwatchScreen()
            }
        }
    }
}