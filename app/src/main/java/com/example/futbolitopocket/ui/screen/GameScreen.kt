package com.example.futbolitopocket.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.futbolitopocket.viewmodel.GameViewModel
import dev.ricknout.composesensors.accelerometer.rememberAccelerometerSensorValueAsState

@Composable
fun GameScreen(vm: GameViewModel = viewModel()) {

    val sensorValue by rememberAccelerometerSensorValueAsState()
    val (accelX, accelY, accelZ) = sensorValue.value

    val state = vm.gameState.value

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // MARCADOR SUPERIOR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.DarkGray),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Jugador Arriba: ${state.scoreTop}",
                color = Color.White,
                fontSize = 18.sp
            )

            Text(
                text = "Jugador Abajo: ${state.scoreBottom}",
                color = Color.White,
                fontSize = 18.sp
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Canvas(
                modifier = Modifier
                    .fillMaxWidth(0.85f)  // cancha más centrada
                    .fillMaxHeight(0.9f)
            ) {

                val goalWidth = size.width * 0.3f
                val goalLeft = (size.width - goalWidth) / 2

                // Inicializar pelota en centro
                if (state.ballX == 0f && state.ballY == 0f) {
                    vm.initializeGame(size.width, size.height) // Usa esta nueva función
                }

                vm.updateBall(
                    accelX = accelX,
                    accelY = accelY,
                    fieldWidth = size.width,
                    fieldHeight = size.height
                )

                // 🟩 CANCHA
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

                // Obstáculos
                vm.obstacles.forEach {

                    drawRect(
                        color = Color.LightGray,
                        topLeft = Offset(it.x, it.y),
                        size = Size(it.width, it.height)
                    )
                }

                // ⚽ Pelota
                drawCircle(
                    color = Color.Red,
                    radius = 25f,
                    center = Offset(state.ballX, state.ballY)
                )

                // Portería superior
                drawRect(
                    color = Color.White,
                    topLeft = Offset(goalLeft, 0f),
                    size = Size(goalWidth, 20f)
                )

                // Portería inferior
                drawRect(
                    color = Color.White,
                    topLeft = Offset(goalLeft, size.height - 20f),
                    size = Size(goalWidth, 20f)
                )
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color.DarkGray),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(onClick = { }) {
                Text("◀")
            }

            Button(onClick = { }) {
                Text("♪")
            }

            Button(onClick = { }) {
                Text("II")
            }
        }
    }
}