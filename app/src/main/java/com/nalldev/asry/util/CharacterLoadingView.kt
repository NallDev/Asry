package com.nalldev.asry.util

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.core.content.res.ResourcesCompat
import com.nalldev.asry.R

class CharacterLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val characters = listOf('A', 'R', 'S', 'Y')
    private var currentIndex = 0
    private var isLoading = false
    private var textScale = 1f

    private val backgroundPaint = Paint().apply {
        color = context.getColor(R.color.background_loading)
    }

    private val textAnimator = ValueAnimator.ofFloat(0.5f, 1.5f).apply {
        duration = 500
        interpolator = BounceInterpolator()
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        addUpdateListener { animation ->
            textScale = animation.animatedValue as Float
            invalidate()
        }
    }

    private val characterPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.color_primary)
        textSize = 100f
        textAlign = Paint.Align.CENTER
        typeface = ResourcesCompat.getFont(context, R.font.space_grotesk_bold)
    }

    private val handler = Handler(Looper.getMainLooper())
    private val loadingRunnable = Runnable { startLoading() }

    init {
        isClickable = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        val character = characters[currentIndex]
        val xPos = (width / 2).toFloat()
        val yPos = (height / 2 - (characterPaint.descent() + characterPaint.ascent()) / 2)

        canvas.save()
        canvas.scale(textScale, textScale, xPos, yPos)
        canvas.drawText(character.toString(), xPos, yPos, characterPaint)
        canvas.restore()
    }

    private fun startLoading() {
        if (!isLoading) return

        currentIndex = (currentIndex + 1) % characters.size
        textAnimator.start()

        handler.postDelayed(loadingRunnable, 500)
    }

    private fun stopLoading() {
        isLoading = false
        currentIndex = 3
        textAnimator.cancel()
        handler.removeCallbacks(loadingRunnable)
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE) {
            isLoading = true
            startLoading()
        } else {
            stopLoading()
        }
    }
}