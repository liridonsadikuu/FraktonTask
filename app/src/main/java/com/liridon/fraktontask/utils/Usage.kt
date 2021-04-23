package com.liridon.fraktontask.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun Context.showToast(message: String){
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
}

fun Context.showToastLong(message: String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}


fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPhoneValid(phone: String): Boolean {
    return android.util.Patterns.PHONE.matcher(phone).matches()
}

val Context.isNetworkAvailable: Boolean
    get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo?.isConnectedOrConnecting == true

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}