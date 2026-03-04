package com.example.toyrobot.presentation.ui

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.core.content.ContextCompat
import com.example.toyrobot.R
import com.example.toyrobot.domain.model.Constants
import com.example.toyrobot.domain.model.Direction
import com.example.toyrobot.domain.model.Robot

@Composable
fun TableGridLayout(
    robot: Robot?,
    modifier: Modifier = Modifier,
    @DrawableRes robotResId: Int = R.drawable.ic_robot,
    @ColorRes backgroundColorResId: Int = R.color.white,
) {
    val context = LocalContext.current
    val robotDrawable = remember { ContextCompat.getDrawable(context, robotResId) }
    val gridPadding = dimensionResource(R.dimen.grid_padding)
    val cellBorderWidth = dimensionResource(R.dimen.grid_cell_border_width)

    Canvas(
        modifier = modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .padding(gridPadding)
            .background(colorResource(backgroundColorResId))
    ) {
        val cellSize = size.width / Constants.TABLE_SIZE

        for (col in 0 until Constants.TABLE_SIZE) {
            for (row in 0 until Constants.TABLE_SIZE) {
                val tableY = Constants.TABLE_SIZE - 1 - row
                val isRobotHere = robot != null && robot.x == col && robot.y == tableY

                val left = col * cellSize
                val top = row * cellSize

                // Cell border
                drawRect(
                    color = Color(0xFFBDBDBD),
                    topLeft = Offset(left, top),
                    size = Size(cellSize, cellSize),
                    style = Stroke(width = cellBorderWidth.toPx())
                )

                // Draw robot icon if robot is here
                if (isRobotHere && robotDrawable != null) {
                    drawRobotIcon(
                        cellLeft = left,
                        cellTop = top,
                        cellSize = cellSize,
                        direction = robot.facing,
                        drawable = robotDrawable
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawRobotIcon(
    cellLeft: Float,
    cellTop: Float,
    cellSize: Float,
    direction: Direction,
    drawable: android.graphics.drawable.Drawable
) {
    val padding = cellSize * 0.1f
    val iconSize = (cellSize - padding * 2).toInt()
    val left = (cellLeft + padding).toInt()
    val top = (cellTop + padding).toInt()

    drawable.setBounds(left, top, left + iconSize, top + iconSize)

    // Rotate to match the facing direction
    // The drawable is drawn facing NORTH by default (arrow points up)
    val angle = when (direction) {
        Direction.NORTH -> 0f
        Direction.EAST -> 90f
        Direction.SOUTH -> 180f
        Direction.WEST -> 270f
    }

    val centerX = cellLeft + cellSize / 2f
    val centerY = cellTop + cellSize / 2f

    rotate(degrees = angle, pivot = Offset(centerX, centerY)) {
        drawable.draw(drawContext.canvas.nativeCanvas)
    }
}
