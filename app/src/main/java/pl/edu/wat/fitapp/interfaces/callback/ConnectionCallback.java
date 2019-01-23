package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

public interface ConnectionCallback {
    void onSuccess();
    void onFailure(String message);
    Activity activity();
}
