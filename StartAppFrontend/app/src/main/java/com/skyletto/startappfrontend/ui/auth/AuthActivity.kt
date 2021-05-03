package com.skyletto.startappfrontend.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.ui.auth.AuthActivity
import com.skyletto.startappfrontend.ui.auth.fragments.FirstStepFragment
import com.skyletto.startappfrontend.ui.auth.fragments.LoginFragment
import com.skyletto.startappfrontend.ui.auth.fragments.SecondStepFragment
import com.skyletto.startappfrontend.ui.auth.fragments.ThirdStepFragment
import com.skyletto.startappfrontend.ui.main.MainActivity

class AuthActivity : AppCompatActivity(), ActivityStepper, TokenSaver {
    private lateinit var fm: FragmentManager
    private val fragments: Array<Fragment?> = arrayOfNulls(STEPS_COUNT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        stepNum = -1
        fm = supportFragmentManager
        fm.beginTransaction().add(R.id.auth_frame_layout, LoginFragment.newInstance(this)).commit()
        fragments[0] = FirstStepFragment.newInstance(this)
        fragments[1] = SecondStepFragment.newInstance(this)
        fragments[2] = ThirdStepFragment.newInstance(this)
    }

    override fun nextStep() {
        fragments[++stepNum]?.let {
            fm.beginTransaction().replace(R.id.auth_frame_layout, it).addToBackStack(null).commit()
        }
    }

    override fun prevStep() {
        fm.popBackStack()
        stepNum--
    }

    override fun onFinish(bundle: Bundle?) {
        val intent = Intent(this@AuthActivity, MainActivity::class.java)
        bundle?.let { intent.putExtras(it) }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        stepNum--
        super.onBackPressed()
    }

    override fun save(token: String?, id: Long) {
        val sp = getSharedPreferences("profile", MODE_PRIVATE)
        val e = sp.edit()
        e.putString("token", token)
        e.putLong("id", id)
        e.apply()
    }

    companion object {
        private const val TAG = "AUTH_ACTIVITY"
        private const val STEPS_COUNT = 3
        private var stepNum = -1
    }
}