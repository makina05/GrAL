package com.example.androidgral

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        intent = Intent(this,AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}