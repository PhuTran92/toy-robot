package com.example.toyrobot.domain.model

/**
 * Immutable robot updated result.
 *
 * @param robot   The new robot state after the operation, or null if not yet placed.
 * @param success True if the command was applied; false if it was ignored (e.g. out-of-bounds).
 */
data class RobotResult(
    val robot: Robot?,
    val success: Boolean
)

