package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

public interface GoalWeekConnectionCallback {
    void onSuccessGoalWeek();
    void onFailure(String message);
    Activity activity();
}
