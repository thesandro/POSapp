package com.possystem.posapp.ui.activities.splash_screen

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.possystem.posapp.ui.MainActivity
import com.possystem.posapp.R
import com.possystem.posapp.data.user_preference.UserPreference
import com.possystem.posapp.ui.activities.authentication.log_in.LogInActivity


class SplashActivity : AppCompatActivity() {
    private val handler = Handler()
    private val runnable = Runnable { init() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    private fun init() {
        val activity: Activity = if (UserPreference.getString(UserPreference.SESSION)!!.isEmpty())
            LogInActivity()
        else
            MainActivity()
        val intent = Intent(this, activity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }


    override fun onStart() {
        super.onStart()
        postDelayed()
    }

    override fun onStop() {
        super.onStop()
        removeCallBack()
    }

    private fun postDelayed() {
        handler.postDelayed(runnable, 700)
    }

    private fun removeCallBack() {
        handler.removeCallbacks(runnable)
    }

}
