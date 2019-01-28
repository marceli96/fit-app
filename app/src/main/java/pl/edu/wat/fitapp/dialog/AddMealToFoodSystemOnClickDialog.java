package pl.edu.wat.fitapp.dialog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.androidComponent.listAdapter.SimpleIngredientsListAdapter;
import pl.edu.wat.fitapp.database.connection.AddMealToFoodSystemConnection;
import pl.edu.wat.fitapp.database.entity.Meal;
import pl.edu.wat.fitapp.interfaces.callback.ConnectionCallback;
import pl.edu.wat.fitapp.R;

public class AddMealToFoodSystemOnClickDialog {
    private ConnectionCallback callback;

    public AddMealToFoodSystemOnClickDialog(ConnectionCallback callback) {
        this.callback = callback;
    }

    public void build(final int position, final ArrayList<Meal> mealList, final int userID, final int mealTime){
        AlertDialog.Builder builder = new AlertDialog.Builder(callback.activity());
        LayoutInflater inflater = LayoutInflater.from(callback.activity());
        final View alertView = inflater.inflate(R.layout.dialog_weight_choose_meal, null);

        TextView tvMealName = alertView.findViewById(R.id.tvMealName);
        ListView lvIngredients = alertView.findViewById(R.id.lvIngredients);
        Button bAddMealToFoodSystem = alertView.findViewById(R.id.bAddMealToFoodSystem);

        tvMealName.setText(mealList.get(position).getName());

        SimpleIngredientsListAdapter adapter = new SimpleIngredientsListAdapter(callback.activity(), R.layout.listview_adapter_ingredient_with_weight_simple, mealList.get(position).getIngredientList());
        lvIngredients.setAdapter(adapter);

        builder.setView(alertView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        bAddMealToFoodSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etWeight = alertView.findViewById(R.id.etWeight);
                String weight = etWeight.getText().toString();
                if (weight.isEmpty())
                    callback.onFailure(callback.activity().getString(R.string.fillWeightError));
                else {
                    AddMealToFoodSystemConnection addConnection = new AddMealToFoodSystemConnection(callback);
                    addConnection.addMealToFoodSystem(mealList.get(position).getID(), userID, mealTime, weight);
                    dialog.dismiss();
                }
            }
        });
    }
}
