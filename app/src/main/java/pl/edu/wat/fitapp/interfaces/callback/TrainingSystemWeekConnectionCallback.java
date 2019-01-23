package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

public interface TrainingSystemWeekConnectionCallback {
    void onSuccessTrainingSystemWeek();
    void onFailure(String message);
    Activity activity();
}
