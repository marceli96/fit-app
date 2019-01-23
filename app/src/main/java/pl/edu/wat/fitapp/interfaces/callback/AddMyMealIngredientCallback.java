package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

import pl.edu.wat.fitapp.database.entity.Ingredient;

public interface AddMyMealIngredientCallback {
    void onSuccess(Ingredient ingredient);
    void onFailure(String message);
    Activity activity();
}
