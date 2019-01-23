package pl.edu.wat.fitapp.Interface;

import android.app.Activity;

public interface GoalWeekConnectionCallback {
    void onSuccessGoalWeek();
    void onFailure(String message);
    Activity activity();
}
