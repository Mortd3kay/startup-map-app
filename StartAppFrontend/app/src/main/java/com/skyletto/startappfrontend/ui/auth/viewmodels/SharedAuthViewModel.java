package com.skyletto.startappfrontend.ui.auth.viewmodels;

import android.app.Application;
import android.util.Log;

import com.skyletto.startappfrontend.data.database.AppDatabase;
import com.skyletto.startappfrontend.data.network.ApiRepository;
import com.skyletto.startappfrontend.data.requests.RegisterDataRequest;
import com.skyletto.startappfrontend.domain.entities.City;
import com.skyletto.startappfrontend.domain.entities.Country;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SharedAuthViewModel extends AndroidViewModel {

    private static final String TAG = "SHARED_AUTH_VIEW_MODEL";

    private ObservableField<RegisterDataRequest> profile;
    private ObservableField<String> passRepeat = new ObservableField<>();

    private CompositeDisposable cd;
    private final ApiRepository api = ApiRepository.getInstance();
    private AppDatabase db;

    public LiveData<List<Country>> countryList;
    public LiveData<List<City>> cityList;
    private OnNextStepListener onNextStepListener;
    private OnPrevStepListener onPrevStepListener;
    private OnFinishRegisterListener onFinishRegisterListener;

    public SharedAuthViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        profile = new ObservableField<>(new RegisterDataRequest("", "", "", "", ""));
        cd = new CompositeDisposable();
        countryList = db.countryDao().getAll();
        loadCountries();
        Log.d(TAG, "SharedAuthViewModel: "+countryList.getValue());
    }

    private void loadCountries(){
        Disposable disposable = api.apiService.getCountries()
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        countries -> {
                            Log.d(TAG, "loadCountries: "+db.countryDao().insertAll(countries));
                        },
                        throwable -> Log.e(TAG, "accept: ", throwable)
                );
        cd.add(disposable);
    }

    public void loadCities(Country country){
        Disposable disposable = api.apiService.getCitiesByCountryId(country.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry()
                .subscribe(
                        cities -> {
                            db.cityDao().insertAll(cities);

                            //
                            Log.d(TAG, "loadCities: "+cities);
                            //
                        },
                        throwable -> Log.e(TAG, "accept: ", throwable)
                );
        cd.add(disposable);
    }

    public ObservableField<RegisterDataRequest> getProfile() {
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

    public void setOnFinishRegisterListener(OnFinishRegisterListener onFinishRegisterListener) {
        this.onFinishRegisterListener = onFinishRegisterListener;
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

    public void finish(){
        if (onFinishRegisterListener!=null) onFinishRegisterListener.onFinish();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (cd!=null) cd.dispose();
    }
}

