package pl.edu.wat.fitapp.AndroidComponent.ListAdapter;

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

import pl.edu.wat.fitapp.Database.Entity.Meal;
import pl.edu.wat.fitapp.R;

public class MealListAdapter extends ArrayAdapter<Meal> {
    private ArrayList<Meal> mealList;

    public MealListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Meal> objects) {
        super(context, resource, objects);
        mealList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.listview_adapter_add_meal, parent, false);

        TextView tvMealName = convertView.findViewById(R.id.tvMealName);
        TextView tvMealTotalWeight = convertView.findViewById(R.id.tvMealTotalWeight);
        TextView tvMealCarbohydrates = convertView.findViewById(R.id.tvMealCarbohydrates);
        TextView tvMealProtein = convertView.findViewById(R.id.tvMealProtein);
        TextView tvMealFat = convertView.findViewById(R.id.tvMealFat);
        TextView tvMealCalories = convertView.findViewById(R.id.tvMealCalories);

        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        String temp;
        tvMealName.setText(mealList.get(position).getName());

        temp = String.valueOf(mealList.get(position).getTotalWeight()) + " g";
        tvMealTotalWeight.setText(temp);

        temp = String.valueOf(decimalFormat.format(mealList.get(position).getCarbohydrates())) + " g";
        tvMealCarbohydrates.setText(temp);

        temp = String.valueOf(decimalFormat.format(mealList.get(position).getProtein())) + " g";
        tvMealProtein.setText(temp);

        temp = String.valueOf(decimalFormat.format(mealList.get(position).getFat())) + " g";
        tvMealFat.setText(temp);

        temp = String.valueOf(mealList.get(position).getCalories()) + " kcal";
        tvMealCalories.setText(temp);

        return convertView;
    }
}

