package com.skyletto.startappfrontend.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.utils.ChatViewModelFactory
import com.skyletto.startappfrontend.common.utils.SettingsViewModelFactory
import com.skyletto.startappfrontend.common.utils.paintButtonText
import com.skyletto.startappfrontend.databinding.ActivitySettingsBinding
import com.skyletto.startappfrontend.ui.auth.AuthActivity
import com.skyletto.startappfrontend.ui.settings.viewmodels.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel
    private lateinit var sp:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        sp = getSharedPreferences("profile", MODE_PRIVATE)
        viewModel = ViewModelProvider(this, SettingsViewModelFactory(application, getIdFromPreferences())).get(SettingsViewModel::class.java)
        binding.model = viewModel
        initViews()
    }

    private fun initViews(){
        paintButtonText(binding.thirdStepContBtn)
        binding.logOutBtn.setOnClickListener {
            sp.edit().remove("token").remove("id").apply()
            startActivity(Intent(this@SettingsActivity, AuthActivity::class.java))
            finish()
        }
    }



    private fun getIdFromPreferences() = sp.getLong("id", -1)
}