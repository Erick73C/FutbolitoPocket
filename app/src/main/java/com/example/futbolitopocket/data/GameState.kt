package com.example.futbolitopocket.data

//Este archivo guardará:
//posición de la pelota
//velocidad
//marcador
data class GameState(

    val ballX: Float = 0f,
    val ballY: Float = 0f,

    val velocityX: Float = 0f,
    val velocityY: Float = 0f,

    val scoreTop: Int = 0,
    val scoreBottom: Int = 0
)
