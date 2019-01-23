package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

public interface ExercisesConnectionCallback {
    void onSuccessExercises();
    void onFailure(String message);
    Activity activity();
}
