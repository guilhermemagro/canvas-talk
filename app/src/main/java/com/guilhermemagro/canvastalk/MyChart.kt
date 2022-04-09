package com.guilhermemagro.canvastalk

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

class MyChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val gridLinesNumber = 3
    private var unitaryHeight = 0F
    private var unitaryWidth = 0F
    private var gridLinesPoints: List<Pair<PointF, PointF>>? = null
    private var chartElements: List<ChartElement>? = null

    private val gridPaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.chart_grid_lines)
        style = Paint.Style.STROKE
        strokeWidth = resources.getDimension(R.dimen.chart_grid_lines_stroke)
    }

    private val valuesPaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.chart_values_line)
        style = Paint.Style.STROKE
        strokeWidth = resources.getDimension(R.dimen.chart_values_line_stroke)
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    var chartData: List<ChartData>? = null
        set(value) {
            field = value
            setUnitaryValues()
            recalculateChartElements()
            invalidate()
        }

    private fun setUnitaryValues() {
        val maxValue = chartData?.maxOfOrNull { it.value * 1.2F } ?: 0F
        unitaryHeight = height / maxValue
        unitaryWidth = width / (chartData?.size?.toFloat() ?: 1F)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.e("MyChart", "onSizeChanged called")
        recalculateChartElements()
    }

    private fun recalculateChartElements() {
        generateGridLinesPoints()
        generateChartElements()
    }

    private fun generateGridLinesPoints() {
        val gridLinesOffset = (height.toFloat() / (gridLinesNumber + 1).toFloat())
        val gridLines = mutableListOf<Pair<PointF, PointF>>()
        for (lineNumber in 0..gridLinesNumber) {
            val startPoint = PointF(0F, lineNumber * gridLinesOffset)
            val endPoint = PointF(width.toFloat(), lineNumber * gridLinesOffset)
            gridLines.add(startPoint to endPoint)
        }
        gridLinesPoints = gridLines
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    private fun generateChartElements() {
        val elements = mutableListOf<ChartElement>()
        chartData?.let {
            it.forEachIndexed { index, data ->
                elements.add(
                    ChartElement(
                        chartData = data,
                        pathPoint = PointF(
                            (unitaryWidth * index) + (unitaryWidth / 2),
                            height - (data.value * unitaryHeight)
                        ),
                        containerRect = RectF(
                            unitaryWidth * index,
                            0F,
                            unitaryWidth * (index + 1),
                            height.toFloat()
                        )
                    )
                )
            }
        }
        chartElements = elements
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        Log.e("MyChart", "onMeasure called")
//    }
//
//    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, left, top, right, bottom)
//        Log.e("MyChart", "onLayout called")
//    }

    override fun onDraw(canvas: Canvas?) {
        Log.e("MyChart", "onDraw called")
        canvas?.let {
            it.drawGrid()
            it.drawValuesPath()
            // TODO - Desenhar retângulo com o title e description e um círculo em volta do path
        }
    }

    private fun Canvas.drawGrid() {
        drawRect(0F, 0F, width.toFloat(), height.toFloat(), gridPaint)
        gridLinesPoints?.let {
            for (linePoints in it) {
                drawLine(
                    linePoints.first.x, linePoints.first.y,
                    linePoints.second.x, linePoints.second.y,
                    gridPaint
                )
            }
        }
        // TODO - Desenhar valores ao lado das linhas de grid
    }

    private fun Canvas.drawValuesPath() {
        val valuesPath = Path()
        chartElements?.let {
            it.forEachIndexed { index, element ->
                if (index == 0) {
                    valuesPath.moveTo(element.pathPoint.x, element.pathPoint.y)
                } else {
                    valuesPath.lineTo(element.pathPoint.x, element.pathPoint.y)
                }
            }
            drawPath(valuesPath, valuesPaint)
        }
    }
}
