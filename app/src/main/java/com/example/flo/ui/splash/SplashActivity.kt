package com.example.flo.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.ui.login.AutoLoginView
import com.example.flo.ui.login.LoginActivity
import com.example.flo.data.remote.AuthResponse
import com.example.flo.data.remote.AuthService
import com.example.flo.databinding.ActivitySplashBinding
import com.example.flo.ui.main.MainActivity

class SplashActivity : AppCompatActivity() , AutoLoginView {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spf = getSharedPreferences("auth2", MODE_PRIVATE)
        val preJWT = spf.getString("jwt","")

        val authService = AuthService()
        authService.setAutoLoginView(this)

        authService.auto_login(preJWT!!)

    }

    override fun onAutoLoginSuccess(res: AuthResponse) {
        when(res.code) {
            1000-> {
                val intent = Intent (this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onAutoLoginFailure(res: AuthResponse) {
        val intent = Intent (this, LoginActivity::class.java)
        startActivity(intent)
    }
}