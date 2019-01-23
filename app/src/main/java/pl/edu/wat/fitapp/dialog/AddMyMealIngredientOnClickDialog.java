package pl.edu.wat.fitapp.dialog;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.database.entity.Ingredient;
import pl.edu.wat.fitapp.interfaces.callback.AddMyMealIngredientCallback;
import pl.edu.wat.fitapp.R;

public class AddMyMealIngredientOnClickDialog {
    private AddMyMealIngredientCallback addMyMealIngredientCallback;

    public AddMyMealIngredientOnClickDialog(AddMyMealIngredientCallback addMyMealIngredientCallback) {
        this.addMyMealIngredientCallback = addMyMealIngredientCallback;
    }

    public void build(final int position, final ArrayList<Ingredient> ingredients){
        AlertDialog.Builder builder = new AlertDialog.Builder(addMyMealIngredientCallback.activity());
        LayoutInflater inflater = LayoutInflater.from(addMyMealIngredientCallback.activity());
        final View alertView = inflater.inflate(R.layout.dialog_weight_choose_inredient, null);

        TextView tvIngredientName = alertView.findViewById(R.id.tvIngredientName);
        Button bAddIngredientToList = alertView.findViewById(R.id.bAddIngredientToFoodSystem);

        tvIngredientName.setText(ingredients.get(position).getName());

        builder.setView(alertView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        bAddIngredientToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etWeight = alertView.findViewById(R.id.etWeight);
                String weight = etWeight.getText().toString();
                if (weight.isEmpty())
                    addMyMealIngredientCallback.onFailure("Wpisz wagę!");
                else {
                    Ingredient tempIngredient = null;
                    try {
                        tempIngredient = (Ingredient) ingredients.get(position).clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    tempIngredient.setWeight(Integer.parseInt(weight));
                    addMyMealIngredientCallback.onSuccess(tempIngredient);
                    dialog.dismiss();
                }
            }
        });
    }
}
