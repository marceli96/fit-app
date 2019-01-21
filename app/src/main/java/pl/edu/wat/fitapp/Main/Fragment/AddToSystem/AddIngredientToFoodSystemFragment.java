package pl.edu.wat.fitapp.Main.Fragment.AddToSystem;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.IngredientsListAdapter;
import pl.edu.wat.fitapp.Database.Connection.IngredientsConnection;
import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Dialog.AddIngredientToFoodSystemOnClickDialog;
import pl.edu.wat.fitapp.Main.MainActivity;
import pl.edu.wat.fitapp.R;


public class AddIngredientToFoodSystemFragment extends Fragment {
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

        user = (User) getActivity().getIntent().getSerializableExtra("user");
        mealTime = (int) getActivity().getIntent().getSerializableExtra("mealTime");

        ingredientList = new ArrayList<>();

        lvIngredients = view.findViewById(R.id.lvIngredients);
        ingredientsListAdapter = new IngredientsListAdapter(getActivity(), R.layout.listview_adapter_add_ingredient, ingredientList);
        lvIngredients.setAdapter(ingredientsListAdapter);

        ingredientsConnection = new IngredientsConnection(getActivity(), ingredientList);
        ingredientsConnection.getIngredients(ingredientsListAdapter);

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
        openMainActivity.putExtra("user", user);
        startActivity(openMainActivity);
    }
}