package com.possystem.posapp.tools

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Patterns
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.possystem.posapp.R
import kotlinx.android.synthetic.main.error_dialog_layout.*

object Tools {
    fun isEmailValid(text: String) = Patterns.EMAIL_ADDRESS.matcher(text).matches()


}