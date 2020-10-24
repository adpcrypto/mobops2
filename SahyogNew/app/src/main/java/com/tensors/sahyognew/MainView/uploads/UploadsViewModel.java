package com.tensors.sahyognew.MainView.uploads;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UploadsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UploadsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is uploads fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}