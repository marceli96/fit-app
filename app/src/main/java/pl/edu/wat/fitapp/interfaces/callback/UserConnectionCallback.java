package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

import pl.edu.wat.fitapp.database.entity.User;

public interface UserConnectionCallback {
    void onSuccess(User user);
    void onFailure(String message);
    Activity activity();
}
