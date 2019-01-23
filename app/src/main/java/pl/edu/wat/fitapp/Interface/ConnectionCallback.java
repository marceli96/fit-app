package pl.edu.wat.fitapp.Interface;

import android.app.Activity;

public interface ConnectionCallback {
    void onSuccess();
    void onFailure(String message);
    Activity activity();
}
