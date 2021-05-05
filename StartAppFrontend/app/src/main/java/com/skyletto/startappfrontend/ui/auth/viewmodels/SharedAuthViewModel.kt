package com.skyletto.startappfrontend.ui.auth.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.skyletto.startappfrontend.data.database.AppDatabase
import com.skyletto.startappfrontend.data.database.AppDatabase.Companion.getInstance
import com.skyletto.startappfrontend.data.requests.RegisterDataRequest
import com.skyletto.startappfrontend.data.responses.ProfileResponse
import com.skyletto.startappfrontend.domain.entities.Tag
import com.skyletto.startappfrontend.common.MainApplication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.regex.Pattern

class SharedAuthViewModel(application: Application) : AndroidViewModel(application) {
    val profile: ObservableField<RegisterDataRequest> = ObservableField(RegisterDataRequest("", "", "", "", ""))
    var passRepeat = ObservableField<String>()
    val tags = MutableLiveData<MutableSet<Tag>>()
    val chosenTags = MutableLiveData<MutableSet<Tag>>(HashSet())
    private val cd = CompositeDisposable()
    private val api = getApplication<MainApplication>().api
    private val db: AppDatabase = getInstance(application)
    private val sp: SharedPreferences = application.getSharedPreferences("profile", Context.MODE_PRIVATE)
    private var onNextStepListener: OnNextStepListener? = null
    private var onPrevStepListener: OnPrevStepListener? = null
    private var onSaveProfileListener: OnSaveProfileListener? = null
    private var onFinishRegisterListener: OnFinishRegisterListener? = null
    val passAndEmailOk = ObservableField(false)
    val personalInfoOk = ObservableField(false)

    fun loadRandomTags() {
        val d = api.apiService.getRandomTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry()
                .subscribe({ tags: Set<Tag> -> setTags(tags) }
                ) { throwable: Throwable? -> Log.e(TAG, "loadRandomTags: ", throwable) }
        cd.add(d)
    }

    fun loadSimilarTags(str: String?) {
        val d = str?.let {
            api.apiService.getSimilarTags(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retry()
                    .subscribe({ tags: Set<Tag> -> setTags(tags) }
                    ) { throwable: Throwable? -> Log.e(TAG, "loadSimilarTags: ", throwable) }
        }
        d?.let { cd.add(it) }
    }

    private fun isPasswordValid(pass: String): Boolean {
        val n = ".*[0-9].*"
        val a = ".*[\\p{L}].*"
        return if (pass.length < 8 || pass.length > 40) false else pass.matches(Regex(n)) && pass.matches(Regex(a))
    }

    private fun isEmailValid(email: String): Boolean {
        val p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
        return p.matcher(email).matches()
    }

    private fun isNameValid(name: String): Boolean {
        val n = "^[\\p{L} .'-]+$"
        return name.isNotEmpty() && name.matches(Regex(n))
    }

    fun checkPasswordsAndEmail() {
        val ps1 = profile.get()?.password
        val email = profile.get()?.email
        val ps2 = passRepeat.get()
        setPassAndEmailOk(ps1 == ps2 && isPasswordValid(ps1 ?: "") && isEmailValid(email ?: ""))
    }

    fun checkPersonalInfo() {
        val name = profile.get()?.firstName
        val surname = profile.get()?.secondName
        setPersonalInfoOk(isNameValid(name ?: "") && isNameValid(surname ?: ""))
    }

    private fun setPassAndEmailOk(value: Boolean) {
        passAndEmailOk.set(value)
        passAndEmailOk.notifyChange()
    }

    fun toTagFromChosenTag(tag: Tag) {
        var buffer = tags.value
        buffer?.add(tag)
        tags.value = buffer
        buffer = chosenTags.value
        buffer?.remove(tag)
        chosenTags.value = buffer
    }

    fun toChosenTagFromTag(tag: Tag) {
        var buffer = chosenTags.value
        buffer?.add(tag)
        chosenTags.value = buffer
        buffer = tags.value
        buffer?.remove(tag)
        tags.value = buffer
    }

    private fun setTags(tags: Set<Tag>) {
        val buffer = tags.filter { !chosenTags.value!!.contains(it) }.toMutableSet()
        this.tags.value = buffer
    }

    private fun setPersonalInfoOk(value: Boolean) {
        personalInfoOk.set(value)
        personalInfoOk.notifyChange()
    }

    fun setOnSaveProfileListener(onSaveProfileListener: OnSaveProfileListener?) {
        this.onSaveProfileListener = onSaveProfileListener
    }

    fun setOnNextStepListener(onNextStepListener: OnNextStepListener?) {
        this.onNextStepListener = onNextStepListener
    }

    fun setOnPrevStepListener(onPrevStepListener: OnPrevStepListener?) {
        this.onPrevStepListener = onPrevStepListener
    }

    fun setOnFinishRegisterListener(onFinishRegisterListener: OnFinishRegisterListener?) {
        this.onFinishRegisterListener = onFinishRegisterListener
    }

    fun nextStep() {
        onNextStepListener?.onNext()
    }

    fun prevStep() {
        onPrevStepListener?.onPrev()
    }

    fun finish() {
        val finalData = profile.get()
        finalData?.tags = chosenTags.value
        Log.d(TAG, "finish: $finalData")
        register(finalData)
    }

    override fun onCleared() {
        super.onCleared()
        cd.dispose()
    }

    fun userExists(func:(Int)->Unit, error: () -> Unit){
        profile.get()?.email?.let { outerIt ->
            api.apiService.findUserByEmail(outerIt)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                func.invoke(it)
                            },
                            {
                                error.invoke()
                            })

        }
    }

    private fun register(data: RegisterDataRequest?) {
        val d = data?.let {
            api.apiService.register(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { profileResponse: ProfileResponse ->
                                Log.d(TAG, "accept: ${profileResponse.token} ${profileResponse.user}")
                                saveProfileInfo(profileResponse)
                                onFinishRegisterListener?.onFinish(profileResponse)
                            }
                    ) { throwable: Throwable? -> Log.e(TAG, "finish: register", throwable) }
        }
        d?.let { cd.add(it) }
    }

    private fun saveProfileInfo(pr: ProfileResponse) {
        onSaveProfileListener?.save(pr)
    }

    companion object {
        private const val TAG = "SHARED_AUTH_VIEW_MODEL"
    }

}