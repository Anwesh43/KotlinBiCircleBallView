package com.example.bicircleballview

/**
 * Created by anweshmishra on 05/05/18.
 */

import android.content.*
import android.graphics.*
import android.view.*

class BiCircleBallView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

}