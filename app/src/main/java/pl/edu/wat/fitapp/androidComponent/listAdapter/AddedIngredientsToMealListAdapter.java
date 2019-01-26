package pl.edu.wat.fitapp.androidComponent.listAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import pl.edu.wat.fitapp.database.entity.Ingredient;
import pl.edu.wat.fitapp.view.main.fragment.profile.AddMyMealIngredientsActivity;
import pl.edu.wat.fitapp.R;

public class AddedIngredientsToMealListAdapter extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> mealIngredients;
    private AddMyMealIngredientsActivity addMyMealIngredientsActivity;

    public AddedIngredientsToMealListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Ingredient> objects, AddMyMealIngredientsActivity addMyMealIngredientsActivity) {
        super(context, resource, objects);
        mealIngredients = objects;
        this.addMyMealIngredientsActivity = addMyMealIngredientsActivity;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.listview_adapter_ingredient_with_weight, parent, false);

        TextView tvIngredientName = convertView.findViewById(R.id.tvIngredientName);
        TextView tvIngredientWeight = convertView.findViewById(R.id.tvIngredientWeight);
        TextView tvIngredientCarbohydrates = convertView.findViewById(R.id.tvIngredientCarbohydrates);
        TextView tvIngredientProtein = convertView.findViewById(R.id.tvIngredientProtein);
        TextView tvIngredientFat = convertView.findViewById(R.id.tvIngredientFat);
        TextView tvIngredientCalories = convertView.findViewById(R.id.tvIngredientCalories);
        ImageView imDeleteIngredient = convertView.findViewById(R.id.imDeleteIngredient);

        DecimalFormat decimalFormat = new DecimalFormat(getContext().getString(R.string.floatZero));
        String tempString;

        final Ingredient ingredient = mealIngredients.get(position);

        tvIngredientName.setText(ingredient.getName());

        tempString = String.valueOf(ingredient.getWeight()) + getContext().getString(R.string.g);
        tvIngredientWeight.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(ingredient.getCarbohydrates() * ingredient.getWeight() / 100)) + getContext().getString(R.string.g);
        tvIngredientCarbohydrates.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(ingredient.getProtein() * ingredient.getWeight() / 100)) + getContext().getString(R.string.g);
        tvIngredientProtein.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(ingredient.getFat() * ingredient.getWeight() / 100)) + getContext().getString(R.string.g);
        tvIngredientFat.setText(tempString);

        tempString = String.valueOf(ingredient.getCalories() * ingredient.getWeight() / 100) + getContext().getString(R.string.kcal);
        tvIngredientCalories.setText(tempString);

        imDeleteIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMyMealIngredientsActivity.deleteIngredient(ingredient);
            }
        });

        return convertView;
    }
}
