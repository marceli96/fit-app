package pl.edu.wat.fitapp.androidComponent.listAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import pl.edu.wat.fitapp.database.entity.Ingredient;
import pl.edu.wat.fitapp.R;

public class IngredientsListAdapter extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> ingredients;

    public IngredientsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Ingredient> objects) {
        super(context, resource, objects);
        ingredients = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.listview_adapter_add_ingredient, parent, false);

        TextView tvIngredientName = convertView.findViewById(R.id.tvIngredientName);
        TextView tvIngredientCarbohydrates = convertView.findViewById(R.id.tvIngredientCarbohydrates);
        TextView tvIngredientProtein = convertView.findViewById(R.id.tvIngredientProtein);
        TextView tvIngredientFat = convertView.findViewById(R.id.tvIngredientFat);
        TextView tvIngredientCalories = convertView.findViewById(R.id.tvIngredientCalories);

        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        String tempString;
        tvIngredientName.setText(ingredients.get(position).getName());

        tempString = String.valueOf(decimalFormat.format(ingredients.get(position).getCarbohydrates())) + " g";
        tvIngredientCarbohydrates.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(ingredients.get(position).getProtein())) + " g";
        tvIngredientProtein.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(ingredients.get(position).getFat())) + " g";
        tvIngredientFat.setText(tempString);

        tempString = String.valueOf(ingredients.get(position).getCalories()) + " kcal";
        tvIngredientCalories.setText(tempString);

        return convertView;
    }
}