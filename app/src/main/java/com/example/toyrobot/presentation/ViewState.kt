package com.example.toyrobot.presentation

import com.example.toyrobot.domain.model.Robot

data class ViewState(
    val robot: Robot? = null,
    val isPlaced: Boolean = false,
    val commandLog: List<String> = emptyList()
)