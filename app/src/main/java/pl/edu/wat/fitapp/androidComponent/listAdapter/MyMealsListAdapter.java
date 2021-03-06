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

import pl.edu.wat.fitapp.database.entity.Meal;
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

        DecimalFormat decimalFormat = new DecimalFormat(getContext().getString(R.string.floatZero));

        tvName.setText(myMeals.get(position).getName());

        String tempString = String.valueOf(myMeals.get(position).getTotalWeight()) + getContext().getString(R.string.g);
        tvWeight.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getCarbohydrates())) + getContext().getString(R.string.g);
        tvCarbohydrates.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getProtein())) + getContext().getString(R.string.g);
        tvProtein.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getFat())) + getContext().getString(R.string.g);
        tvFat.setText(tempString);

        tempString = String.valueOf(myMeals.get(position).getCalories()) + getContext().getString(R.string.kcal);
        tvCalories.setText(tempString);

        return convertView;
    }
}
