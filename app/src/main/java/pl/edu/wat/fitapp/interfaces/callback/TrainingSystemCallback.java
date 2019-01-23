package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

import pl.edu.wat.fitapp.interfaces.TrainingSystem;

public interface TrainingSystemCallback {
    void onSuccessTrainingSystem(TrainingSystem training);
    void onFailure(String message);
    Activity activity();
}
