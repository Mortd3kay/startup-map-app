package com.skyletto.startappfrontend.ui.auth.viewmodels;

import android.app.Application;

import com.skyletto.startappfrontend.data.requests.RegisterDataRequest;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

public class SharedAuthViewModel extends AndroidViewModel {

    private RegisterDataRequest profile;
    private ObservableField<String> passRepeat = new ObservableField<>();

    private OnNextStepListener onNextStepListener;
    private OnPrevStepListener onPrevStepListener;

    public SharedAuthViewModel(@NonNull Application application) {
        super(application);
    }

    public RegisterDataRequest getProfile() {
        return profile;
    }

    public ObservableField<String> getPassRepeat() {
        return passRepeat;
    }

    public void setOnNextStepListener(OnNextStepListener onNextStepListener) {
        this.onNextStepListener = onNextStepListener;
    }

    public void setOnPrevStepListener(OnPrevStepListener onPrevStepListener) {
        this.onPrevStepListener = onPrevStepListener;
    }

    public void setPassRepeat(ObservableField<String> passRepeat) {
        this.passRepeat = passRepeat;
    }

//    public boolean equalPasswords(){
//        return Objects.requireNonNull(profile.getPassword().trim().equals(passRepeat.get().trim());
//    }

    public void nextStep(){
        if (onNextStepListener!=null) onNextStepListener.onNext();
    }

    public void prevStep(){
        if (onPrevStepListener!=null) onPrevStepListener.onPrev();
    }
}

