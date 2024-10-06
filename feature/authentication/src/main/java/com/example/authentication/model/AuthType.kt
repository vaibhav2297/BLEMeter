package com.example.authentication.model

import com.example.designsystem.icons.AppIcon
import com.example.designsystem.icons.AppIcons

internal enum class AuthType(
    val title: String
) {
    LOGIN_WITH_EMAIL("Login"),
    SIGNUP_WITH_EMAIL("SignUp");


    fun getIcon(): AppIcon {
        return when (this) {
            LOGIN_WITH_EMAIL -> AppIcon.DrawableResourceIcon(AppIcons.LoginLogo)
            SIGNUP_WITH_EMAIL -> AppIcon.DrawableResourceIcon(AppIcons.SignUpLogo)
        }
    }

    fun isLogin(): Boolean = this == LOGIN_WITH_EMAIL
}