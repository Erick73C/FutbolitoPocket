package com.example.futbolitopocket.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.futbolitopocket.data.GameState

class GameViewModel : ViewModel() {

    var gameState = mutableStateOf(GameState())
        private set

    private val ballRadius = 30f

    fun updateBall(
        accelX: Float,
        accelY: Float,
        fieldWidth: Float,
        fieldHeight: Float
    ) {
        val current = gameState.value

        var velocityX = current.velocityX + (-accelX * 0.5f)
        var velocityY = current.velocityY + (accelY * 0.5f)

        var ballX = current.ballX + velocityX
        var ballY = current.ballY + velocityY

        // rebote paredes
        if (ballX < ballRadius || ballX > fieldWidth - ballRadius) {
            velocityX *= -1
        }

        if (ballY < ballRadius || ballY > fieldHeight - ballRadius) {
            velocityY *= -1
        }

        ballX = ballX.coerceIn(ballRadius, fieldWidth - ballRadius)
        ballY = ballY.coerceIn(ballRadius, fieldHeight - ballRadius)

        gameState.value = current.copy(
            ballX = ballX,
            ballY = ballY,
            velocityX = velocityX * 0.98f,
            velocityY = velocityY * 0.98f
        )
    }
}