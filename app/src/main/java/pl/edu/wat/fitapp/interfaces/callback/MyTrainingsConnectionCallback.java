package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

public interface MyTrainingsConnectionCallback {
    void onSuccessMyTrainings();
    void onFailure(String message);
    Activity activity();
}
