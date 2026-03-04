package com.example.toyrobot.domain.usecase

import com.example.toyrobot.domain.model.Constants

/**
 * Validates whether the given (x, y) position falls within the 5×5 table bounds.
 * Returns true if the position is valid.
 */
class ValidatePositionUseCase {
    operator fun invoke(x: Int, y: Int): Boolean =
        x in 0 until Constants.TABLE_SIZE && y in 0 until Constants.TABLE_SIZE
}
