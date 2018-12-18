package pl.edu.wat.fitapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private Button bBreakfast, bSecondBreakfast, bLunch, bDinner, bSnack, bSupper;

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private ArrayList<FoodSystem> foodSystemListBreakfast;
    private ArrayList<FoodSystem> foodSystemListSecondBreakfast;
    private ArrayList<FoodSystem> foodSystemListLunch;
    private ArrayList<FoodSystem> foodSystemListDinner;
    private ArrayList<FoodSystem> foodSystemListSnack;
    private ArrayList<FoodSystem> foodSystemListSupper;

    private User user;

    public HomeFragment() {

    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        user = (User) args.getSerializable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        getArguments nie chce zadziałać
//        user = (User) getArguments().getSerializable("user");
        user = (User) getActivity().getIntent().getSerializableExtra("user");

        foodSystemListBreakfast = new ArrayList<>();
        foodSystemListSecondBreakfast = new ArrayList<>();
        foodSystemListLunch = new ArrayList<>();
        foodSystemListDinner = new ArrayList<>();
        foodSystemListSnack = new ArrayList<>();
        foodSystemListSupper = new ArrayList<>();
//        getFoodSystem();


        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        bBreakfast = getView().findViewById(R.id.bBreakfast);
        bSecondBreakfast = getView().findViewById(R.id.bSecondBreakfast);
        bLunch = getView().findViewById(R.id.bLunch);
        bDinner = getView().findViewById(R.id.bDinner);
        bSnack = getView().findViewById(R.id.bSnack);
        bSupper = getView().findViewById(R.id.bSupper);

        bBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(0);
            }
        });

        bSecondBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(1);
            }
        });

        bLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(2);
            }
        });

        bDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(3);
            }
        });

        bSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(4);
            }
        });

        bSupper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(5);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void getFoodSystem() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Log.d("TESTOWANIE", "Dlugosc response = " + jsonResponse.length());
                        for (int i = 0; i < jsonResponse.length() - 3; i++) {
                            JSONObject temp = jsonResponse.getJSONObject(String.valueOf(i));
                            String type = temp.getString("type");
                            if (type.equals("meal")) {
                                switch (temp.getInt("mealTime")) {
                                    case 0:

                                        break;
                                    case 1:

                                        break;

                                    case 2:

                                        break;
                                    case 3:

                                        break;
                                    case 4:

                                        break;
                                    case 5:

                                        break;
                                }
                            } else {
                                Ingredient tempIngredient;
                                switch (temp.getInt("mealTime")) {
                                    case 0:
                                        tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                        foodSystemListBreakfast.add(tempIngredient);
                                        break;
                                    case 1:
                                        tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                        foodSystemListSecondBreakfast.add(tempIngredient);
                                        break;
                                    case 2:
                                        tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                        foodSystemListLunch.add(tempIngredient);
                                        break;
                                    case 3:
                                        tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                        foodSystemListDinner.add(tempIngredient);
                                        break;
                                    case 4:
                                        tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                        foodSystemListSnack.add(tempIngredient);
                                        break;
                                    case 5:
                                        tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                        foodSystemListSupper.add(tempIngredient);
                                        break;
                                }
                            }
                        }
                        // TODO notify data set changed
                        Toast.makeText(getActivity(), "Pobrano do FoodSystem", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Błąd podczas pobierania do FoodSystem", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd podczas pobierania do FoodSystem " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd podczas pobierania do FoodSystem " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getFoodSystemFromDay");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("date", format.format(date));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public int checkMealPositionInList(int mealId, ArrayList<Meal> mealList) {
        for (int i = 0; i < mealList.size(); i++) {
            if (mealList.get(i).getID() == mealId) {
                return i;
            }
        }
        return -1;
    }



    private void addToFoodSystem(int mealTime) {
        Intent openAddToFoodSystemActivity = new Intent(this.getContext(), AddToFoodSystemActivity.class);
        openAddToFoodSystemActivity.putExtra("user", user);
        openAddToFoodSystemActivity.putExtra("mealTime", mealTime);
        startActivity(openAddToFoodSystemActivity);
    }
}
