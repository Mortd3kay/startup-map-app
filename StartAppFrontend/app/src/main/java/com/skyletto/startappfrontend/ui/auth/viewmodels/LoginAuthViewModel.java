package com.skyletto.startappfrontend.ui.auth.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;

import com.skyletto.startappfrontend.data.network.ApiRepository;
import com.skyletto.startappfrontend.data.requests.LoginDataRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginAuthViewModel extends AndroidViewModel {
    
    private LoginDataRequest data;
    private OnSuccessLoginListener onSuccessLoginListener;
    private OnErrorLoginListener onErrorLoginListener;
    private GoToRegister goToRegister;

    public LoginAuthViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void login() {
        ApiRepository.getInstance().apiService.login(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        profileResponse -> {if (onSuccessLoginListener!=null) onSuccessLoginListener.onSuccess();},
                        throwable -> {if (onErrorLoginListener!=null) onErrorLoginListener.onError();}
                );
    }

    public LoginDataRequest getData() {
        return data;
    }

    public void setData(LoginDataRequest data) {
        this.data = data;
    }

    public void setOnSuccessLoginListener(OnSuccessLoginListener onSuccessLoginListener) {
        this.onSuccessLoginListener = onSuccessLoginListener;
    }

    public void setOnErrorLoginListener(OnErrorLoginListener onErrorLoginListener) {
        this.onErrorLoginListener = onErrorLoginListener;
    }

    public void setGoToRegister(GoToRegister goToRegister) {
        this.goToRegister = goToRegister;
    }

    public void goToRegister(){
        if (goToRegister!=null)
            goToRegister.goTo();
    }
}

