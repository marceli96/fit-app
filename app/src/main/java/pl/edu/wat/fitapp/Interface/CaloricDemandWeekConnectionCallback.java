package pl.edu.wat.fitapp.Interface;

import android.app.Activity;

public interface CaloricDemandWeekConnectionCallback {
    void onSuccessCaloricDemandWeek();
    void onFailure(String message);
    Activity activity();
}
