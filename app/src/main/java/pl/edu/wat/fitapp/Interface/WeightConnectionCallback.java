package pl.edu.wat.fitapp.Interface;

import android.app.Activity;

import java.util.ArrayList;

public interface WeightConnectionCallback {
    void onSuccessWeightDay(Double weightDay);
    void onSuccessWeightWeek(ArrayList<Double> weightWeek);
    void onFailure(String message);
    Activity activity();
}
