package com.example.futbolitopocket.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.futbolitopocket.data.GameState
import com.example.futbolitopocket.data.Obstacle

class GameViewModel : ViewModel() {

    val obstacles = listOf(

        // zona superior
        Obstacle(200f, 200f, 80f, 40f),
        Obstacle(500f, 200f, 80f, 40f),
        Obstacle(800f, 200f, 80f, 40f),

        Obstacle(150f, 350f, 80f, 40f),
        Obstacle(450f, 350f, 80f, 40f),
        Obstacle(750f, 350f, 80f, 40f),

        // zona media
        Obstacle(250f, 600f, 80f, 40f),
        Obstacle(550f, 600f, 80f, 40f),

        Obstacle(150f, 800f, 80f, 40f),
        Obstacle(450f, 800f, 80f, 40f),
        Obstacle(750f, 800f, 80f, 40f),

        // zona inferior
        Obstacle(200f, 1050f, 80f, 40f),
        Obstacle(500f, 1050f, 80f, 40f),
        Obstacle(800f, 1050f, 80f, 40f),

        Obstacle(150f, 1200f, 80f, 40f),
        Obstacle(450f, 1200f, 80f, 40f),
        Obstacle(750f, 1200f, 80f, 40f)
    )
    var gameState = mutableStateOf(GameState())
        private set

    private val ballRadius = 30f

    fun initializeBall(fieldWidth: Float, fieldHeight: Float) {

        val centerX = fieldWidth / 2
        val centerY = fieldHeight / 2

        gameState.value = gameState.value.copy(
            ballX = centerX,
            ballY = centerY
        )
    }

    fun updateBall(
        accelX: Float,
        accelY: Float,
        fieldWidth: Float,
        fieldHeight: Float
    ) {

        val current = gameState.value

        var velocityX = current.velocityX + (-accelX * 0.6f)
        var velocityY = current.velocityY + (accelY * 0.6f)

        var ballX = current.ballX + velocityX
        var ballY = current.ballY + velocityY

        // Rebote en paredes
        if (ballX < ballRadius || ballX > fieldWidth - ballRadius) {
            velocityX *= -1
        }

        if (ballY < ballRadius || ballY > fieldHeight - ballRadius) {
            velocityY *= -1
        }

        ballX = ballX.coerceIn(ballRadius, fieldWidth - ballRadius)
        ballY = ballY.coerceIn(ballRadius, fieldHeight - ballRadius)
        // Colisión con obstáculos
        for (obstacle in obstacles) {

            val left = obstacle.x
            val right = obstacle.x + obstacle.width
            val top = obstacle.y
            val bottom = obstacle.y + obstacle.height

            if (
                ballX + ballRadius > left &&
                ballX - ballRadius < right &&
                ballY + ballRadius > top &&
                ballY - ballRadius < bottom
            ) {

                // Rebote simple
                velocityX *= -1
                velocityY *= -1

                // Empujar pelota fuera del obstáculo
                ballX += velocityX
                ballY += velocityY
            }
        }

        gameState.value = current.copy(
            ballX = ballX,
            ballY = ballY,
            velocityX = velocityX * 0.98f,
            velocityY = velocityY * 0.98f
        )
    }
}