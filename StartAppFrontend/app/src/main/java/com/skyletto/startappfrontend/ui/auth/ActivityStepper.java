package com.skyletto.startappfrontend.ui.auth;

import android.os.Bundle;

public interface ActivityStepper {

    void nextStep();
    void prevStep();
    void onFinish(Bundle bundle);
}
