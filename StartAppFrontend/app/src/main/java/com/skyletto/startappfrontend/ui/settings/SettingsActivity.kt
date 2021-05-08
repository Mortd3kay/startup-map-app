package com.skyletto.startappfrontend.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.ui.auth.AuthActivity

class SettingsActivity : AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        sp = getSharedPreferences("profile", MODE_PRIVATE)
        val logOutBtn = findViewById<Button>(R.id.log_out_btn)
        logOutBtn.setOnClickListener {
            sp.edit().remove("token").remove("id").apply()
            startActivity(Intent(this@SettingsActivity, AuthActivity::class.java))
            finish()
        }
    }
}