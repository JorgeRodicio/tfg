package com.tfg.hrv.ui.charts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.tfg.hrv.core.XmlService;

public class ChartsViewModel extends AndroidViewModel {
    private XmlService xmlService;

    public ChartsViewModel(@NonNull Application application) {
        super(application);
        this.xmlService = new XmlService(application);
    }

    public XmlService getXmlService() {
        return xmlService;
    }
}
