package com.example.flo.ui.login

import com.example.flo.data.remote.AuthResponse

interface AutoLoginView {
    fun onAutoLoginSuccess(res : AuthResponse)
    fun onAutoLoginFailure(res : AuthResponse)
}