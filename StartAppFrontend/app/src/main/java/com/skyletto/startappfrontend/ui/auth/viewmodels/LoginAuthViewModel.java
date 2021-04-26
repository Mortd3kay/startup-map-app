package com.skyletto.startappfrontend.ui.auth.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;

import com.skyletto.startappfrontend.data.network.ApiRepository;
import com.skyletto.startappfrontend.data.requests.LoginDataRequest;
import com.skyletto.startappfrontend.data.responses.ProfileResponse;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginAuthViewModel extends AndroidViewModel {
    
    private ObservableField<LoginDataRequest> data = new ObservableField<>(new LoginDataRequest());
    private OnSuccessLoginListener onSuccessLoginListener;
    private OnErrorLoginListener onErrorLoginListener;
    private OnSaveProfileListener onSaveProfileListener;
    private GoToRegister goToRegister;

    public LoginAuthViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void login() {
        ApiRepository.getInstance().apiService.login(data.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        profileResponse -> {
                            saveProfileInfo(profileResponse);
                            if (onSuccessLoginListener!=null) onSuccessLoginListener.onSuccess(profileResponse);
                            },
                        throwable -> {if (onErrorLoginListener!=null) onErrorLoginListener.onError();}
                );
    }

    public ObservableField<LoginDataRequest> getData() {
        return data;
    }

    public void setData(ObservableField<LoginDataRequest> data) {
        this.data = data;
    }

    public void setOnSuccessLoginListener(OnSuccessLoginListener onSuccessLoginListener) {
        this.onSuccessLoginListener = onSuccessLoginListener;
    }

    public void setOnErrorLoginListener(OnErrorLoginListener onErrorLoginListener) {
        this.onErrorLoginListener = onErrorLoginListener;
    }


    public void setOnSaveProfileListener(OnSaveProfileListener onSaveProfileListener) {
        this.onSaveProfileListener = onSaveProfileListener;
    }

    public void setGoToRegister(GoToRegister goToRegister) {
        this.goToRegister = goToRegister;
    }


    private void saveProfileInfo(ProfileResponse pr) {
        if (onSaveProfileListener!=null) onSaveProfileListener.save(pr);
    }

    public void goToRegister(){
        if (goToRegister!=null)
            goToRegister.goTo();
    }
}

