package com.android.stopwatchdemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.stopwatchdemo.ui.theme.Pink80
import com.android.stopwatchdemo.ui.theme.buttonColor
import com.android.stopwatchdemo.ui.theme.dp_12
import com.android.stopwatchdemo.ui.theme.dp_16
import com.android.stopwatchdemo.ui.theme.dp_20
import com.android.stopwatchdemo.ui.theme.dp_200
import com.android.stopwatchdemo.ui.theme.dp_240
import com.android.stopwatchdemo.ui.theme.dp_8
import com.android.stopwatchdemo.ui.theme.sp_30
import com.android.stopwatchdemo.ui.theme.sp_40
import com.android.stopwatchdemo.ui.theme.sp_60
import com.training.stopwatchboundservice.StopwatchViewModel


@Composable
fun StopwatchApp(viewModel: StopwatchViewModel = viewModel()) {
    val time by viewModel.time.observeAsState(0L)
    val isRunning by viewModel.isRunning.observeAsState(false)
    val textColor= Pink80

    Surface(modifier = Modifier.fillMaxSize(), color = White) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(dp_16))
            Text(
                text = stringResource(id = R.string.stopwatch),
                fontWeight = FontWeight.Bold,
                color = textColor, fontSize = sp_40
            )
            Spacer(modifier = Modifier.height(dp_8))
            CircularButton(isRunning = isRunning, onStartClick = {
                viewModel.startStopwatch()
            }, onStopClick = {
                viewModel.stopStopwatch()
            })

            Text(text = formatTime(time), fontWeight = FontWeight.ExtraBold, fontSize = sp_60, color = textColor)

            Button(onClick = { viewModel.resetStopwatch() }, modifier = Modifier
                .fillMaxWidth()
                .padding(dp_20), colors = ButtonDefaults.buttonColors(buttonColor)) {
                Text(text = stringResource(id = R.string.reset), fontSize = sp_30 )
            }
            Spacer(modifier = Modifier.height(dp_16))
        }
    }
}

@Composable
fun CircularButton(isRunning: Boolean, onStartClick: () -> Unit, onStopClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(dp_240)
            .clip(CircleShape)
            .background(Pink80),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { if (isRunning) onStopClick() else onStartClick() },
            modifier = Modifier
                .size(dp_200)
                .clip(CircleShape), colors = ButtonDefaults.buttonColors(buttonColor)
        ) {
            Text(text = if (isRunning) {
                stringResource(id = R.string.stop)
            } else {
                stringResource(id = R.string.start)
            }, fontSize = sp_40, modifier = Modifier
                .fillMaxWidth()
                .padding(dp_12),textAlign = TextAlign.Center)
        }
    }
}

fun formatTime(time: Long): String {
    val seconds = time % 60
    val minutes = (time / 60) % 60
    val hours = time / 3600
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}