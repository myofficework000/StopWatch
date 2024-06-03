package com.android.stopwatchdemo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.stopwatchdemo.model.StopwatchState
import com.android.stopwatchdemo.viewmodel.StopwatchViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@Composable
fun StopwatchScreen(viewModel: StopwatchViewModel = viewModel()) {
    val stopwatchState by viewModel.stopwatchState.observeAsState(StopwatchState())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Start/Stop Button
        Button(
            onClick = { if (stopwatchState.isRunning) viewModel.stopStopwatch() else viewModel.startStopwatch() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (stopwatchState.isRunning) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = CircleShape,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                    )
                )
        ) {
            Text(
                text = if (stopwatchState.isRunning) "STOP" else "START",
                fontSize = 24.sp
            )

        }

        Spacer(modifier = Modifier.height(50.dp))

        // Time Display
        Text(
            text = String.format(
                "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(stopwatchState.time),
                TimeUnit.MILLISECONDS.toSeconds(stopwatchState.time) % 60,
                stopwatchState.time % 100
            ),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Reset Button
        Button(
            onClick = { viewModel.resetStopwatch() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = CircleShape,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(0.5f)
                .clip(CircleShape)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                    )
                )
        ) {
            Text(
                text = "RESET",
                fontSize = 20.sp
            )
        }
    }

    LaunchedEffect(stopwatchState.isRunning) {
        while (stopwatchState.isRunning) {
            delay(10)
            viewModel.updateElapsedTime()
        }
    }
}