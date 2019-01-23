package pl.edu.wat.fitapp.interfaces.callback;

import android.app.Activity;

public interface IngredientsConnectionCallback {
    void onSuccessIngredients();
    void onFailure(String message);
    Activity activity();
}
