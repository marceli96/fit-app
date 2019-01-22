package pl.edu.wat.fitapp.View.Main.Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import pl.edu.wat.fitapp.DataExport.PDFGenerator;
import pl.edu.wat.fitapp.Database.Connection.CaloricDemandWeekConnection;
import pl.edu.wat.fitapp.Database.Connection.FoodSystemWeekConnection;
import pl.edu.wat.fitapp.Database.Connection.GoalWeekConnection;
import pl.edu.wat.fitapp.Database.Connection.TrainingSystemWeekConnection;
import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.Database.Entity.Meal;
import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Interface.FoodSystem;
import pl.edu.wat.fitapp.Interface.TrainingSystem;
import pl.edu.wat.fitapp.View.Main.MainActivity;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;


public class ExportFragment extends Fragment {
    private CheckBox cbBreakfast, cbSecondBreakfast, cbLunch, cbDinner, cbSnack, cbSupper, cbSummary, cbTraining;
    private Button bGenerate;
    private LinearLayout llOptions;
    private ProgressBar pbLoading;

    private FoodSystemWeekConnection foodSystemWeekConnection;
    private TrainingSystemWeekConnection trainingSystemWeekConnection;
    private CaloricDemandWeekConnection caloricDemandWeekConnection;
    private GoalWeekConnection goalWeekConnection;

    private User user;
    private ArrayList<ArrayList<ArrayList<FoodSystem>>> foodSystemWeek;
    private ArrayList<ArrayList<TrainingSystem>> trainingSystemWeek;
    private ArrayList<Integer> caloricDemandWeek;
    private ArrayList<Integer> goalWeek;
    private ArrayList<Date> days;

    public ExportFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Eksport danych");

        View view = inflater.inflate(R.layout.fragment_export, container, false);
        user = (User) getActivity().getIntent().getSerializableExtra("user");

        cbBreakfast = view.findViewById(R.id.cbBreakfast);
        cbSecondBreakfast = view.findViewById(R.id.cbSecondBreakfast);
        cbLunch = view.findViewById(R.id.cbLunch);
        cbDinner = view.findViewById(R.id.cbDinner);
        cbSnack = view.findViewById(R.id.cbSnack);
        cbSupper = view.findViewById(R.id.cbSupper);
        cbSummary = view.findViewById(R.id.cbSummary);
        cbTraining = view.findViewById(R.id.cbTraining);
        bGenerate = view.findViewById(R.id.bGenerate);
        llOptions = view.findViewById(R.id.llOptions);
        pbLoading = view.findViewById(R.id.pbLoading);

        initializeArrays();

        foodSystemWeekConnection = new FoodSystemWeekConnection(this, foodSystemWeek);
        foodSystemWeekConnection.getFoodSystemFromWeek(user.getUserID());

        bGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cbBreakfast.isChecked() && !cbSecondBreakfast.isChecked() && !cbLunch.isChecked() && !cbDinner.isChecked() &&
                        !cbSnack.isChecked() && !cbSupper.isChecked() && !cbSummary.isChecked() && !cbTraining.isChecked()) {
                    ToastUtils.shortToast(getActivity(), "Wybierz którąś z opcji");
                } else {
                    PDFGenerator pdf = new PDFGenerator(ExportFragment.this);
                    ArrayList<Boolean> exportOptions = new ArrayList<>();
                    exportOptions.add(cbBreakfast.isChecked());
                    exportOptions.add(cbSecondBreakfast.isChecked());
                    exportOptions.add(cbLunch.isChecked());
                    exportOptions.add(cbDinner.isChecked());
                    exportOptions.add(cbSnack.isChecked());
                    exportOptions.add(cbSupper.isChecked());
                    exportOptions.add(cbTraining.isChecked());
                    exportOptions.add(cbSummary.isChecked());
                    pdf.generate(exportOptions, foodSystemWeek, trainingSystemWeek, caloricDemandWeek, goalWeek);
                }
            }
        });

        return view;
    }

    private void initializeArrays() {
        foodSystemWeek = new ArrayList<>();
        trainingSystemWeek = new ArrayList<>();
        caloricDemandWeek = new ArrayList<>();
        goalWeek = new ArrayList<>();
        days = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        for (int i = 0; i < 7; i++) {
            foodSystemWeek.add(new ArrayList<ArrayList<FoodSystem>>());
            trainingSystemWeek.add(new ArrayList<TrainingSystem>());
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            days.add(calendar.getTime());
            for (int j = 0; j < 6; j++)
                foodSystemWeek.get(i).add(new ArrayList<FoodSystem>());
        }
    }

    public void getTrainingSystemFromWeek() {
        trainingSystemWeekConnection = new TrainingSystemWeekConnection(this, trainingSystemWeek);
        trainingSystemWeekConnection.getTrainingSystemFromWeek(user.getUserID());
    }

    public void getCaloricDemandFromWeek() {
        caloricDemandWeekConnection = new CaloricDemandWeekConnection(this, caloricDemandWeek);
        caloricDemandWeekConnection.getCaloricDemandFromWeek(user.getUserID());
    }

    public void getGoalFromWeek() {
        goalWeekConnection = new GoalWeekConnection(this, goalWeek);
        goalWeekConnection.getGoalFromWeek(user.getUserID());
    }

    public void showOptions() {
        pbLoading.setVisibility(View.GONE);
        llOptions.setVisibility(View.VISIBLE);
    }
}
