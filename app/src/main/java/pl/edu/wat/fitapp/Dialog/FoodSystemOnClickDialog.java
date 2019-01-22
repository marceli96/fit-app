package pl.edu.wat.fitapp.Dialog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.FoodSystemListAdapter;
import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.SimpleIngredientsListAdapter;
import pl.edu.wat.fitapp.Database.Connection.DeleteFoodSystemConnection;
import pl.edu.wat.fitapp.Database.Entity.Meal;
import pl.edu.wat.fitapp.Interface.FoodSystem;
import pl.edu.wat.fitapp.View.Main.Fragment.HomeFragment;
import pl.edu.wat.fitapp.R;

public class FoodSystemOnClickDialog {
    private HomeFragment homeFragment;
    private ArrayList<ArrayList<FoodSystem>> foodSystemDay;
    private ArrayList<FoodSystemListAdapter> foodSystemMealTimeAdapters;

    public FoodSystemOnClickDialog(HomeFragment homeFragment, ArrayList<ArrayList<FoodSystem>> foodSystemDay, ArrayList<FoodSystemListAdapter> foodSystemMealTimeAdapters) {
        this.homeFragment = homeFragment;
        this.foodSystemDay = foodSystemDay;
        this.foodSystemMealTimeAdapters = foodSystemMealTimeAdapters;
    }

    public void build(final int position, final int mealTime, final ArrayList<FoodSystem> tempList, final int userID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(homeFragment.getActivity());
        LayoutInflater inflater = LayoutInflater.from(homeFragment.getContext());
        View alertView = inflater.inflate(R.layout.dialog_food_system_details, null);

        TextView tvName = alertView.findViewById(R.id.tvName);
        TextView tvIn100 = alertView.findViewById(R.id.tvIn100);
        LinearLayout llMeal = alertView.findViewById(R.id.llMeal);
        TextView tvTotalMealWeight = alertView.findViewById(R.id.tvTotalMealWeight);
        TextView tvCarbohydrates = alertView.findViewById(R.id.tvCarbohydrates);
        TextView tvProtein = alertView.findViewById(R.id.tvProtein);
        TextView tvFat = alertView.findViewById(R.id.tvFat);
        TextView tvCalories = alertView.findViewById(R.id.tvCalories);
        TextView tvIngredients = alertView.findViewById(R.id.tvIngredients);
        ListView lvIngredients = alertView.findViewById(R.id.lvIngredients);
        Button bDelete = alertView.findViewById(R.id.bDelete);

        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        tvName.setText(tempList.get(position).getName());

        String tempString = String.valueOf(decimalFormat.format(tempList.get(position).getCarbohydrates())) + " g";
        tvCarbohydrates.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(tempList.get(position).getProtein())) + " g";
        tvProtein.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(tempList.get(position).getFat())) + " g";
        tvFat.setText(tempString);

        tempString = String.valueOf(tempList.get(position).getCalories()) + " g";
        tvCalories.setText(tempString);

        if (tempList.get(position).getClass() == Meal.class) {
            tvIn100.setVisibility(View.GONE);
            bDelete.setText("Usuń posiłek z sekcji 'Śniadanie'");
            final Meal tempMeal = (Meal) tempList.get(position);
            tempString = String.valueOf(tempMeal.getTotalWeight()) + " g";
            tvTotalMealWeight.setText(tempString);

            SimpleIngredientsListAdapter adapter = new SimpleIngredientsListAdapter(homeFragment.getActivity(), R.layout.listview_adapter_ingredient_with_weight_simple, tempMeal.getIngredientList());
            lvIngredients.setAdapter(adapter);
        } else {
            tvIngredients.setVisibility(View.GONE);
            llMeal.setVisibility(View.GONE);
            bDelete.setText("Usuń produkt z sekcji 'Śniadanie'");
            lvIngredients.setVisibility(View.GONE);
        }

        builder.setView(alertView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFoodSystemConnection deleteConnection = new DeleteFoodSystemConnection(homeFragment, foodSystemDay, foodSystemMealTimeAdapters);
                deleteConnection.deleteFromFoodSystem(tempList.get(position), userID, mealTime, tempList.get(position).getWeight());
                dialog.dismiss();
            }
        });
    }
}
