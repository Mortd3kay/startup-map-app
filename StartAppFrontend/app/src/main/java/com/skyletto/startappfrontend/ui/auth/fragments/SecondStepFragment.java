package com.skyletto.startappfrontend.ui.auth.fragments;

import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.skyletto.startappfrontend.R;
import com.skyletto.startappfrontend.databinding.FragmentSecondStepBinding;
import com.skyletto.startappfrontend.domain.entities.City;
import com.skyletto.startappfrontend.domain.entities.Country;
import com.skyletto.startappfrontend.ui.auth.ActivityStepper;
import com.skyletto.startappfrontend.ui.auth.viewmodels.SharedAuthViewModel;
import com.skyletto.startappfrontend.utils.LaconicTextWatcher;

import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class SecondStepFragment extends Fragment {

    private static final String TAG = "SECOND_AUTH_STEP_FRAGMENT";

    private ActivityStepper mActivity;
    private SharedAuthViewModel viewModel;
    private AutoCompleteTextView countryTextView;
    private AutoCompleteTextView cityTextView;

    private TextWatcher personalInfoWatcher;

    public SecondStepFragment() {
        // Required empty public constructor
    }

    public static SecondStepFragment newInstance(ActivityStepper activityStepper) {
        SecondStepFragment fragment = new SecondStepFragment();
        fragment.mActivity = activityStepper;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(SharedAuthViewModel.class);
        personalInfoWatcher = (LaconicTextWatcher) s -> viewModel.checkPersonalInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSecondStepBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second_step, container, false);
        View v = binding.getRoot();
        binding.setModel(viewModel);
        binding.setLifecycleOwner(this);
        countryTextView = binding.authCountryInput;
        cityTextView = binding.authCityInput;

        binding.authRegisterFirstNameInput.addTextChangedListener(personalInfoWatcher);
        binding.authRegisterSecondNameInput.addTextChangedListener(personalInfoWatcher);

        cityTextView.setAdapter(new ArrayAdapter<City>(getContext(), R.layout.support_simple_spinner_dropdown_item, new ArrayList<>()));
        viewModel.getCountryList()
                .observe(
                        getViewLifecycleOwner(),
                        countries -> countryTextView.setAdapter(
                                new ArrayAdapter<Country>(
                                        getContext(),
                                        R.layout.support_simple_spinner_dropdown_item,
                                        countries
                                )
                        )
                );

        viewModel.getCityList().observe(getViewLifecycleOwner(), cities -> {
            ArrayAdapter adapter = (ArrayAdapter) cityTextView.getAdapter();
            adapter.clear();
            adapter.addAll(cities);
            Log.d(TAG, "onCreateView: " + cities);
            adapter.notifyDataSetChanged();
        });

        cityTextView.setOnItemClickListener((parent, view, position, id) -> {
            City c = (City) cityTextView.getAdapter().getItem(position);
            viewModel.saveCity(c);
            viewModel.putCity(c);
        });

        countryTextView.addTextChangedListener((LaconicTextWatcher) s -> checkStringIsCountryName(s.toString()));

        countryTextView.setOnItemClickListener((parent, view, position, id) -> {
            Country c = (Country) countryTextView.getAdapter().getItem(position);
            Log.d(TAG, "onCreateView: " + c + " " + c.getId());
            viewModel.loadAndSaveCities(c);
            viewModel.putCountry(c);
        });
        return v;
    }

    private void checkStringIsCountryName(String editable) {
        if (countryTextView.isPerformingCompletion()) return;
        Country c = viewModel.containsCountry(editable);
        if (c != null) viewModel.loadAndSaveCities(c);
        else if (editable.trim().isEmpty())
            viewModel.loadAllCities();
        viewModel.putCountry(c);
    }
}