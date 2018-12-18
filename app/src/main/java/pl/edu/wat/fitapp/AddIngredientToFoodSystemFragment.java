package pl.edu.wat.fitapp;


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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AddIngredientToFoodSystemFragment extends Fragment {

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";
    private ListView lvIngredients;
    private ArrayList<Ingredient> ingredientList;
    private IngredientListAdapter ingredientListAdapter;

    private User user;
    private int mealTime;


    public AddIngredientToFoodSystemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = (User) getActivity().getIntent().getSerializableExtra("user");
        mealTime = (int) getActivity().getIntent().getSerializableExtra("mealTime");

        ingredientList = new ArrayList<>();
        getIngredients();

        View view = inflater.inflate(R.layout.fragment_add_ingredient_to_food_system, container, false);

        lvIngredients = view.findViewById(R.id.lvIngredients);
        ingredientListAdapter = new IngredientListAdapter(getActivity(), R.layout.listview_adapter_add_ingredient, ingredientList);
        lvIngredients.setAdapter(ingredientListAdapter);

        lvIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Toast.makeText(getActivity(), "Wybrales skladnik o nazwie = " + ingredientList.get(position).getName(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View alertView = getLayoutInflater().inflate(R.layout.dialog_weight_choose_inredient, null);

                TextView tvIngredientName = alertView.findViewById(R.id.tvIngredientName);
                Button bAddIngredientToFoodSystem = alertView.findViewById(R.id.bAddIngredientToFoodSystem);

                tvIngredientName.setText(ingredientList.get(position).getName());

                builder.setView(alertView);
                final AlertDialog dialog = builder.create();
                dialog.show();

                bAddIngredientToFoodSystem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText etWeight = alertView.findViewById(R.id.etWeight);
                        String weight = etWeight.getText().toString();
                        if (weight.isEmpty())
                            Toast.makeText(getActivity(), "Wpisz wagę!", Toast.LENGTH_SHORT).show();
                        else {
                            addIngredientToFoodSystem(ingredientList.get(position).getID(), user.getUserID(), mealTime, weight);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        return view;
    }

    class IngredientListAdapter extends ArrayAdapter<Ingredient> {

        public IngredientListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Ingredient> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_adapter_add_ingredient, parent, false);

            TextView tvIngredientName = convertView.findViewById(R.id.tvIngredientName);
            TextView tvIngredientCarbohydrates = convertView.findViewById(R.id.tvIngredientCarbohydrates);
            TextView tvIngredientProtein = convertView.findViewById(R.id.tvIngredientProtein);
            TextView tvIngredientFat = convertView.findViewById(R.id.tvIngredientFat);
            TextView tvIngredientCalories = convertView.findViewById(R.id.tvIngredientCalories);

            DecimalFormat decimalFormat = new DecimalFormat("#0.0");

            String temp;
            tvIngredientName.setText(ingredientList.get(position).getName());

            temp = String.valueOf(decimalFormat.format(ingredientList.get(position).getCarbohydrates())) + " g";
            tvIngredientCarbohydrates.setText(temp);

            temp = String.valueOf(decimalFormat.format(ingredientList.get(position).getProtein())) + " g";
            tvIngredientProtein.setText(temp);

            temp = String.valueOf(decimalFormat.format(ingredientList.get(position).getFat())) + " g";
            tvIngredientFat.setText(temp);

            temp = String.valueOf(decimalFormat.format(ingredientList.get(position).getCalories())) + " kcal";
            tvIngredientCalories.setText(temp);

            return convertView;
        }
    }

    private void getIngredients() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject ingredient = jsonResponse.getJSONObject(String.valueOf(i));
                            Ingredient tempIngredient = new Ingredient(ingredient.getInt("ID_Ingredient"), ingredient.getString("IngredientName"),
                                    ingredient.getDouble("Carbohydrates"), ingredient.getDouble("Protein"), ingredient.getDouble("Fat"),
                                    ingredient.getInt("Calories"));
                            ingredientList.add(tempIngredient);
                        }
                        ingredientListAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Pobrano składniki", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getContext(), "Wystąpił błąd podczas pobieraniA składników", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Login error! " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Login error! " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "getIngredients");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void addIngredientToFoodSystem(final int ingredientId, final int userID, final int mealTime, final String weight) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(getActivity(), "Dodano pomyślnie", Toast.LENGTH_SHORT).show();
                        openMainActivity();
                    } else {
                        Toast.makeText(getActivity(), "Błąd podczas dodawania", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd podczas dodawania " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd podczas dodawania " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "addIngredientToFoodSystem");
                params.put("ingredientId", String.valueOf(ingredientId));
                params.put("userId", String.valueOf(userID));
                params.put("mealTime", String.valueOf(mealTime));
                params.put("weight", weight);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void openMainActivity() {
        Intent openMainActivity = new Intent(getContext(), MainActivity.class);
        openMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openMainActivity.putExtra("user", user);
        startActivity(openMainActivity);
    }
}
