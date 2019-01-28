package pl.edu.wat.fitapp.dialog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import pl.edu.wat.fitapp.androidComponent.listAdapter.SimpleIngredientsListAdapter;
import pl.edu.wat.fitapp.database.entity.Meal;
import pl.edu.wat.fitapp.interfaces.callback.Callback;
import pl.edu.wat.fitapp.R;

public class MyMealOnClickDialog {
    private Callback callback;

    public MyMealOnClickDialog(Callback callback) {
        this.callback = callback;
    }

    public void build(int position, ArrayList<Meal> myMeals){
        AlertDialog.Builder builder = new AlertDialog.Builder(callback.activity());
        LayoutInflater inflater = LayoutInflater.from(callback.activity());
        View alertView = inflater.inflate(R.layout.dialog_my_meal_details, null);

        TextView tvName = alertView.findViewById(R.id.tvName);
        TextView tvTotalMealWeight = alertView.findViewById(R.id.tvTotalMealWeight);
        TextView tvCarbohydrates = alertView.findViewById(R.id.tvCarbohydrates);
        TextView tvProtein = alertView.findViewById(R.id.tvProtein);
        TextView tvFat = alertView.findViewById(R.id.tvFat);
        TextView tvCalories = alertView.findViewById(R.id.tvCalories);
        TextView tvIngredientAmount = alertView.findViewById(R.id.tvIngredientAmount);
        ListView lvIngredients = alertView.findViewById(R.id.lvIngredients);

        DecimalFormat decimalFormat = new DecimalFormat(callback.activity().getString(R.string.floatZero));

        tvName.setText(myMeals.get(position).getName());

        String tempString = String.valueOf(myMeals.get(position).getTotalWeight()) + callback.activity().getString(R.string.g);
        tvTotalMealWeight.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getCarbohydrates())) + callback.activity().getString(R.string.g);
        tvCarbohydrates.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getProtein())) + callback.activity().getString(R.string.g);
        tvProtein.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getFat())) + callback.activity().getString(R.string.g);
        tvFat.setText(tempString);

        tempString = String.valueOf(myMeals.get(position).getCalories()) + callback.activity().getString(R.string.kcal);
        tvCalories.setText(tempString);

        tempString = String.valueOf(myMeals.get(position).getIngredientList().size());
        tvIngredientAmount.setText(tempString);

        SimpleIngredientsListAdapter simpleIngredientsListAdapter = new SimpleIngredientsListAdapter(callback.activity(), R.layout.listview_adapter_ingredient_with_weight_simple, myMeals.get(position).getIngredientList());
        lvIngredients.setAdapter(simpleIngredientsListAdapter);

        builder.setView(alertView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
