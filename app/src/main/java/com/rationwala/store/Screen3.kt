package com.rationwala.store

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_screen3.*


@Suppress("DEPRECATION")
class Screen3: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen3)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        next.setOnClickListener {
            startActivity(
                Intent(this@Screen3,MainActivity::class.java)
            )
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);

        }
    }
}