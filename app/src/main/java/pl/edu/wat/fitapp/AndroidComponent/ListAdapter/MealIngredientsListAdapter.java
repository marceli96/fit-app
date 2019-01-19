package pl.edu.wat.fitapp.AndroidComponent.ListAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.R;

public class MealIngredientsListAdapter extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> ingredientList;

    public MealIngredientsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Ingredient> objects) {
        super(context, resource, objects);
        ingredientList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.listview_adapter_ingredient_with_weight_simple, parent, false);

        TextView tvIngredientName = convertView.findViewById(R.id.tvIngredientName);
        TextView tvIngredientWeight = convertView.findViewById(R.id.tvIngredientWeight);

        tvIngredientName.setText(ingredientList.get(position).getName());
        String tempString = ingredientList.get(position).getWeight() + " g";
        tvIngredientWeight.setText(tempString);

        return convertView;
    }
}
