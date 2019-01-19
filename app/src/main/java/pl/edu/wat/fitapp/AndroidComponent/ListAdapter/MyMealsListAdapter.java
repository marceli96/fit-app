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

public class MyMealsListAdapter extends ArrayAdapter<Meal> {
    private ArrayList<Meal> myMeals;

    public MyMealsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Meal> objects) {
        super(context, resource, objects);
        myMeals = objects;
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

        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        tvName.setText(myMeals.get(position).getName());

        String tempString = String.valueOf(myMeals.get(position).getTotalWeight()) + " g";
        tvWeight.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getCarbohydrates())) + " g";
        tvCarbohydrates.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getProtein())) + " g";
        tvProtein.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getFat())) + " g";
        tvFat.setText(tempString);

        tempString = String.valueOf(myMeals.get(position).getCalories()) + " g";
        tvCalories.setText(tempString);

        return convertView;
    }
}
