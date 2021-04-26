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
import com.liridon.fraktontask.utils.hideKeyboard
import com.liridon.fraktontask.utils.showToast
import com.liridon.fraktontask.utils.showToastLong
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        onClickListeners()
    }

    private fun onClickListeners() {
        signup_btn.setOnClickListener{
            val email: String = email_edt_text.text.toString()
            val password: String = pass_edt_text.text.toString()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                showToastLong(getString(R.string.fill_all_fields))
            } else{
                progress_bar.visibility = View.VISIBLE
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener{ task ->
                    if(task.isSuccessful){
                        progress_bar.visibility = View.GONE
                        hideKeyboard()
                        showToast(getString(R.string.successfully_registered))
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        progress_bar.visibility = View.GONE
                        showToastLong(getString(R.string.registration_failed))
                    }
                })
            }
        }
        login_btn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}