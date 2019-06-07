package com.ramonapp.randomcoins

import android.animation.*
import android.animation.ValueAnimator.ofPropertyValuesHolder
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kotlinx.android.synthetic.main.activity_main.*

class CoinsView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val ROW_COUNT = 5
    private val PADDING = 20f

    private var totalCoins = 0
    private var radius = 0f
    lateinit var arrayX: Array<Float?>
    lateinit var arrayY: Array<Float?>

    private var isWorking = false

    private var paint: Paint = Paint().apply {
        color = Color.BLUE
    }

    public fun createCoins(number: Int) {

        if (isWorking) return
        isWorking = true

        totalCoins = number
        arrayX = arrayOfNulls(totalCoins)
        arrayY = arrayOfNulls(totalCoins)

        val firstXY = width / ROW_COUNT / 2f
        radius = firstXY - PADDING
        var currentX = firstXY
        var currentY = firstXY

        val propertyArrayX = arrayOfNulls<PropertyValuesHolder>(totalCoins)
        val propertyArrayY = arrayOfNulls<PropertyValuesHolder>(totalCoins)
        for (i in 1..totalCoins) {
            propertyArrayX[i - 1] = PropertyValuesHolder.ofFloat("x$i", width / 2f, currentX)
            propertyArrayY[i - 1] = PropertyValuesHolder.ofFloat("y$i", height / 2f, currentY)

            if (i % ROW_COUNT == 0) {
                currentY += firstXY * 2
                currentX = firstXY
                continue
            }
            currentX += firstXY * 2
        }


        ValueAnimator.ofPropertyValuesHolder(*propertyArrayX, *propertyArrayY).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                for (i in 1..totalCoins) {
                    arrayX[i - 1] = it.getAnimatedValue("x$i") as Float
                    arrayY[i - 1] = it.getAnimatedValue("y$i") as Float
                }
                invalidate()
            }
            addListener(object: AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    isWorking = false
                }
            })
            start()
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (i in 1..totalCoins) {
            canvas?.drawCircle(arrayX[i - 1] ?: 0f, arrayY[i - 1] ?: 0f, radius, paint)
        }

    }
}