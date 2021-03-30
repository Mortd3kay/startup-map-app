package com.skyletto.startappfrontend.utils;

import android.text.Editable;
import android.text.TextWatcher;

public interface LaconicTextWatcher extends TextWatcher {
    @Override
    default void beforeTextChanged(CharSequence s, int start, int count, int after){}

    @Override
    default void onTextChanged(CharSequence s, int start, int before, int count){}

    @Override
    void afterTextChanged(Editable s);
}
