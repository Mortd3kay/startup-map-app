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
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Completable;
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

    private LiveData<List<Country>> countryList;
    private MutableLiveData<List<City>> cityList = new MutableLiveData<>();
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
        loadAllCities();
    }

    private void loadCountries() {
        Disposable disposable = api.apiService.getCountries()
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        countries -> db.countryDao().insertAll(countries),
                        throwable -> Log.e(TAG, "accept: ", throwable)
                );
        cd.add(disposable);
    }

    public void loadCities(Country country) {
        Disposable disposable = api.apiService.getCitiesByCountryId(country.getId())
                .subscribeOn(Schedulers.io())
                .doFinally(() -> cityList.postValue(db.cityDao().getByCountryId(country.getId())))
                .subscribe(
                        cities -> db.cityDao().insertAll(cities),
                        throwable -> Log.e(TAG, "accept: ", throwable)
                );
        cd.add(disposable);
    }

    public void loadAllCities() {
        Disposable disposable = api.apiService.getAllCities()
                .subscribeOn(Schedulers.io())
                .retry()
                .subscribe(
                        cities -> cityList.postValue(cities),
                        throwable -> Log.e(TAG, "accept: ", throwable)
                );
        cd.add(disposable);
    }

    public void saveCity(City city){
        Completable.fromRunnable(() -> db.cityDao().insert(city))
                .subscribeOn(Schedulers.io()).subscribe();
    }

    public Country containsCountry(String name) {
        return countryList.getValue().stream()
                .filter(country -> country.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public LiveData<List<Country>> getCountryList() {
        return countryList;
    }

    public LiveData<List<City>> getCityList() {
        return cityList;
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

    public void nextStep() {
        if (onNextStepListener != null) onNextStepListener.onNext();
    }

    public void prevStep() {
        if (onPrevStepListener != null) onPrevStepListener.onPrev();
    }

    public void finish() {
        RegisterDataRequest finalData = profile.get();
        Disposable d = db.cityDao().getCityByName(finalData.getCity().getName())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        finalData::setCity,
                        throwable -> Log.e(TAG, "finish_get_city: ", throwable)
                );
        cd.add(d);
        d = db.countryDao().getCountryByName(finalData.getCountry().getName())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        finalData::setCountry,
                        throwable -> Log.e(TAG, "finish_get_country: ", throwable)
                );
        cd.add(d);
        Log.d(TAG, "finish: "+finalData);
        if (onFinishRegisterListener != null) onFinishRegisterListener.onFinish();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (cd != null) cd.dispose();
    }
}

