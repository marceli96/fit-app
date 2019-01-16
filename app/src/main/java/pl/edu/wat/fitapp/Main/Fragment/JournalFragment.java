package pl.edu.wat.fitapp.Main.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import pl.edu.wat.fitapp.Database.Entity.Connection.FoodSystemDayConnection;
import pl.edu.wat.fitapp.Database.Entity.Connection.FoodSystemWeekConnection;
import pl.edu.wat.fitapp.Database.Entity.Connection.WeightConnection;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Interface.FoodSystem;
import pl.edu.wat.fitapp.Main.MainActivity;
import pl.edu.wat.fitapp.Mangement.MacrocomponentManagement;
import pl.edu.wat.fitapp.R;


public class JournalFragment extends Fragment {

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private TextView tvDate, tvWeightDay;
    private LinearLayout llCaloriesWeekly, llCarbohydratesWeekly, llProteinWeekly, llFatWeekly, llWeightWeekly, llWeightDay;
    private BarChart chartCaloriesWeek, chartCarbohydratesWeek, chartProteinWeek, chartFatWeek, chartDaily, chartWeightWeek;
    private ProgressBar pbLoadingLastWeek, pbLoadingDaily;
    private CalendarView cvDate;

    private User user;
    private ArrayList<ArrayList<FoodSystem>> foodSystemDate;
    private ArrayList<ArrayList<ArrayList<FoodSystem>>> foodSystemWeek;
    private ArrayList<Double> weightWeek;
    private double weightDay;

    private WeightConnection weightConnection;
    private FoodSystemDayConnection foodSystemDayConnection;
    private FoodSystemWeekConnection foodSystemWeekConnection;

    public JournalFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Dziennik");

        View view = getLayoutInflater().inflate(R.layout.fragment_journal, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra("user");

        llCaloriesWeekly = view.findViewById(R.id.llCaloriesWeekly);
        llCarbohydratesWeekly = view.findViewById(R.id.llCarbohydratesWeekly);
        llProteinWeekly = view.findViewById(R.id.llProteinWeekly);
        llFatWeekly = view.findViewById(R.id.llFatWeekly);
        llWeightWeekly = view.findViewById(R.id.llWeightWeekly);
        llWeightDay = view.findViewById(R.id.llWeightDay);
        tvDate = view.findViewById(R.id.tvDate);
        tvWeightDay = view.findViewById(R.id.tvWeightDay);
        chartCaloriesWeek = view.findViewById(R.id.chartCarloriesWeek);
        chartCarbohydratesWeek = view.findViewById(R.id.chartCarbohydratesWeek);
        chartProteinWeek = view.findViewById(R.id.chartProteinWeek);
        chartFatWeek = view.findViewById(R.id.chartFatWeek);
        chartDaily = view.findViewById(R.id.chartDaily);
        chartWeightWeek = view.findViewById(R.id.chartWeightWeek);
        pbLoadingLastWeek = view.findViewById(R.id.pbLoadingLastWeek);
        pbLoadingDaily = view.findViewById(R.id.pbLoadingDaily);
        cvDate = view.findViewById(R.id.cvDate);

        initializeArrays();

        weightConnection = new WeightConnection(JournalFragment.this, weightWeek);
        foodSystemDayConnection = new FoodSystemDayConnection(JournalFragment.this, foodSystemDate);
        foodSystemWeekConnection = new FoodSystemWeekConnection(JournalFragment.this, foodSystemWeek);

        foodSystemWeekConnection.getFoodSystemFromWeek(user.getUserID());
        weightConnection.getWeightFromWeek(user.getUserID());

        cvDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                chartDaily.setVisibility(View.GONE);
                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                tvDate.setText(date);
                llWeightDay.setVisibility(View.GONE);
                pbLoadingDaily.setVisibility(View.VISIBLE);
                clearFoodSystemDate();
                foodSystemDayConnection.getFoodSystemFromDay(user.getUserID(), date);
                weightConnection.getWeightFromDay(user.getUserID(), date);
            }
        });

        return view;
    }

    public void setWeightDay(double weightDay) {
        this.weightDay = weightDay;
    }

    public TextView getTvWeightDay() {
        return tvWeightDay;
    }

    public LinearLayout getLlWeightDay() {
        return llWeightDay;
    }

    public void setWeightWeek(ArrayList<Double> weightWeek) {
        this.weightWeek = weightWeek;
    }

    public ProgressBar getPbLoadingDaily() {
        return pbLoadingDaily;
    }

    public ProgressBar getPbLoadingLastWeek() {
        return pbLoadingLastWeek;
    }

    public BarChart getChartCaloriesWeek() {
        return chartCaloriesWeek;
    }

    public BarChart getChartCarbohydratesWeek() {
        return chartCarbohydratesWeek;
    }

    public BarChart getChartProteinWeek() {
        return chartProteinWeek;
    }

    public BarChart getChartFatWeek() {
        return chartFatWeek;
    }

    public BarChart getChartDaily() {
        return chartDaily;
    }

    public BarChart getChartWeightWeek() {
        return chartWeightWeek;
    }

    public LinearLayout getLlCaloriesWeekly() {
        return llCaloriesWeekly;
    }

    public LinearLayout getLlCarbohydratesWeekly() {
        return llCarbohydratesWeekly;
    }

    public LinearLayout getLlProteinWeekly() {
        return llProteinWeekly;
    }

    public LinearLayout getLlFatWeekly() {
        return llFatWeekly;
    }

    public LinearLayout getLlWeightWeekly() {
        return llWeightWeekly;
    }

    private void clearFoodSystemDate() {
        for (int i = 0; i < 6; i++)
            foodSystemDate.get(i).clear();
    }

    private void initializeArrays() {
        foodSystemWeek = new ArrayList<>();
        foodSystemDate = new ArrayList<>();
        weightWeek = new ArrayList<>();

        for(int i = 0; i < 7; i++){
            foodSystemWeek.add(new ArrayList<ArrayList<FoodSystem>>());
            for(int j = 0; j < 6; j++){
                foodSystemWeek.get(i).add(new ArrayList<FoodSystem>());
            }
        }
        for (int i = 0; i < 6; i++){
            foodSystemDate.add(new ArrayList<FoodSystem>());
        }
    }
}