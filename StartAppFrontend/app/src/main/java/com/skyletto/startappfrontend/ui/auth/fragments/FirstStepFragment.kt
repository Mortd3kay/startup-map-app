package com.skyletto.startappfrontend.ui.auth.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.data.responses.ProfileResponse
import com.skyletto.startappfrontend.databinding.FragmentFirstStepBinding
import com.skyletto.startappfrontend.ui.auth.ActivityStepper
import com.skyletto.startappfrontend.ui.auth.TokenSaver
import com.skyletto.startappfrontend.ui.auth.viewmodels.*
import com.skyletto.startappfrontend.utils.LaconicTextWatcher
import com.skyletto.startappfrontend.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirstStepFragment : Fragment() {
    private var mActivity: ActivityStepper? = null
    private var viewModel: SharedAuthViewModel? = null
    private var emailAndPassWatcher: TextWatcher? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let { ViewModelProvider(it).get(SharedAuthViewModel::class.java) }
        viewModel?.setOnNextStepListener(object : OnNextStepListener {
            override fun onNext() {
                viewModel?.userExists(
                        { i ->
                            run {
                                if (i > 0) toast(context, "Такой email уже зарегистрирован")
                                else mActivity?.nextStep()
                            }
                        },
                        {
                            toast(context, "Не удалось подключиться к серверу")
                        }
                )

            }
        })
        viewModel?.setOnPrevStepListener(object : OnPrevStepListener {
            override fun onPrev() {
                mActivity?.prevStep()
            }
        })
        viewModel?.setOnFinishRegisterListener(object : OnFinishRegisterListener {
            override fun onFinish(pr: ProfileResponse) {
                val b = Bundle()
                b.putString("token", pr.token)
                pr.user.id?.let { b.putLong("id", it) }
                mActivity?.onFinish(b)
            }
        })
        viewModel?.setOnSaveProfileListener(object : OnSaveProfileListener {
            override fun save(pr: ProfileResponse) {
                if (mActivity is TokenSaver) pr.user.id?.let { (mActivity as TokenSaver).save(pr.token, it) }
            }
        })
        emailAndPassWatcher = object : LaconicTextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewModel?.checkPasswordsAndEmail()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentFirstStepBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_first_step, container, false)
        binding.model = viewModel
        val v = binding.root
        binding.authRegisterEmailInput.addTextChangedListener(emailAndPassWatcher)
        binding.authRegisterPassInput.addTextChangedListener(emailAndPassWatcher)
        binding.authRegisterPassRepeatInput.addTextChangedListener(emailAndPassWatcher)
        return v
    }

    companion object {
        fun newInstance(activityStepper: ActivityStepper?): FirstStepFragment {
            val fragment = FirstStepFragment()
            fragment.mActivity = activityStepper
            return fragment
        }
    }
}