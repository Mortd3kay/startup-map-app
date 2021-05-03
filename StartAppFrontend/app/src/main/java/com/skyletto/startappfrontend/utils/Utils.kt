package com.skyletto.startappfrontend.utils

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