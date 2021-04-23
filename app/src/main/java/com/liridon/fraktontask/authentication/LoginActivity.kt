package com.liridon.fraktontask.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.liridon.fraktontask.MainActivity
import com.liridon.fraktontask.R
import com.liridon.fraktontask.utils.showToast
import com.liridon.fraktontask.utils.showToastLong
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.email_edt_text
import kotlinx.android.synthetic.main.activity_login.login_btn
import kotlinx.android.synthetic.main.activity_login.pass_edt_text
import kotlinx.android.synthetic.main.activity_login.progress_bar
import kotlinx.android.synthetic.main.activity_login.signup_btn
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        onClickListeners()

    }

    private fun onClickListeners() {
        login_btn.setOnClickListener {
            var email: String = email_edt_text.text.toString()
            var password: String = pass_edt_text.text.toString()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                showToastLong("Please fill all the fields")
            } else{
                progress_bar.visibility = View.VISIBLE

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                    if(task.isSuccessful) {
                        progress_bar.visibility = View.GONE
                        showToast("Successfully Logged In")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        progress_bar.visibility = View.GONE
                        showToastLong("Login Failed")
                    }
                })
            }
        }

        signup_btn.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

}