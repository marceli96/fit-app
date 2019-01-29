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

import pl.edu.wat.fitapp.androidComponent.listAdapter.MealListAdapter;
import pl.edu.wat.fitapp.database.connection.MyMealsConnection;
import pl.edu.wat.fitapp.database.entity.Meal;
import pl.edu.wat.fitapp.database.entity.User;
import pl.edu.wat.fitapp.dialog.AddMealToFoodSystemOnClickDialog;
import pl.edu.wat.fitapp.interfaces.callback.ConnectionCallback;
import pl.edu.wat.fitapp.interfaces.callback.MyMealsConnectionCallback;
import pl.edu.wat.fitapp.utils.ToastUtils;
import pl.edu.wat.fitapp.view.main.MainActivity;
import pl.edu.wat.fitapp.R;

public class AddMealToFoodSystemFragment extends Fragment implements MyMealsConnectionCallback, ConnectionCallback {
    private ListView lvMeals;
    private ArrayList<Meal> mealList;
    private MealListAdapter mealListAdapter;

    private MyMealsConnection myMealsConnection;

    private User user;
    private int mealTime;


    public AddMealToFoodSystemFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_meal_to_food_system, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra(getString(R.string.userExtra));
        mealTime = (int) getActivity().getIntent().getSerializableExtra(getString(R.string.mealTimeExtra));

        mealList = new ArrayList<>();

        lvMeals = view.findViewById(R.id.lvMeals);
        mealListAdapter = new MealListAdapter(getActivity(), R.layout.listview_adapter_add_meal, mealList);
        lvMeals.setAdapter(mealListAdapter);

        myMealsConnection = new MyMealsConnection(AddMealToFoodSystemFragment.this, mealList);
        myMealsConnection.getMyMeals(user.getUserID());

        lvMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AddMealToFoodSystemOnClickDialog addMealToFoodSystemOnClickDialog = new AddMealToFoodSystemOnClickDialog(AddMealToFoodSystemFragment.this);
                addMealToFoodSystemOnClickDialog.build(position, mealList, user.getUserID(), mealTime);
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
    public void onSuccessMyMeals() {
        mealListAdapter.notifyDataSetChanged();
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
