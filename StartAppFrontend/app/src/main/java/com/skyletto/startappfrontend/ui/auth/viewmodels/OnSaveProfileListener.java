package com.skyletto.startappfrontend.ui.auth.viewmodels;

import com.skyletto.startappfrontend.data.responses.ProfileResponse;

public interface OnSaveProfileListener {
    void save(ProfileResponse pr);
}
