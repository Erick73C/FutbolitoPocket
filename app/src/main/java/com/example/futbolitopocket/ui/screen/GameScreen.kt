package com.example.futbolitopocket.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.futbolitopocket.viewmodel.GameViewModel
import dev.ricknout.composesensors.accelerometer.rememberAccelerometerSensorValueAsState

@Composable
fun GameScreen(vm: GameViewModel = viewModel()) {

    val sensorValue by rememberAccelerometerSensorValueAsState()
    val (x, y, z) = sensorValue.value

    val state = vm.gameState.value

    Canvas(modifier = Modifier.fillMaxSize()) {

        vm.updateBall(
            accelX = x,
            accelY = y,
            fieldWidth = size.width,
            fieldHeight = size.height
        )

        drawCircle(
            color = Color.Red,
            radius = 30f,
            center = androidx.compose.ui.geometry.Offset(
                state.ballX,
                state.ballY
            )
        )
    }
}