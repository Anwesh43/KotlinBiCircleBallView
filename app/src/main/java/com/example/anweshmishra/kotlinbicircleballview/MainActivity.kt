package com.example.anweshmishra.kotlinbicircleballview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.bicircleballview.BiCircleBallView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BiCircleBallView.render(this)
    }
}
