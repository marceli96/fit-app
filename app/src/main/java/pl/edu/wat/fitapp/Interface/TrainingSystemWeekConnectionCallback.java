package pl.edu.wat.fitapp.Interface;

import android.app.Activity;

public interface TrainingSystemWeekConnectionCallback {
    void onSuccessTrainingSystemWeek();
    void onFailure(String message);
    Activity activity();
}
