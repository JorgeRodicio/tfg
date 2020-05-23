package com.tfg.hrv.ui.historical;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.tfg.hrv.core.XmlService;

public class HistoricalViewModel extends AndroidViewModel {

    private XmlService xmlService;

    public HistoricalViewModel(@NonNull Application application) {
        super(application);
        this.xmlService = new XmlService(application);
    }

    public XmlService getXmlService() {
        return xmlService;
    }
}
