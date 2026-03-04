package com.example.toyrobot.domain.usecase

import com.example.toyrobot.domain.model.Robot
import com.example.toyrobot.domain.model.RobotResult

/**
 * Rotates the robot 90 degrees to the right without moving it.
 * Ignored if the robot is not placed.
 *
 * @param current The current robot state.
 * @return [RobotResult] with the rotated [Robot] and success=true, or
 *         the unchanged [current] and success=false if not placed.
 */
class TurnRightUseCase {
    operator fun invoke(current: Robot?): RobotResult {
        if (current == null) return RobotResult(robot = null, success = false)
        return RobotResult(robot = current.copy(facing = current.facing.turnRight()), success = true)
    }
}
