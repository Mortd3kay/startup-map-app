package com.skyletto.startappfrontend.common.utils

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.widget.Button
import android.widget.Toast
import com.skyletto.startappfrontend.R
import java.util.regex.Pattern


fun toast(context: Context?, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun paintButtonText(btn: Button) {
    val textShader: Shader = LinearGradient(0f, 0f, 0f, 20f, intArrayOf(btn.context.getColor(R.color.pink), btn.context.getColor(R.color.skin)), floatArrayOf(0f, 1f), TileMode.CLAMP)
    btn.paint.shader = textShader
}

fun isPasswordValid(pass: String): Boolean {
    val n = ".*[0-9].*"
    val a = ".*[\\p{L}].*"
    return if (pass.length < 8 || pass.length > 40) false else pass.matches(Regex(n)) && pass.matches(Regex(a))
}

fun isEmailValid(email: String): Boolean {
    val p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
    return p.matcher(email).matches()
}

fun isNameValid(name: String): Boolean {
    val n = "^[\\p{L} .'-]+$"
    return name.isNotEmpty() && name.matches(Regex(n))
}

//fun isNetworkConnected(context: Context): Boolean {
//    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
//    return cm?.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
//}
//
//suspend fun isInternetConnected(): Boolean {
//    return try {
//        val ipAddr: InetAddress = InetAddress.getByName("google.com")
//        //You can replace it with your name
//        !ipAddr.equals("")
//    } catch (e: Exception) {
//        false
//    }
//}