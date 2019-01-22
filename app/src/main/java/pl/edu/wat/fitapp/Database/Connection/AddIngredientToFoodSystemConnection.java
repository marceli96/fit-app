package pl.edu.wat.fitapp.Database.Connection;

import android.support.v4.app.Fragment;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.Main.Fragment.AddToSystem.AddIngredientToFoodSystemFragment;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

public class AddIngredientToFoodSystemConnection {
    private Fragment fragment;

    public AddIngredientToFoodSystemConnection(Fragment fragment) {
        this.fragment = fragment;
    }

    public void addIngredientToFoodSystem(final int ingredientId, final int userID, final int mealTime, final String weight) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, fragment.getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean message = jsonResponse.getBoolean("message");
                    if (!message) {
                        ToastUtils.shortToast(fragment.getActivity(), "Dany składnik został już dodany w tej porze jedzenia");
                    } else {
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            ToastUtils.shortToast(fragment.getActivity(), "Dodano pomyślnie");
                            if(fragment.getClass() == AddIngredientToFoodSystemFragment.class){
                                ((AddIngredientToFoodSystemFragment) fragment).openMainActivity();
                            }
                        } else {
                            ToastUtils.shortToast(fragment.getActivity(), "Błąd podczas dodawania");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.shortToast(fragment.getActivity(), "Błąd podczas dodawania " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.shortToast(fragment.getActivity(), "Błąd podczas dodawania " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "addIngredientToFoodSystem");
                params.put("ingredientId", String.valueOf(ingredientId));
                params.put("userId", String.valueOf(userID));
                params.put("mealTime", String.valueOf(mealTime));
                params.put("weight", weight);
                params.put("date", dateFormat.format(date));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(fragment.getActivity());
        requestQueue.add(stringRequest);
    }

}
