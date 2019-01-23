package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

public interface TrainingSystemDayConnectionCallback {
    void onSuccessTrainingSystemDay();
    void onFailure(String message);
    Activity activity();
}
