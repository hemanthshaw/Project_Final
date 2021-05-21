package com.rationwala.store

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_screen2.*


@Suppress("DEPRECATION")
class Screen2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen2)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        next.setOnClickListener {
            startActivity(
                Intent(this@Screen2,Screen3::class.java)
            )
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);

        }
    }
}