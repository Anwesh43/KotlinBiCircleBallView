package com.example.bicircleballview

/**
 * Created by anweshmishra on 05/05/18.
 */

import android.app.Activity
import android.content.*
import android.graphics.*
import android.view.*

class BiCircleBallView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val bcbRenderer : BCBRenderer = BCBRenderer(this)

    override fun onDraw(canvas : Canvas) {
        bcbRenderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                bcbRenderer.handleTap()
            }
        }
        return true
    }

    data class BCBState(var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0) {

        val scales : Array<Float> = arrayOf(0f, 0f, 0f, 0f)

        fun update(stopcb : (Float) -> Unit) {
            scales[j] += dir * 0.1f
            if (Math.abs(prevScale - scales[j]) > 1) {
                scales[j] = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class BCBAnimator(var view : View, var animated : Boolean = false) {

        fun animate(updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class BiCircleBall(var i : Int, private val state : BCBState = BCBState()) {

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            paint.color = Color.parseColor("#4CAF50")
            paint.strokeWidth = Math.min(w, h) / 60
            paint.strokeCap = Paint.Cap.ROUND
            val size : Float = Math.min(w, h)/ 3
            val r : Float = Math.min(w, h)/10
            canvas.save()
            canvas.translate(w/2, h/2)
            for (i in 0..1) {
                val x : Float = -size * state.scales[1] * state.scales[3]
                canvas.save()
                canvas.scale(1f - 2 * i,1f)
                canvas.rotate(90f * state.scales[2])
                canvas.drawLine(0f, 0f, x, 0f, paint)
                canvas.drawCircle(x, 0f, r * state.scales[0], paint)
                canvas.restore()
            }
            canvas.restore()
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }

    data class BCBRenderer(var view : BiCircleBallView) {

        private val bcb : BiCircleBall = BiCircleBall(1)

        private val animator : BCBAnimator = BCBAnimator(view)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            bcb.draw(canvas, paint)
            animator.animate {
                bcb.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            bcb.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun render(activity : Activity) : BiCircleBallView {
            val view : BiCircleBallView = BiCircleBallView(activity)
            activity.setContentView(view)
            return view
        }
    }
}