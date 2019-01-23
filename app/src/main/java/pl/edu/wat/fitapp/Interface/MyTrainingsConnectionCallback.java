package pl.edu.wat.fitapp.Interface;

import android.app.Activity;

public interface MyTrainingsConnectionCallback {
    void onSuccessMyTrainings();
    void onFailure(String message);
    Activity activity();
}
