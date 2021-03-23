package com.skyletto.startappfrontend.ui.auth.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.Toast;

import com.skyletto.startappfrontend.data.network.ApiRepository;
import com.skyletto.startappfrontend.data.requests.LoginDataRequest;
import com.skyletto.startappfrontend.ui.auth.ActivityStepper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginAuthViewModel extends AndroidViewModel {
    
    private String email;
    private String password;
    
    public LoginAuthViewModel(@NonNull Application application) {
        super(application);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @SuppressLint("CheckResult")
    public void login(ActivityStepper activityStepper) {
        ApiRepository.getInstance().apiService.login(new LoginDataRequest(email, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        profileResponse -> activityStepper.onFinish(),
                        throwable -> Toast.makeText(getApplication(), "Неправильно введенные данные", Toast.LENGTH_SHORT).show()
                );
    }
}
