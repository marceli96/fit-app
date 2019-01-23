package pl.edu.wat.fitapp.view.main.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pl.edu.wat.fitapp.export.PDFGenerator;
import pl.edu.wat.fitapp.database.connection.CaloricDemandWeekConnection;
import pl.edu.wat.fitapp.database.connection.FoodSystemWeekConnection;
import pl.edu.wat.fitapp.database.connection.GoalWeekConnection;
import pl.edu.wat.fitapp.database.connection.TrainingSystemWeekConnection;
import pl.edu.wat.fitapp.database.entity.User;
import pl.edu.wat.fitapp.interfaces.callback.CaloricDemandWeekConnectionCallback;
import pl.edu.wat.fitapp.interfaces.FoodSystem;
import pl.edu.wat.fitapp.interfaces.callback.FoodSystemWeekConnectionCallback;
import pl.edu.wat.fitapp.interfaces.callback.GoalWeekConnectionCallback;
import pl.edu.wat.fitapp.interfaces.TrainingSystem;
import pl.edu.wat.fitapp.interfaces.callback.TrainingSystemWeekConnectionCallback;
import pl.edu.wat.fitapp.view.main.MainActivity;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.utils.ToastUtils;

public class ExportFragment extends Fragment implements FoodSystemWeekConnectionCallback,
        TrainingSystemWeekConnectionCallback, CaloricDemandWeekConnectionCallback, GoalWeekConnectionCallback {
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

    @Override
    public void onSuccessFoodSystemWeek() {
        trainingSystemWeekConnection = new TrainingSystemWeekConnection(this, trainingSystemWeek);
        trainingSystemWeekConnection.getTrainingSystemFromWeek(user.getUserID());
    }

    @Override
    public void onSuccessTrainingSystemWeek() {
        caloricDemandWeekConnection = new CaloricDemandWeekConnection(this, caloricDemandWeek);
        caloricDemandWeekConnection.getCaloricDemandFromWeek(user.getUserID());
    }

    @Override
    public void onSuccessCaloricDemandWeek() {
        goalWeekConnection = new GoalWeekConnection(this, goalWeek);
        goalWeekConnection.getGoalFromWeek(user.getUserID());
    }

    @Override
    public void onSuccessGoalWeek() {
        pbLoading.setVisibility(View.GONE);
        llOptions.setVisibility(View.VISIBLE);
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
