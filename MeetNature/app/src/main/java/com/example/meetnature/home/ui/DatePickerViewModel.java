package com.example.meetnature.home.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class DatePickerViewModel extends ViewModel {

    public Date date;

    public MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    private static DatePickerViewModel instance = null;
    public static DatePickerViewModel getInstance(){
        if (instance == null){
            instance = new DatePickerViewModel();
        }

        return instance;
    }
}
