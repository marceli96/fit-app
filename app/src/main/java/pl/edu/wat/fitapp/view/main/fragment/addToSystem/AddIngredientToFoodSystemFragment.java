package pl.edu.wat.fitapp.view.main.fragment.addToSystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.androidComponent.listAdapter.IngredientsListAdapter;
import pl.edu.wat.fitapp.database.connection.IngredientsConnection;
import pl.edu.wat.fitapp.database.entity.Ingredient;
import pl.edu.wat.fitapp.database.entity.User;
import pl.edu.wat.fitapp.dialog.AddIngredientToFoodSystemOnClickDialog;
import pl.edu.wat.fitapp.interfaces.callback.ConnectionCallback;
import pl.edu.wat.fitapp.interfaces.callback.IngredientsConnectionCallback;
import pl.edu.wat.fitapp.utils.ToastUtils;
import pl.edu.wat.fitapp.view.main.MainActivity;
import pl.edu.wat.fitapp.R;

public class AddIngredientToFoodSystemFragment extends Fragment implements IngredientsConnectionCallback, ConnectionCallback {
    private ListView lvIngredients;
    private ArrayList<Ingredient> ingredientList;
    private IngredientsListAdapter ingredientsListAdapter;

    private IngredientsConnection ingredientsConnection;

    private User user;
    private int mealTime;

    public AddIngredientToFoodSystemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_ingredient_to_food_system, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra(getString(R.string.userExtra));
        mealTime = (int) getActivity().getIntent().getSerializableExtra(getString(R.string.mealTimeExtra));

        ingredientList = new ArrayList<>();

        lvIngredients = view.findViewById(R.id.lvIngredients);
        ingredientsListAdapter = new IngredientsListAdapter(getActivity(), R.layout.listview_adapter_add_ingredient, ingredientList);
        lvIngredients.setAdapter(ingredientsListAdapter);

        ingredientsConnection = new IngredientsConnection(this, ingredientList);
        ingredientsConnection.getIngredients();

        lvIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AddIngredientToFoodSystemOnClickDialog addIngredientToFoodSystemOnClickDialog = new AddIngredientToFoodSystemOnClickDialog(AddIngredientToFoodSystemFragment.this);
                addIngredientToFoodSystemOnClickDialog.build(position, ingredientList, user.getUserID(), mealTime);
            }
        });

        return view;
    }

    public void openMainActivity() {
        Intent openMainActivity = new Intent(getContext(), MainActivity.class);
        openMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openMainActivity.putExtra(getString(R.string.userExtra), user);
        startActivity(openMainActivity);
    }

    @Override
    public void onSuccessIngredients() {
        ingredientsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess() {
        ToastUtils.shortToast(getActivity(), getString(R.string.successAdd));
        openMainActivity();
    }

    @Override
    public void onFailure(String message) {
        ToastUtils.shortToast(getActivity(), message);
    }

    @Override
    public Activity activity() {
        return getActivity();
    }
}