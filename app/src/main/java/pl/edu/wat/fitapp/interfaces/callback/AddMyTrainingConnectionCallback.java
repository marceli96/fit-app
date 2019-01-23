package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

public interface AddMyTrainingConnectionCallback {
    void onSuccessAddMyTraining();
    void onFailure(String message);
    Activity activity();
}
