package com.possystem.posapp.data.user_preference

import android.content.Context
import com.possystem.posapp.App

object UserPreference {

    const val USER_ID = "USER_ID"
    const val TOKEN = "TOKEN"
    const val SESSION = "SESSION"

    private val preference by lazy {
        App.instance.getContext().getSharedPreferences("USER", Context.MODE_PRIVATE)
    }

    private val editor by lazy {
        preference.edit()
    }

    fun saveString(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    fun getString(key: String) = preference.getString(key, "")

    fun removeString(key: String) {
        if (preference.contains(key)) {
            editor.remove(key)
            editor.commit()
        }
    }

    fun clearAll() {
        editor.clear()
        editor.commit()
    }
}