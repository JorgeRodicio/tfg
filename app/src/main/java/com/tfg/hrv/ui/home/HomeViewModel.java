package com.tfg.hrv.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tfg.hrv.core.XmlService;

public class HomeViewModel extends AndroidViewModel {

    private XmlService xmlService;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.xmlService = new XmlService(application);
    }

    public XmlService getXmlService() {
        return xmlService;
    }
}