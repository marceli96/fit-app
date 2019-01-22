package pl.edu.wat.fitapp.Charts;

import android.graphics.Color;
import android.view.View;
import android.widget.ScrollView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import pl.edu.wat.fitapp.Charts.ChartComponent.DoubleValueFormatter;
import pl.edu.wat.fitapp.Charts.ChartComponent.XAxisValueFormatter;
import pl.edu.wat.fitapp.Interface.FoodSystem;
import pl.edu.wat.fitapp.View.Main.Fragment.JournalFragment;
import pl.edu.wat.fitapp.Mangement.MacrocomponentManagement;
import pl.edu.wat.fitapp.R;

public class JournalChartDay {
    private JournalFragment journalFragment;
    private ArrayList<ArrayList<FoodSystem>> foodSystemDay;
    private int colorCalories, colorCarbohydrates, colorProtein, colorFat;

    public JournalChartDay(JournalFragment journalFragment, ArrayList<ArrayList<FoodSystem>> foodSystemDay) {
        this.journalFragment = journalFragment;
        this.foodSystemDay = foodSystemDay;
        colorCalories = Color.rgb(13, 202, 232);
        colorCarbohydrates = Color.rgb(67, 153, 70);
        colorProtein = Color.rgb(196, 124, 23);
        colorFat = Color.rgb(198, 188, 7);
    }

    public void drawChartsMacroDaily() {
        MacrocomponentManagement macroMg = new MacrocomponentManagement();
        BarChart chartDaily = journalFragment.getChartDaily();
        chartDaily.setVisibility(View.VISIBLE);

        ArrayList<String> xLabels = new ArrayList<>();
        xLabels.add("Kalorie");
        xLabels.add("Węglowodany");
        xLabels.add("Białko");
        xLabels.add("Tłuszcz");

        chartDaily.getDescription().setEnabled(false);
        chartDaily.getLegend().setEnabled(false);
        chartDaily.setScaleEnabled(false);

        if (macroMg.getCaloriesFromDay(foodSystemDay) == 0 && macroMg.getCarbohydratesFromDay(foodSystemDay) == 0 &&
                macroMg.getProteinFromDay(foodSystemDay) == 0 && macroMg.getFatFromDay(foodSystemDay) == 0) {
            YAxis yAxisLeft = chartDaily.getAxisLeft();
            YAxis yAxisRight = chartDaily.getAxisRight();
            yAxisLeft.setAxisMinimum(0);
            yAxisRight.setAxisMinimum(0);
        }

        ArrayList<BarEntry> barEntriesDaily = new ArrayList<>();
        barEntriesDaily.add(new BarEntry(0, macroMg.getCaloriesFromDay(foodSystemDay)));
        barEntriesDaily.add(new BarEntry(1, (float) macroMg.getCarbohydratesFromDay(foodSystemDay)));
        barEntriesDaily.add(new BarEntry(2, (float) macroMg.getProteinFromDay(foodSystemDay)));
        barEntriesDaily.add(new BarEntry(3, (float) macroMg.getFatFromDay(foodSystemDay)));

        BarDataSet barDataSetDaily = new BarDataSet(barEntriesDaily, "Dzień");
        barDataSetDaily.setValueFormatter(new DoubleValueFormatter());
        barDataSetDaily.setColors(colorCalories, colorCarbohydrates, colorProtein, colorFat);
        barDataSetDaily.setValueTextSize(12);

        BarData dataDaily = new BarData(barDataSetDaily);
        chartDaily.setData(dataDaily);

        XAxis xAxis = chartDaily.getXAxis();
        xAxis.setGranularity(1);
        xAxis.setValueFormatter(new XAxisValueFormatter(xLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        final ScrollView scrollView = journalFragment.getView().findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
