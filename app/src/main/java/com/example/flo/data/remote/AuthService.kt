package com.example.flo.data.remote

import android.util.Log
import com.example.flo.ui.login.AutoLoginView
import com.example.flo.ui.login.LoginView
import com.example.flo.ui.signup.SignUpView
import com.example.flo.data.entities.User
import com.example.flo.utils.getRetrofit
import retrofit2.*
class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView
    private lateinit var autoView : AutoLoginView
    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }
    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    fun setAutoLoginView( autoView : AutoLoginView){
        this.autoView = autoView
    }

    fun signUp(user : User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.signUp(user).enqueue(object:Callback<AuthResponse>{
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!
                Log.d("res", resp.toString())

                when(resp.code) {
                    1000->signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure(resp)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE", t.message.toString())
            }

        })
        Log.d("SIGNUP", "HELLO")
    }

    fun login(user : User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.login(user).enqueue(object:Callback<AuthResponse>{
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGIN/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!
                Log.d("LOGIN/RES", resp.toString())
                when(val code = resp.code) {
                    1000->loginView.onLoginSuccess(code, resp.result!!)
                    else ->loginView.onLoginFailure(resp)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGIN/FAILURE", t.message.toString())
            }

        })
        Log.d("LOGIN", "HELLO")
    }

    fun auto_login(header : String){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        Log.d("header", header)

        authService.auto_login(headerValue = header).enqueue(object:Callback<AuthResponse>{
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGIN/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!
                Log.d("LOGIN/RES", resp.toString())
                when(val code = resp.code) {
                    1000->autoView.onAutoLoginSuccess(resp)
                    else ->autoView.onAutoLoginFailure(resp)
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGIN/FAILURE", t.message.toString())
            }
        })
    }
}