package com.skyletto.startappfrontend.ui.start

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.ui.auth.AuthActivity
import com.skyletto.startappfrontend.ui.main.MainActivity

class StartActivity : AppCompatActivity() {
    private var auth = false
    private var bundle: Bundle? = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val sp = getSharedPreferences("profile", MODE_PRIVATE)
        val token = sp.getString("token", "")
        val id = sp.getLong("id", -1)
        auth = token != ""
        Handler().postDelayed({
            val intent: Intent
            if (auth) {
                bundle?.putString("token", token)
                bundle?.putLong("id", id)
                intent = Intent(this@StartActivity, MainActivity::class.java)
            } else {
                val v = findViewById<View>(R.id.start_logo_img)
                intent = Intent(this@StartActivity, AuthActivity::class.java)
                if (v != null) {
                    val options = ActivityOptions.makeSceneTransitionAnimation(this@StartActivity, v, getString(R.string.start_auth_image_transition))
                    bundle = options.toBundle()
                }
            }
            bundle?.let { intent.putExtras(it) }
            startActivity(intent)
        }, 1000)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
        Handler().postDelayed({ finish() }, 1000)
    }

    companion object {
        private const val TAG = "START_ACTIVITY"
    }
}