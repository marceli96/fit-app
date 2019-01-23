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
import pl.edu.wat.fitapp.database.entity.Meal;
import pl.edu.wat.fitapp.interfaces.FoodSystem;
import pl.edu.wat.fitapp.R;

public class FoodSystemListAdapter extends ArrayAdapter<FoodSystem> {
    private ArrayList<FoodSystem> foodList;
    private DecimalFormat format = new DecimalFormat("0.0");

    public FoodSystemListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<FoodSystem> objects) {
        super(context, resource, objects);
        foodList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.listview_adapter_show_foodsystem, parent, false);

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvWeight = convertView.findViewById(R.id.tvWeight);
        TextView tvCarbohydrates = convertView.findViewById(R.id.tvCarbohydrates);
        TextView tvProtein = convertView.findViewById(R.id.tvProtein);
        TextView tvFat = convertView.findViewById(R.id.tvFat);
        TextView tvCalories = convertView.findViewById(R.id.tvCalories);

        tvName.setText(foodList.get(position).getName());
        String tempString = String.valueOf(foodList.get(position).getWeight()) + " g";
        tvWeight.setText(tempString);
        if (foodList.get(position).getClass() == Ingredient.class) {
            tempString = String.valueOf(format.format(foodList.get(position).getCarbohydrates() * foodList.get(position).getWeight() / 100)) + " g";
            tvCarbohydrates.setText(tempString);
            tempString = String.valueOf(format.format(foodList.get(position).getProtein() * foodList.get(position).getWeight() / 100)) + " g";
            tvProtein.setText(tempString);
            tempString = String.valueOf(format.format(foodList.get(position).getFat() * foodList.get(position).getWeight() / 100)) + " g";
            tvFat.setText(tempString);
            tempString = String.valueOf(foodList.get(position).getCalories() * foodList.get(position).getWeight() / 100) + " kcal";
            tvCalories.setText(tempString);
        } else {
            Meal tempMeal = (Meal) foodList.get(position);
            tempString = String.valueOf(format.format(tempMeal.getCarbohydrates() * tempMeal.getWeight() / tempMeal.getTotalWeight())) + " g";
            tvCarbohydrates.setText(tempString);
            tempString = String.valueOf(format.format(tempMeal.getProtein() * tempMeal.getWeight() / tempMeal.getTotalWeight())) + " g";
            tvProtein.setText(tempString);
            tempString = String.valueOf(format.format(tempMeal.getFat() * tempMeal.getWeight() / tempMeal.getTotalWeight())) + " g";
            tvFat.setText(tempString);
            tempString = String.valueOf(tempMeal.getCalories() * tempMeal.getWeight() / tempMeal.getTotalWeight()) + " kcal";
            tvCalories.setText(tempString);
        }

        return convertView;
    }
}
