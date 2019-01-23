package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

import pl.edu.wat.fitapp.interfaces.FoodSystem;

public interface FoodSystemCallback {
    void onSuccessFoodSystem(int mealTime, FoodSystem food);
    void onFailure(String message);
    Activity activity();
}
