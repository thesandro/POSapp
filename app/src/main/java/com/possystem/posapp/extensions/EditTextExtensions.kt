package com.possystem.posapp.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.possystem.posapp.tools.Tools

fun EditText.isEmailValid(context: Context) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            var drawable: Drawable? = null
            if (Tools.isEmailValid(p0.toString())) {
                //drawable = ContextCompat.getDrawable(context, R.mipmap.ic_success)
                tag = "1"
            }else
                tag = "0"
            setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        }
    })
}

