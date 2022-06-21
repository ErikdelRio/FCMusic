package com.example.fcmusicapp.ui.descargas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DescargasViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DescargasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}