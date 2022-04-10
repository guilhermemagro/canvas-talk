package com.guilhermemagro.canvastalk

import android.graphics.PointF
import android.graphics.RectF

data class ChartElement(
    val chartData: ChartData,
    var pathPoint: PointF,
    var containerRect: RectF
) {
    fun contains(x: Float, y: Float) = containerRect.contains(x, y)
}
