package com.example.futbolitopocket.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.futbolitopocket.viewmodel.GameViewModel
import dev.ricknout.composesensors.accelerometer.rememberAccelerometerSensorValueAsState

@Composable
fun GameScreen(vm: GameViewModel = viewModel()) {

    val sensorValue by rememberAccelerometerSensorValueAsState()

    val (accelX, accelY, accelZ) = sensorValue.value

    val state = vm.gameState.value

    Canvas(modifier = Modifier.fillMaxSize()) {

        // Inicializar pelota en centro
//        if (state.ballX == 0f && state.ballY == 0f) {
//            vm.initializeBall(size.width, size.height)
//        }

        vm.updateBall(
            accelX = accelX,
            accelY = accelY,
            fieldWidth = size.width,
            fieldHeight = size.height
        )

        // Dibujar cancha
        drawRect(
            color = Color(0xFF2E7D32)
        )

        // línea central
        drawLine(
            color = Color.White,
            start = Offset(size.width / 2, 0f),
            end = Offset(size.width / 2, size.height),
            strokeWidth = 8f
        )

        // círculo central
        drawCircle(
            color = Color.White,
            radius = 120f,
            center = Offset(size.width / 2, size.height / 2),
            style = Stroke(width = 6f)
        )

        // Dibujar obstáculos
        vm.obstacles.forEach {

            drawRect(
                color = Color.Gray,
                topLeft = Offset(it.x, it.y),
                size = Size(it.width, it.height)
            )

        }

        // Dibujar pelota
        drawCircle(
            color = Color.Red,
            radius = 30f,
            center = Offset(state.ballX, state.ballY)
        )
    }
}