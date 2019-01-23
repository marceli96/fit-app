package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

public interface FoodSystemWeekConnectionCallback {
    void onSuccessFoodSystemWeek();
    void onFailure(String message);
    Activity activity();
}
