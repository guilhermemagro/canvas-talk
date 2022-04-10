package com.guilhermemagro.canvastalk

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

private const val GRID_LINES_NUMBER = 3

class MyChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var unitaryGridHeight = 0F
    private var unitaryHeight = 0F
    private var unitaryWidth = 0F
    private var gridLinesPoints: List<Pair<PointF, PointF>>? = null
    private var chartElements: List<ChartElement>? = null

    private val gridLinesPaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.chart_grid_lines)
        style = Paint.Style.STROKE
        strokeWidth = resources.getDimension(R.dimen.chart_grid_lines_stroke)
    }

    private val gridTextPaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.chart_grid_lines)
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        textSize = resources.getDimension(R.dimen.chart_description_text_size)
        textAlign = Paint.Align.LEFT
    }

    private val valuesPathPaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.chart_values_line)
        style = Paint.Style.STROKE
        strokeWidth = resources.getDimension(R.dimen.chart_values_line_stroke)
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val valuesCirclePaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.chart_values_line)
        style = Paint.Style.FILL
    }

    private val descriptionBackgroundPaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.chart_values_description_background)
        style = Paint.Style.FILL
    }

    private val descriptionTextPaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.black)
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        textSize = resources.getDimension(R.dimen.chart_description_text_size)
        textAlign = Paint.Align.CENTER
    }

    private var selectedValue: ChartElement? = null
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
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
        unitaryGridHeight = maxValue / (GRID_LINES_NUMBER + 1)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        recalculateChartElements()
    }

    private fun recalculateChartElements() {
        generateGridLinesPoints()
        generateChartElements()
    }

    private fun generateGridLinesPoints() {
        val gridLinesOffset = (height.toFloat() / (GRID_LINES_NUMBER + 1).toFloat())
        val gridLines = mutableListOf<Pair<PointF, PointF>>()
        for (lineNumber in 1..GRID_LINES_NUMBER) {
            val startPoint = PointF(0F, lineNumber * gridLinesOffset)
            val endPoint = PointF(width.toFloat(), lineNumber * gridLinesOffset)
            gridLines.add(startPoint to endPoint)
        }
        gridLinesPoints = gridLines
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

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.drawGrid()
            it.drawValuesPath()
            if (selectedValue != null) {
                it.drawSelectionView()
            }
        }
    }

    private fun Canvas.drawGrid() {
        drawRect(0F, 0F, width.toFloat(), height.toFloat(), gridLinesPaint)
        gridLinesPoints?.let {
            it.forEachIndexed { index, linePoints ->
                drawLine(
                    linePoints.first.x, linePoints.first.y,
                    linePoints.second.x, linePoints.second.y,
                    gridLinesPaint
                )
                if (unitaryGridHeight > 0) {
                    drawText(
                        ((GRID_LINES_NUMBER) * unitaryGridHeight - ((index) * unitaryGridHeight)).toString(),
                        linePoints.first.x,
                        linePoints.first.y,
                        gridTextPaint
                    )
                    drawText("0.0", 0F, height.toFloat(), gridTextPaint)
                }
            }
        }
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
            drawPath(valuesPath, valuesPathPaint)
        }
    }

    private fun Canvas.drawSelectionView() {
        selectedValue?.let { selected ->
            // Circle
            drawCircle(
                selected.pathPoint.x, selected.pathPoint.y,
                resources.getDimension(R.dimen.chart_selection_circle_radius),
                valuesCirclePaint
            )

            // Background
            val titleTextBound = Rect()
            val descriptionTextBound = Rect()
            descriptionTextPaint.getTextBounds(
                selected.chartData.title,
                0,
                selected.chartData.title.length,
                titleTextBound
            )
            descriptionTextPaint.getTextBounds(
                selected.chartData.description,
                0,
                selected.chartData.description.length,
                descriptionTextBound
            )
            titleTextBound.union(descriptionTextBound)
            val resultRect = RectF(
                (titleTextBound.left - 20).toFloat(),
                (titleTextBound.top * 2 - 20).toFloat(),
                (titleTextBound.right + 20).toFloat(),
                (titleTextBound.bottom + 20).toFloat()
            )
            resultRect.offsetTo(
                selected.pathPoint.x - (resultRect.width() / 2),
                selected.pathPoint.y - resultRect.height() - 20
            )
            drawRoundRect(resultRect, 10F, 10F, descriptionBackgroundPaint)

            // Text
            drawText(
                selected.chartData.description,
                resultRect.centerX().toFloat(),
                resultRect.centerY().toFloat(),
                descriptionTextPaint
            )
            drawText(
                selected.chartData.title,
                resultRect.centerX().toFloat(),
                resultRect.bottom.toFloat() - 20,
                descriptionTextPaint
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val selected = chartElements?.firstOrNull { it.contains(event.x, event.y) }
                selected?.let {
                    selectedValue = it
                }
            }
            MotionEvent.ACTION_UP -> {
                selectedValue = null
            }
        }
        return true
    }
}
