package pl.edu.wat.fitapp.Dialog;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.SimpleIngredientsListAdapter;
import pl.edu.wat.fitapp.Database.Connection.AddMealToFoodSystemConnection;
import pl.edu.wat.fitapp.Database.Entity.Meal;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

public class AddMealToFoodSystemOnClickDialog {
    private Fragment fragment;

    public AddMealToFoodSystemOnClickDialog(Fragment fragment) {
        this.fragment = fragment;
    }

    public void build(final int position, final ArrayList<Meal> mealList, final int userID, final int mealTime){
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        LayoutInflater inflater = LayoutInflater.from(fragment.getActivity());
        final View alertView = inflater.inflate(R.layout.dialog_weight_choose_meal, null);

        TextView tvMealName = alertView.findViewById(R.id.tvMealName);
        ListView lvIngredients = alertView.findViewById(R.id.lvIngredients);
        Button bAddMealToFoodSystem = alertView.findViewById(R.id.bAddMealToFoodSystem);

        tvMealName.setText(mealList.get(position).getName());

        SimpleIngredientsListAdapter adapter = new SimpleIngredientsListAdapter(fragment.getActivity(), R.layout.listview_adapter_ingredient_with_weight_simple, mealList.get(position).getIngredientList());
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
                    ToastUtils.shortToast(fragment.getActivity(), "Wpisz wagę!");
                else {
                    AddMealToFoodSystemConnection addConnection = new AddMealToFoodSystemConnection(fragment);
                    addConnection.addMealToFoodSystem(mealList.get(position).getID(), userID, mealTime, weight);
                    dialog.dismiss();
                }
            }
        });
    }
}
