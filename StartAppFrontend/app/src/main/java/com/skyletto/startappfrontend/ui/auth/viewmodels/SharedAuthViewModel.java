package com.skyletto.startappfrontend.ui.auth.viewmodels;

import android.app.Application;
import android.text.TextWatcher;
import android.util.Log;

import com.skyletto.startappfrontend.data.database.AppDatabase;
import com.skyletto.startappfrontend.data.network.ApiRepository;
import com.skyletto.startappfrontend.data.requests.RegisterDataRequest;
import com.skyletto.startappfrontend.domain.entities.City;
import com.skyletto.startappfrontend.domain.entities.Country;
import com.skyletto.startappfrontend.utils.LaconicTextWatcher;

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

    private final ObservableField<Boolean> passAndEmailOk = new ObservableField<>(false);
    private final ObservableField<Boolean> personalInfoOk = new ObservableField<>(false);

    private final TextWatcher emailAndPassWatcher = (LaconicTextWatcher) s -> checkPasswordsAndEmail();
    private final TextWatcher personalInfoWatcher = (LaconicTextWatcher) s -> checkPasswordsAndEmail();

    public SharedAuthViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        profile = new ObservableField<>(new RegisterDataRequest("", "", "", "", ""));
        cd = new CompositeDisposable();
        countryList = db.countryDao().getAll();
        loadAndSaveCountries();
        loadAllCities();
    }

    private void loadAndSaveCountries() {
        Disposable disposable = api.apiService.getCountries()
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        countries -> db.countryDao().insertAll(countries),
                        throwable -> Log.e(TAG, "accept: ", throwable)
                );
        cd.add(disposable);
    }

    public void loadAndSaveCities(Country country) {
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

    private boolean isPasswordValid(String pass){
        String n = ".*[0-9].*";
        String a = ".*[A-Z].*";
        if (pass.length() < 8 || pass.length() > 40) return false;
        return pass.matches(n) && pass.matches(a);
    }

    private boolean isEmailValid(String email){
        String e = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$";
        return email.matches(e);
    }

    public void checkPasswordsAndEmail(){
        String ps1 = profile.get().getPassword();
        String email = profile.get().getEmail();
        String ps2 = passRepeat.get();
        setPassAndEmailOk(ps1.equals(ps2) && isPasswordValid(ps1) && isEmailValid(email));
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

    public ObservableField<Boolean> getPassAndEmailOk() {
        return passAndEmailOk;
    }

    public TextWatcher getEmailAndPassWatcher() {
        return emailAndPassWatcher;
    }

    public TextWatcher getPersonalInfoWatcher() {
        return personalInfoWatcher;
    }

    public void setPassAndEmailOk(boolean value) {
        passAndEmailOk.set(value);
        passAndEmailOk.notifyChange();
    }

    public ObservableField<Boolean> getPersonalInfoOk() {
        return personalInfoOk;
    }

    public void setPersonalInfoOk(boolean value) {
        personalInfoOk.set(value);
        personalInfoOk.notifyChange();
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

