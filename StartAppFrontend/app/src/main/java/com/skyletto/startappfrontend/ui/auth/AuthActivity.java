package com.skyletto.startappfrontend.ui.auth;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.data.network.ApiRepository;
import com.skyletto.startappfrontend.data.requests.RegisterDataRequest;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.skyletto.startappfrontend.data.network.ApiRepository.ERR_500;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AUTH_ACTIVITY";

    private CompositeDisposable cd = new CompositeDisposable();

    private ApiRepository api = ApiRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        RegisterDataRequest rdr = new RegisterDataRequest("test2@mail.ru", "tester", "Test", "Register", null);

        Disposable disposable = api.apiService.register(rdr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        profileResponse -> {
                            Log.d(TAG, "onCreate: "+profileResponse.getToken());
                        },
                        throwable -> {
                            if (throwable.getMessage().contains(ERR_500))
                                Toast.makeText(AuthActivity.this, "Пользователь с таким email уже существует", Toast.LENGTH_SHORT).show();
                        }
                );

        cd.add(disposable);
    }

    @Override
    protected void onDestroy() {
        if (cd!=null) cd.dispose();
        super.onDestroy();
    }
}