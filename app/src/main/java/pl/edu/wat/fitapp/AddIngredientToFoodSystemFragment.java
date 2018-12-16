package pl.edu.wat.fitapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddIngredientToFoodSystemFragment extends Fragment {

    private final String GET_INGREDIENTS_URL = "http://fitappliaction.cba.pl/operations.php";
    private ListView lvIngredients;
    private ArrayList<Ingredient> ingredientList;


    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public AddIngredientToFoodSystemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TESTOWANIE", "AddIngredientToFoodSystemFragment start");
        ingredientList = new ArrayList<>();
        Log.d("TESTOWANIE", "Dlugosc listy skladinklow przed pobraniem = " + ingredientList.size());
        getIngredients();
        Log.d("TESTOWANIE", "Dlugosc listy skladinklow po pobraniu = " + ingredientList.size());

        View view = inflater.inflate(R.layout.fragment_add_ingredient_to_food_system, container, false);

        lvIngredients = view.findViewById(R.id.lvIngredients);
        IngredientListAdapter ingredientListAdapter = new IngredientListAdapter(getActivity(), R.layout.add_ingredient_listview_adapter, ingredientList);
        lvIngredients.setAdapter(ingredientListAdapter);
        return view;
    }

    class IngredientListAdapter extends ArrayAdapter<Ingredient> {

        public IngredientListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Ingredient> objects) {
            super(context, resource, objects);
            Log.d("TESTOWANIE", "Dlugosc listy skladnikow przekazanych = " + objects.size());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Log.d("TESTOWANIE", "Utworzono IngredientListAdapter");
            convertView = getLayoutInflater().inflate(R.layout.add_ingredient_listview_adapter, parent, false);

//            TextView tvIngredientName = convertView.findViewById(R.id.tvIngredientName);
//            TextView tvIngredientCarbohydrates = convertView.findViewById(R.id.tvIngredientCarbohydrates);
//            TextView tvIngredientProtein = convertView.findViewById(R.id.tvIngredientProtein);
//            TextView tvIngredientFat = convertView.findViewById(R.id.tvIngredientFat);
//            TextView tvIngredientCalories = convertView.findViewById(R.id.tvIngredientCalories);
//
//            tvIngredientName.setText(ingredientList.get(position).getIngredientName());
//            tvIngredientCarbohydrates.setText(String.valueOf(ingredientList.get(position).getIngredientCarbohydrates()));
//            tvIngredientProtein.setText(String.valueOf(ingredientList.get(position).getIngredientProtein()));
//            tvIngredientFat.setText(String.valueOf(ingredientList.get(position).getIngredientProtein()));
//            tvIngredientCalories.setText(String.valueOf(ingredientList.get(position).getIngredientProtein()));

            return convertView;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private void getIngredients() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_INGREDIENTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        for(int i = 0; i < jsonResponse.length() - 1; i++){
                            JSONObject ingredient = jsonResponse.getJSONObject(String.valueOf(i));
                            Ingredient tempIngredient = new Ingredient(ingredient.getInt("ID_Ingredient"), ingredient.getString("IngredientName"),
                                    ingredient.getInt("Carbohydrates"), ingredient.getInt("Protein"), ingredient.getInt("Fat"),
                                    ingredient.getInt("Calories"));
                            boolean test = getIngredientList().add(tempIngredient);
                            if(test)
                                Log.d("TESTOWANIE", "Dodano");
                        }
                        Toast.makeText(getContext(), "Pobrano składniki", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getContext(), "Wystąpił błąd podczas pobieranie składników", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Login error! " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Login error! " + error.toString(), Toast.LENGTH_LONG).show();

            }
        })
        {
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
}
