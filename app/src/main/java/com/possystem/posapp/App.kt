package com.possystem.posapp

import android.app.Application
import android.content.Context

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
    }

    companion object {
        lateinit var instance: App
        private lateinit var context: Context
    }

    fun getContext() = context
}