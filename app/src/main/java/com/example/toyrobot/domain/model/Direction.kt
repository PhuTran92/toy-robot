package com.example.toyrobot.domain.model

enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    fun turnLeft(): Direction = when (this) {
        NORTH -> WEST
        WEST  -> SOUTH
        SOUTH -> EAST
        EAST  -> NORTH
    }

    fun turnRight(): Direction = when (this) {
        NORTH -> EAST
        EAST  -> SOUTH
        SOUTH -> WEST
        WEST  -> NORTH
    }
}

