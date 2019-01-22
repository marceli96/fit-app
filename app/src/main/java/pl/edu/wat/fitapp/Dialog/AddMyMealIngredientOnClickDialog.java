package pl.edu.wat.fitapp.Dialog;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.Main.Fragment.Profile.AddMyMealIngredientsActivity;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

public class AddMyMealIngredientOnClickDialog {
    private AddMyMealIngredientsActivity addMyMealIngredientsActivity;

    public AddMyMealIngredientOnClickDialog(AddMyMealIngredientsActivity addMyMealIngredientsActivity) {
        this.addMyMealIngredientsActivity = addMyMealIngredientsActivity;
    }

    public void build(final int position, final ArrayList<Ingredient> ingredients){
        AlertDialog.Builder builder = new AlertDialog.Builder(addMyMealIngredientsActivity);
        LayoutInflater inflater = LayoutInflater.from(addMyMealIngredientsActivity);
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
                    ToastUtils.shortToast(addMyMealIngredientsActivity, "Wpisz wagę!");
                else {
                    Ingredient tempIngredient = null;
                    try {
                        tempIngredient = (Ingredient) ingredients.get(position).clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    tempIngredient.setWeight(Integer.parseInt(weight));
                    addMyMealIngredientsActivity.addIngredient(tempIngredient);
                    dialog.dismiss();
                }
            }
        });
    }
}
