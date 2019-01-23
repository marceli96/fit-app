package pl.edu.wat.fitapp.Interface;

import android.app.Activity;

public interface AddMyMealConnectionCallback {
    void onSuccessAddMyMeal();
    void onFailure(String message);
    Activity activity();
}
