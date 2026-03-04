package com.example.toyrobot.domain.usecase

import com.example.toyrobot.domain.model.Robot

/**
 * Reports the current position and facing of the robot.
 *
 * @param current The current robot state.
 * @return A formatted string like "3,3,NORTH", or null if no robot is placed.
 */
class ReportRobotUseCase {
    operator fun invoke(current: Robot?): String? {
        current ?: return null
        return "${current.x},${current.y},${current.facing.name}"
    }
}
