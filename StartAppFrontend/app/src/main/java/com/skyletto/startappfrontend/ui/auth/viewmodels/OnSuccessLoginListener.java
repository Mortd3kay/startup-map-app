package com.skyletto.startappfrontend.ui.auth.viewmodels;

import com.skyletto.startappfrontend.data.responses.ProfileResponse;

public interface OnSuccessLoginListener{
    void onSuccess(ProfileResponse pr);
}
