package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

public interface FoodSystemDayConnectionCallback {
    void onSuccessFoodSystemDay();
    void onFailure(String message);
    Activity activity();
}
