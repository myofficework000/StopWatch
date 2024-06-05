package com.android.stopwatchdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.stopwatchdemo.ui.theme.StopWatchDemoTheme
import com.training.stopwatchboundservice.StopwatchViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: StopwatchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopwatchApp(viewModel)
        }
        viewModel.bindService(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unbindService(this)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StopWatchDemoTheme {
        Greeting("Android")
    }
}