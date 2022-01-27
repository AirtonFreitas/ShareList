package com.iasoftwares.sharelist.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.iasoftwares.sharelist.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val mIntent = Intent(this, MainActivity::class.java)
            startActivity(mIntent)
        },3000)
    }
}