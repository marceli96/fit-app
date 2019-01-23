package pl.edu.wat.fitapp.dialog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.database.connection.AddIngredientToFoodSystemConnection;
import pl.edu.wat.fitapp.database.entity.Ingredient;
import pl.edu.wat.fitapp.interfaces.callback.ConnectionCallback;
import pl.edu.wat.fitapp.R;

public class AddIngredientToFoodSystemOnClickDialog {
    private ConnectionCallback callback;

    public AddIngredientToFoodSystemOnClickDialog(ConnectionCallback callback) {
        this.callback = callback;
    }

    public void build(final int position, final ArrayList<Ingredient> ingredientList, final int userID, final int mealTime){
        AlertDialog.Builder builder = new AlertDialog.Builder(callback.activity());
        LayoutInflater inflater = LayoutInflater.from(callback.activity());
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
                    callback.onFailure("Wpisz wagę!");
                else {
                    AddIngredientToFoodSystemConnection addConnection = new AddIngredientToFoodSystemConnection(callback);
                    addConnection.addIngredientToFoodSystem(ingredientList.get(position).getID(), userID, mealTime, weight);
                    dialog.dismiss();
                }
            }
        });
    }
}
