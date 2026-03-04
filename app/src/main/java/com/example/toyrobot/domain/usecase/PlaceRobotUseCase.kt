package com.example.toyrobot.domain.usecase

import com.example.toyrobot.domain.model.Direction
import com.example.toyrobot.domain.model.Robot
import com.example.toyrobot.domain.model.RobotResult

/**
 * Places the robot on the table at the given position and facing direction.
 * Ignored if the position is out of bounds.
 *
 * @param current The current robot state (unused for placement, but kept for API consistency).
 * @return [RobotResult] with the newly placed [Robot] and success=true, or
 *         the unchanged [current] and success=false if out of bounds.
 */
class PlaceRobotUseCase(
    private val validatePosition: ValidatePositionUseCase
) {
    operator fun invoke(current: Robot?, x: Int, y: Int, direction: Direction): RobotResult {
        if (!validatePosition(x, y)) return RobotResult(robot = current, success = false)
        return RobotResult(robot = Robot(x, y, direction), success = true)
    }
}
