package com.skyletto.startappfrontend.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.data.responses.ProfileResponse
import com.skyletto.startappfrontend.databinding.FragmentLoginBinding
import com.skyletto.startappfrontend.ui.auth.ActivityStepper
import com.skyletto.startappfrontend.ui.auth.TokenSaver
import com.skyletto.startappfrontend.ui.auth.viewmodels.*
import com.skyletto.startappfrontend.common.utils.paintButtonText

class LoginFragment : Fragment() {
    private var mActivity: ActivityStepper? = null
    private var viewModel: LoginAuthViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginAuthViewModel::class.java)
        viewModel?.setOnErrorLoginListener(object : OnErrorLoginListener {
            override fun onError() {
                Toast.makeText(context, "Неверные данные", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel?.setOnSuccessLoginListener(object : OnSuccessLoginListener {
            override fun onSuccess(pr: ProfileResponse) {
                val b = Bundle()
                b.putString("token", pr.token)
                pr.user.id?.let { b.putLong("id", it) }
                mActivity!!.onFinish(b)
            }
        })
        viewModel?.setOnSaveProfileListener(object : OnSaveProfileListener {
            override fun save(pr: ProfileResponse) {
                if (mActivity is TokenSaver) pr.user.id?.let { (mActivity as TokenSaver).save(pr.token, it) }
            }
        })
        viewModel?.setGoToRegister(object : GoToRegister {
            override fun goTo() {
                mActivity?.nextStep()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.model = viewModel
        val btn = binding.loginBtn
        paintButtonText(btn)
        return binding.root
    }

    companion object {
        fun newInstance(activity: ActivityStepper?): LoginFragment {
            val fragment = LoginFragment()
            fragment.mActivity = activity
            return fragment
        }
    }
}