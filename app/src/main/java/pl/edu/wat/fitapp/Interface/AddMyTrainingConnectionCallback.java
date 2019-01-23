package pl.edu.wat.fitapp.Interface;

import android.app.Activity;

public interface AddMyTrainingConnectionCallback {
    void onSuccessAddMyTraining();
    void onFailure(String message);
    Activity activity();
}
