package com.tfg.hrv.core;

import java.util.ArrayList;
import java.util.List;

public class Measurements {
    private List<Measurement> measurementList;

    public Measurements() {
        this.measurementList = new ArrayList<>();
    }

    public List<Measurement> getMeasurementList() {
        return measurementList;
    }

    public void setMeasurementList(List<Measurement> measurementList) {
        this.measurementList = measurementList;
    }
}
