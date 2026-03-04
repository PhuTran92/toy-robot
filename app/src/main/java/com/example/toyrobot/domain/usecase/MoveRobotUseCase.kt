package com.example.toyrobot.domain.usecase

import com.example.toyrobot.domain.model.Direction
import com.example.toyrobot.domain.model.Robot
import com.example.toyrobot.domain.model.RobotResult

/**
 * Moves the robot one unit forward in the direction it is currently facing.
 * Ignored if the robot is not placed or would fall off the table.
 *
 * @param current The current robot state.
 * @return [RobotResult] with the updated [Robot] and success=true, or
 *         the unchanged [current] and success=false if ignored.
 */
class MoveRobotUseCase(
    private val validatePosition: ValidatePositionUseCase
) {
    operator fun invoke(current: Robot?): RobotResult {
        if (current == null) return RobotResult(robot = null, success = false)
        val (newX, newY) = when (current.facing) {
            Direction.NORTH -> current.x to current.y + 1
            Direction.EAST  -> current.x + 1 to current.y
            Direction.SOUTH -> current.x to current.y - 1
            Direction.WEST  -> current.x - 1 to current.y
        }
        if (!validatePosition(newX, newY)) return RobotResult(robot = current, success = false)
        return RobotResult(robot = current.copy(x = newX, y = newY), success = true)
    }
}
