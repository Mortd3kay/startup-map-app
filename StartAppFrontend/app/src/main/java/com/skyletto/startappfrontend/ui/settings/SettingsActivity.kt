package com.skyletto.startappfrontend.ui.settings

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.utils.*
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
        binding.logOutBtn.setOnClickListener {
            sp.edit().remove("token").remove("id").apply()
            viewModel.clearDB{
                Log.d(TAG, "clearDB: success")
                Log.d(TAG, "initViews: ${sp.getLong("id", -1)}")
                finishAffinity()
                startActivity(Intent(this@SettingsActivity, AuthActivity::class.java))
            }

        }
        paintButtonText(binding.editProfileBtn)
        binding.editProfileBtn.setOnClickListener {
            if (viewModel.areFieldsValid()){
                createConfirmDialog {
                    viewModel.profile.get()?.oldPassword = it
                    viewModel.sendNewProfile()
                }
            } else toast(this, getString(R.string.wrong_fields))
        }
    }

    private fun createConfirmDialog(positive:(pass: String)->Unit){
        val dlg = AlertDialog.Builder(this).create()
        dlg.setTitle(getString(R.string.confirm))
        val v = LayoutInflater.from(this).inflate(R.layout.settings_confirm_dialog,null)
        dlg.setView(v)
        val input = v.findViewById<TextInputEditText>(R.id.settings_confirm_input)
        dlg.setButton(AlertDialog.BUTTON_POSITIVE,getString(R.string.ok)){ _: DialogInterface, _: Int ->
            val pass = input.text.toString()
            if (pass.isBlank() || !isPasswordValid(pass)) toast(this, getString(R.string.wrong_password))
            else positive(pass)
        }
        dlg.setCancelable(true)
        dlg.setTitle(R.string.confirm)
        dlg.show()
    }

    private fun getIdFromPreferences() = sp.getLong("id", -1)

    companion object{
        private const val TAG = "SETTINGS_ACTIVITY"
    }
}