package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

import pl.edu.wat.fitapp.database.entity.Exercise;

public interface AddMyTrainingExerciseCallback {
    void onSuccess(Exercise exercise);
    void onFailure(String message);
    Activity activity();
}
