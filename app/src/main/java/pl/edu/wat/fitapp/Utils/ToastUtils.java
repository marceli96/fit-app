package pl.edu.wat.fitapp.Utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void shortToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
