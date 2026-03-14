package com.example.futbolitopocket.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.futbolitopocket.data.GameState
import com.example.futbolitopocket.data.Obstacle

class GameViewModel : ViewModel() {

    var obstacles = listOf<Obstacle>()
        private set
    var gameState = mutableStateOf(GameState())
        private set

    private val ballRadius = 25f

    fun initializeGame(fieldWidth: Float, fieldHeight: Float) {
        val goalWidth = fieldWidth * 0.3f
        val goalLeft = (fieldWidth - goalWidth) / 2
        val goalRight = goalLeft + goalWidth

        // Generamos obstáculos proporcionales
        obstacles = listOf(
            // --- BORDES SUPERIORES (Dejan el hueco de la portería) ---
            Obstacle(0f, 0f, goalLeft - 10f, 20f),
            Obstacle(goalRight + 10f, 0f, fieldWidth - goalRight, 20f),

            // --- BORDES INFERIORES ---
            Obstacle(0f, fieldHeight - 20f, goalLeft - 10f, 20f),
            Obstacle(goalRight + 10f, fieldHeight - 20f, fieldWidth - goalRight, 20f),

            // --- DETALLES LATERALES (Los pasillos de la imagen) ---
            Obstacle(fieldWidth * 0.1f, fieldHeight * 0.15f, 25f, fieldHeight * 0.25f), // Izq arriba
            Obstacle(fieldWidth * 0.85f, fieldHeight * 0.15f, 25f, fieldHeight * 0.25f), // Der arriba
            Obstacle(fieldWidth * 0.1f, fieldHeight * 0.6f, 25f, fieldHeight * 0.25f),  // Izq abajo
            Obstacle(fieldWidth * 0.85f, fieldHeight * 0.6f, 25f, fieldHeight * 0.25f),  // Der abajo

            // --- BLOQUES CENTRALES LATERALES ---
            Obstacle(0f, fieldHeight / 2 - 60f, fieldWidth * 0.15f, 30f),
            Obstacle(fieldWidth * 0.85f, fieldHeight / 2 - 60f, fieldWidth * 0.15f, 30f),
            Obstacle(0f, fieldHeight / 2 + 30f, fieldWidth * 0.15f, 30f),
            Obstacle(fieldWidth * 0.85f, fieldHeight / 2 + 30f, fieldWidth * 0.15f, 30f),

            // --- ISLAS CERCA DEL CENTRO (Respetando el círculo) ---
            Obstacle(fieldWidth * 0.25f, fieldHeight * 0.4f, 80f, 30f),
            Obstacle(fieldWidth * 0.65f, fieldHeight * 0.4f, 80f, 30f),
            Obstacle(fieldWidth * 0.25f, fieldHeight * 0.55f, 80f, 30f),
            Obstacle(fieldWidth * 0.65f, fieldHeight * 0.55f, 80f, 30f),

            // --- DEFENSAS DE PORTERÍA ---
            Obstacle(fieldWidth / 2 - 40f, fieldHeight * 0.2f, 80f, 25f), // Defensa arriba
            Obstacle(fieldWidth / 2 - 40f, fieldHeight * 0.75f, 80f, 25f) // Defensa abajo
        )

        initializeBall(fieldWidth, fieldHeight)
    }

    fun initializeBall(fieldWidth: Float, fieldHeight: Float) {
        gameState.value = gameState.value.copy(
            ballX = fieldWidth / 2,
            ballY = fieldHeight / 2,
            velocityX = 0f,
            velocityY = 0f
        )
    }

    fun updateBall(
        accelX: Float,
        accelY: Float,
        fieldWidth: Float,
        fieldHeight: Float
    ) {
        val goalWidth = fieldWidth * 0.3f
        val goalLeft = (fieldWidth - goalWidth) / 2
        val goalRight = goalLeft + goalWidth
        val current = gameState.value

        // 1. Calculamos la velocidad actual sumando la aceleración
        var newVelocityX = current.velocityX + (-accelX * 0.6f)
        var newVelocityY = current.velocityY + (accelY * 0.6f)

        // 2. Calculamos la posición a la que intenta ir la pelota
        var newBallX = current.ballX + newVelocityX
        var newBallY = current.ballY + newVelocityY

        // 3. Rebote en paredes laterales (Eje X)
        if (newBallX < ballRadius || newBallX > fieldWidth - ballRadius) {
            newVelocityX *= -0.8f
            newBallX = current.ballX + newVelocityX
        }

        // 4. Lógica de Goles y Rebote en bordes (Eje Y)
        if (newBallY < ballRadius) {
            if (newBallX > goalLeft && newBallX < goalRight) {
                gameState.value = current.copy(scoreBottom = current.scoreBottom + 1)
                initializeBall(fieldWidth, fieldHeight)
                return
            } else {
                newVelocityY *= -0.8f
                newBallY = current.ballY + newVelocityY
            }
        } else if (newBallY > fieldHeight - ballRadius) {
            if (newBallX > goalLeft && newBallX < goalRight) {
                gameState.value = current.copy(scoreTop = current.scoreTop + 1)
                initializeBall(fieldWidth, fieldHeight)
                return
            } else {
                newVelocityY *= -0.8f
                newBallY = current.ballY + newVelocityY
            }
        }

        // 5. COLISIÓN CON OBSTÁCULOS
        for (obstacle in obstacles) {
            if (newBallX + ballRadius > obstacle.x &&
                newBallX - ballRadius < obstacle.x + obstacle.width &&
                newBallY + ballRadius > obstacle.y &&
                newBallY - ballRadius < obstacle.y + obstacle.height) {

                // Si hay choque, invertimos velocidad
                newVelocityX *= -0.7f
                newVelocityY *= -0.7f

                // Movemos la pelota fuera del obstáculo para que no se quede pegada
                newBallX = current.ballX + newVelocityX
                newBallY = current.ballY + newVelocityY
            }
        }

        //ACTUALIZAR ESTADO
        gameState.value = current.copy(
            ballX = newBallX.coerceIn(ballRadius, fieldWidth - ballRadius),
            ballY = newBallY.coerceIn(ballRadius, fieldHeight - ballRadius),
            velocityX = newVelocityX * 0.98f, // Fricción
            velocityY = newVelocityY * 0.98f
        )
    }
}