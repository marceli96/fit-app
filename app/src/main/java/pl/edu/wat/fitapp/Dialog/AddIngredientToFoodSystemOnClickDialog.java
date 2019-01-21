package pl.edu.wat.fitapp.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pl.edu.wat.fitapp.Database.Connection.AddIngredientToFoodSystemConnection;
import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.R;

public class AddIngredientToFoodSystemOnClickDialog {
    private Fragment fragment;

    public AddIngredientToFoodSystemOnClickDialog(Fragment fragment) {
        this.fragment = fragment;
    }

    public void build(final int position, final ArrayList<Ingredient> ingredientList, final int userID, final int mealTime){
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        LayoutInflater inflater = LayoutInflater.from(fragment.getActivity());
        final View alertView = inflater.inflate(R.layout.dialog_weight_choose_inredient, null);

        TextView tvIngredientName = alertView.findViewById(R.id.tvIngredientName);
        Button bAddIngredientToFoodSystem = alertView.findViewById(R.id.bAddIngredientToFoodSystem);

        tvIngredientName.setText(ingredientList.get(position).getName());

        builder.setView(alertView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        bAddIngredientToFoodSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etWeight = alertView.findViewById(R.id.etWeight);
                String weight = etWeight.getText().toString();
                if (weight.isEmpty())
                    Toast.makeText(fragment.getActivity(), "Wpisz wagę!", Toast.LENGTH_SHORT).show();
                else {
                    AddIngredientToFoodSystemConnection addConnection = new AddIngredientToFoodSystemConnection(fragment);
                    addConnection.addIngredientToFoodSystem(ingredientList.get(position).getID(), userID, mealTime, weight);
                    dialog.dismiss();
                }
            }
        });
    }
}
