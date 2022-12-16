package com.alex.dogedex.utils

import android.util.Patterns

fun isValidEmail(email: String?): Boolean {
    //ya existe una función para vilidar un email
    return !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}