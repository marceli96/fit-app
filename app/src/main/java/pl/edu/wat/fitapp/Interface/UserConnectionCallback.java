package pl.edu.wat.fitapp.Interface;

import android.app.Activity;

import pl.edu.wat.fitapp.Database.Entity.User;

public interface UserConnectionCallback {
    void onSuccess(User user);
    void onFailure(String message);
    Activity activity();
}
