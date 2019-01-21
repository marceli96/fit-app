package pl.edu.wat.fitapp.Main.Fragment.AddToSystem;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.MealListAdapter;
import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.SimpleIngredientsListAdapter;
import pl.edu.wat.fitapp.Database.Connection.MyMealsConnection;
import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.Database.Entity.Meal;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Dialog.AddMealToFoodSystemOnClickDialog;
import pl.edu.wat.fitapp.Main.MainActivity;
import pl.edu.wat.fitapp.R;

public class AddMealToFoodSystemFragment extends Fragment {
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

        user = (User) getActivity().getIntent().getSerializableExtra("user");
        mealTime = (int) getActivity().getIntent().getSerializableExtra("mealTime");

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
        openMainActivity.putExtra("user", user);
        startActivity(openMainActivity);
    }

    public void showMyMeals() {
        mealListAdapter.notifyDataSetChanged();
    }
}
