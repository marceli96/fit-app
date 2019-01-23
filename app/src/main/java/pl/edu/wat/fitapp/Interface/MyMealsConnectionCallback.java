package pl.edu.wat.fitapp.Interface;

import android.app.Activity;

public interface MyMealsConnectionCallback {
    void onSuccessMyMeals();
    void onFailure(String message);
    Activity activity();
}
