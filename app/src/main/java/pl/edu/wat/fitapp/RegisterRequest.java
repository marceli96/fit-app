package pl.edu.wat.fitapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://fitappliaction.cba.pl/register.php";
    private Map<String, String> params;

    public RegisterRequest(String userName, String password, String email, int sex, int age, int height,
                           int activityLevel, int caloricDemand, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("userName", userName);
        params.put("password", password);
        params.put("email", email);
//        params.put("sex", sex + "");
//        params.put("age", age + "");
//        params.put("height", height + "");
//        params.put("activityLevel", activityLevel + "");
//        params.put("caloricDemand", caloricDemand + "");
        params.put("sex", String.valueOf(sex));
        params.put("age", String.valueOf(age));
        params.put("height", String.valueOf(height));
        params.put("activityLevel", String.valueOf(activityLevel));
        params.put("caloricDemand", String.valueOf(caloricDemand));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
