package com.skyletto.startappfrontend.ui.auth.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.skyletto.startappfrontend.data.database.AppDatabase;
import com.skyletto.startappfrontend.data.network.ApiRepository;
import com.skyletto.startappfrontend.data.requests.RegisterDataRequest;
import com.skyletto.startappfrontend.data.responses.ProfileResponse;
import com.skyletto.startappfrontend.domain.entities.Tag;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SharedAuthViewModel extends AndroidViewModel {

    private static final String TAG = "SHARED_AUTH_VIEW_MODEL";

    private ObservableField<RegisterDataRequest> profile;
    private ObservableField<String> passRepeat = new ObservableField<>();

    private MutableLiveData<Set<Tag>> tags = new MutableLiveData<>();
    private MutableLiveData<Set<Tag>> chosenTags = new MutableLiveData<>(new HashSet<>());

    private CompositeDisposable cd;
    private final ApiRepository api = ApiRepository.getInstance();
    private AppDatabase db;
    private SharedPreferences sp;

    private OnNextStepListener onNextStepListener;
    private OnPrevStepListener onPrevStepListener;
    private OnSaveProfileListener onSaveProfileListener;
    private OnFinishRegisterListener onFinishRegisterListener;
    private final ObservableField<Boolean> passAndEmailOk = new ObservableField<>(false);
    private final ObservableField<Boolean> personalInfoOk = new ObservableField<>(false);

    public SharedAuthViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        profile = new ObservableField<>(new RegisterDataRequest("", "", "", "", ""));
        cd = new CompositeDisposable();
        sp = application.getSharedPreferences("profile", Context.MODE_PRIVATE);
    }


    public void loadRandomTags() {
        Disposable d = api.apiService.getRandomTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setTags,
                        throwable -> Log.e(TAG, "loadRandomTags: ", throwable)
                );
        cd.add(d);
    }

    public void loadSimilarTags(String str) {
        Disposable d = api.apiService.getSimilarTags(str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setTags,
                        throwable -> Log.e(TAG, "loadSimilarTags: ", throwable)
                );
        cd.add(d);
    }


    private boolean isPasswordValid(String pass) {
        String n = ".*[0-9].*";
        String a = ".*[\\p{L}].*";
        if (pass.length() < 8 || pass.length() > 40) return false;
        return pass.matches(n) && pass.matches(a);
    }

    private boolean isEmailValid(String email) {
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        boolean b = p.matcher(email).matches();
        return b;
    }

    private boolean isNameValid(String name) {
        String n = "^[\\p{L} .'-]+$";
        return !name.isEmpty() && name.matches(n);
    }

    public void checkPasswordsAndEmail() {
        String ps1 = profile.get().getPassword();
        String email = profile.get().getEmail();
        String ps2 = passRepeat.get();
        setPassAndEmailOk(ps1.equals(ps2) && isPasswordValid(ps1) && isEmailValid(email));
    }

    public void checkPersonalInfo() {
        String name = profile.get().getFirstName();
        String surname = profile.get().getSecondName();
        setPersonalInfoOk(isNameValid(name) && isNameValid(surname));
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

    public void setPassAndEmailOk(boolean value) {
        passAndEmailOk.set(value);
        passAndEmailOk.notifyChange();
    }

    public ObservableField<Boolean> getPersonalInfoOk() {
        return personalInfoOk;
    }

    public MutableLiveData<Set<Tag>> getChosenTags() {
        return chosenTags;
    }

    public void toTagFromChosenTag(Tag tag) {
        Set<Tag> buffer = tags.getValue();
        buffer.add(tag);
        tags.setValue(buffer);
        buffer = chosenTags.getValue();
        buffer.remove(tag);
        chosenTags.setValue(buffer);
    }

    public void toChosenTagFromTag(Tag tag) {
        Set<Tag> buffer = chosenTags.getValue();
        buffer.add(tag);
        chosenTags.setValue(buffer);
        buffer = tags.getValue();
        buffer.remove(tag);
        tags.setValue(buffer);
    }

    public MutableLiveData<Set<Tag>> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        Set<Tag> buffer = tags.stream().filter(tag -> !chosenTags.getValue().contains(tag)).collect(Collectors.toSet());
        this.tags.setValue(buffer);
    }

    public void setPersonalInfoOk(boolean value) {
        personalInfoOk.set(value);
        personalInfoOk.notifyChange();
    }

    public void setOnSaveProfileListener(OnSaveProfileListener onSaveProfileListener) {
        this.onSaveProfileListener = onSaveProfileListener;
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
        finalData.setTags(chosenTags.getValue());

        Log.d(TAG, "finish: " + finalData);

        register(finalData);

    }

    private void register(RegisterDataRequest data){
        Disposable d;
        d = api.apiService.register(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        profileResponse -> {
                            Log.d(TAG, "accept: " + profileResponse.getToken() + " " + profileResponse.getUser());
                            saveProfileInfo(profileResponse);
                            if (onFinishRegisterListener != null)
                                onFinishRegisterListener.onFinish(profileResponse);
                        },
                        throwable -> Log.e(TAG, "finish: register", throwable)
                );
        cd.add(d);
    }

    private void saveProfileInfo(ProfileResponse pr) {
        if (onSaveProfileListener!=null) onSaveProfileListener.save(pr);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (cd != null) cd.dispose();
    }

}
