package com.possystem.posapp.ui.activities.authentication.log_in

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.possystem.posapp.R
import com.possystem.posapp.extensions.isEmailValid
import com.possystem.posapp.tools.Tools
import com.possystem.posapp.ui.NavigationActivity
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_log_in.emailET
import kotlinx.android.synthetic.main.activity_log_in.passwordET
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogInActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var loginViewModel: LogInViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        loginViewModel = ViewModelProvider(this).get(LogInViewModel::class.java)
        init()
    }

    private fun init() {
        //emailET.onTextChanged { checkValidation(emailET.text.toString()) }
        //setColor()
        rememberMeLayout.setOnClickListener(this)
        signInBT.setOnClickListener(this)


    }



    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.signInBT -> logIn()
            R.id.rememberMeLayout -> rememberMe()
        }
    }

    private fun rememberMe() {
        if (rememberMeLayout.tag == "0") {
            rememberMeIV.setImageResource(R.drawable.ic_check_box)
            rememberMeLayout.tag = "1"
        } else {
            rememberMeIV.setImageResource(R.drawable.ic_check_box_outline_blank)
            rememberMeLayout.tag = "0"
        }

    }


    private fun logIn() {
        val email = emailET.text.toString()
        val password = passwordET.text.toString()
        when {
            email.isEmpty() && password.isEmpty() -> Toast.makeText(this,"არასწორი ინფორმაცია",Toast.LENGTH_SHORT).show()

            password.isNotEmpty() -> {
                //spinKitLoader.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.Main).launch {
                    val response = loginViewModel.logIn(email,password,rememberMeLayout.tag == "1")
                    if(response.success){
                        val intent = Intent(this@LogInActivity, NavigationActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    else {
                         Toast.makeText(this@LogInActivity,response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}


