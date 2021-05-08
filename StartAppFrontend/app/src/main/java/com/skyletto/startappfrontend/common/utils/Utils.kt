package com.skyletto.startappfrontend.common.utils

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.widget.Button
import android.widget.Toast
import com.skyletto.startappfrontend.R


fun toast(context: Context?, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun paintButtonText(btn: Button) {
    val textShader: Shader = LinearGradient(0f, 0f, 0f, 20f, intArrayOf(btn.context.getColor(R.color.pink), btn.context.getColor(R.color.skin)), floatArrayOf(0f, 1f), TileMode.CLAMP)
    btn.paint.shader = textShader
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