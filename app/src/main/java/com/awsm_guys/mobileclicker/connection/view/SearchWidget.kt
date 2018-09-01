package com.awsm_guys.mobileclicker.connection.view

import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.awsm_guys.mobileclicker.R
import kotlin.math.max
import kotlin.math.min


class SearchWidget: View {

    private var bitmap: Bitmap? = null
    private val paint = Paint()
    private lateinit var localCanvas: Canvas

    private var searchBackgroundColor: Int = 0xFF222327.toInt()
    private var searchMainColor: Int = 0xFF8A89C0.toInt()

    private var circleRadius: Int = 0
    private var centerX = 0f
    private var centerY = 0f
    private val valueAnimator = ValueAnimator.ofFloat(0f, 2f)
    private var init = true

    init {
        valueAnimator.apply {
            repeatCount = INFINITE
            repeatMode = ValueAnimator.RESTART
            duration = 1000
            interpolator = AccelerateInterpolator()
            addUpdateListener(::redrawBitmap)
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SearchWidget)
        searchBackgroundColor =
                typedArray.getInt(R.styleable.SearchWidget_search_background_color, searchBackgroundColor)
        searchMainColor =
                typedArray.getInt(R.styleable.SearchWidget_search_main_color, searchMainColor)
        typedArray.recycle()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        if (bitmap == null) {
            bitmap = getInitialBitmap()
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        if (init) {
            startAnimation()
            init = false
        }
    }

    private fun getInitialBitmap(): Bitmap =
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            localCanvas = Canvas(this)
        }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        circleRadius = max(width, height)
        centerY = (height / 2).toFloat()
        centerX = (width / 2).toFloat()
    }

    private fun initialDraw(canvas: Canvas) {
        with(canvas) {
            drawBackground(this)
            paint.color = searchMainColor
            drawCircle(
                    centerX,
                    centerY,
                    min(width, height).toFloat() / 15f,
                    paint
            )
        }
    }

    private fun drawBackground(canvas: Canvas){
        paint.color = searchBackgroundColor
        paint.style = Paint.Style.FILL
        canvas.drawRect(Rect(0, 0, width, height), paint)
    }

    private fun redrawBitmap(valueAnimator: ValueAnimator){
        val value = valueAnimator.animatedValue as Float
        if (value == 0f) {
            initialDraw(localCanvas)
        } else {
            drawBackground(localCanvas)
            with(paint){
                color = searchMainColor
                alpha = (0xFF.toFloat() * value).toInt()
                style = Paint.Style.STROKE
                strokeWidth = 30f
                localCanvas.drawCircle(
                        centerX, centerY, circleRadius * value, this
                )
                alpha = 0xFF
            }
        }

        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = getInitialBitmap()
        drawBackground(localCanvas)
    }

    fun startAnimation() {
        valueAnimator.start()
    }

    fun stopAnimation() {
        valueAnimator.pause()
    }
}
