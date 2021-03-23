package com.skyletto.startappfrontend.ui.auth.viewmodels;

import android.app.Application;

import com.skyletto.startappfrontend.domain.entities.User;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SharedAuthViewModel extends AndroidViewModel {

    private User profile;

    public SharedAuthViewModel(@NonNull Application application) {
        super(application);
    }

    public User getProfile() {
        return profile;
    }

    public void setProfile(User profile) {
        this.profile = profile;
    }
}
