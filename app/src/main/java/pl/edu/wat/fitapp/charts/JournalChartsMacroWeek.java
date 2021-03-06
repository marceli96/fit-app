package pl.edu.wat.fitapp.charts;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.charts.chartComponent.XAxisValueFormatter;
import pl.edu.wat.fitapp.charts.chartComponent.DoubleValueFormatter;
import pl.edu.wat.fitapp.interfaces.FoodSystem;
import pl.edu.wat.fitapp.view.main.fragment.JournalFragment;
import pl.edu.wat.fitapp.mangement.MacrocomponentManagement;

public class JournalChartsMacroWeek {
    private JournalFragment journalFragment;
    private ArrayList<ArrayList<ArrayList<FoodSystem>>> foodSystemWeek;
    private ArrayList<String> days;
    private int colorCalories, colorCarbohydrates, colorProtein, colorFat;

    public JournalChartsMacroWeek(JournalFragment journalFragment, ArrayList<ArrayList<ArrayList<FoodSystem>>> foodSystemWeek) {
        this.journalFragment = journalFragment;
        this.foodSystemWeek = foodSystemWeek;
        colorCalories = Color.rgb(13, 202, 232);
        colorCarbohydrates = Color.rgb(67, 153, 70);
        colorProtein = Color.rgb(196, 124, 23);
        colorFat = Color.rgb(198, 188, 7);

        Calendar calendar = Calendar.getInstance();
        days = new ArrayList<>();
        Date today = new Date();

        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + journalFragment.getString(R.string.slash) + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + journalFragment.getString(R.string.slash) + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + journalFragment.getString(R.string.slash) + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + journalFragment.getString(R.string.slash) + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + journalFragment.getString(R.string.slash) + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + journalFragment.getString(R.string.slash) + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + journalFragment.getString(R.string.slash) + (calendar.get(Calendar.MONTH) + 1));
    }

    public void drawChartsMacroWeek() {
        MacrocomponentManagement macroMg = new MacrocomponentManagement();
        XAxisValueFormatter XAxisValueFormatter = new XAxisValueFormatter(days);
        DoubleValueFormatter doubleValueFormatter = new DoubleValueFormatter();
        journalFragment.getLlCaloriesWeekly().setVisibility(View.VISIBLE);
        journalFragment.getLlCarbohydratesWeekly().setVisibility(View.VISIBLE);
        journalFragment.getLlProteinWeekly().setVisibility(View.VISIBLE);
        journalFragment.getLlFatWeekly().setVisibility(View.VISIBLE);

        BarChart chartCaloriesWeek = journalFragment.getChartCaloriesWeek();
        BarChart chartCarbohydratesWeek = journalFragment.getChartCarbohydratesWeek();
        BarChart chartProteinWeek = journalFragment.getChartProteinWeek();
        BarChart chartFatWeek = journalFragment.getChartFatWeek();

        chartCaloriesWeek.setVisibility(View.VISIBLE);
        chartCarbohydratesWeek.setVisibility(View.VISIBLE);
        chartProteinWeek.setVisibility(View.VISIBLE);
        chartFatWeek.setVisibility(View.VISIBLE);

        // wykres kalorii
        chartCaloriesWeek.getDescription().setEnabled(false);
        chartCaloriesWeek.getLegend().setEnabled(false);
        chartCaloriesWeek.setScaleEnabled(false);

        if (macroMg.getCaloriesFromDay(foodSystemWeek.get(0)) == 0 && macroMg.getCaloriesFromDay(foodSystemWeek.get(1)) == 0 &&
                macroMg.getCaloriesFromDay(foodSystemWeek.get(2)) == 0 && macroMg.getCaloriesFromDay(foodSystemWeek.get(3)) == 0 &&
                macroMg.getCaloriesFromDay(foodSystemWeek.get(4)) == 0 && macroMg.getCaloriesFromDay(foodSystemWeek.get(5)) == 0 &&
                macroMg.getCaloriesFromDay(foodSystemWeek.get(6)) == 0) {
            YAxis yAxisLeft = chartCaloriesWeek.getAxisLeft();
            YAxis yAxisRight = chartCaloriesWeek.getAxisRight();
            yAxisLeft.setAxisMinimum(0);
            yAxisRight.setAxisMinimum(0);
        }

        ArrayList<BarEntry> barEntriesCalories = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            barEntriesCalories.add(new BarEntry(i, macroMg.getCaloriesFromDay(foodSystemWeek.get(i))));

        BarDataSet barDataSetCalories = new BarDataSet(barEntriesCalories, journalFragment.getString(R.string.calories2));
        barDataSetCalories.setColors(colorCalories);
        barDataSetCalories.setValueTextSize(10);

        BarData dataCalories = new BarData(barDataSetCalories);
        dataCalories.setHighlightEnabled(false);
        chartCaloriesWeek.setData(dataCalories);

        XAxis xAxisCalories = chartCaloriesWeek.getXAxis();
        xAxisCalories.setGranularity(1);
        xAxisCalories.setValueFormatter(XAxisValueFormatter);
        xAxisCalories.setPosition(XAxis.XAxisPosition.BOTTOM);

        //wykres węglowodanów
        chartCarbohydratesWeek.getDescription().setEnabled(false);
        chartCarbohydratesWeek.getLegend().setEnabled(false);
        chartCarbohydratesWeek.setScaleEnabled(false);

        if (macroMg.getCarbohydratesFromDay(foodSystemWeek.get(0)) == 0 && macroMg.getCarbohydratesFromDay(foodSystemWeek.get(1)) == 0 &&
                macroMg.getCarbohydratesFromDay(foodSystemWeek.get(2)) == 0 && macroMg.getCarbohydratesFromDay(foodSystemWeek.get(3)) == 0 &&
                macroMg.getCarbohydratesFromDay(foodSystemWeek.get(4)) == 0 && macroMg.getCarbohydratesFromDay(foodSystemWeek.get(5)) == 0 &&
                macroMg.getCarbohydratesFromDay(foodSystemWeek.get(6)) == 0) {
            YAxis yAxisLeft = chartCarbohydratesWeek.getAxisLeft();
            YAxis yAxisRight = chartCarbohydratesWeek.getAxisRight();
            yAxisLeft.setAxisMinimum(0);
            yAxisRight.setAxisMinimum(0);
        }

        ArrayList<BarEntry> barEntriesCarbohydrates = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            barEntriesCarbohydrates.add(new BarEntry(i, (float) macroMg.getCarbohydratesFromDay(foodSystemWeek.get(i))));

        BarDataSet barDataSetCarbohydrates = new BarDataSet(barEntriesCarbohydrates, journalFragment.getString(R.string.carbohydrates2));
        barDataSetCarbohydrates.setValueFormatter(doubleValueFormatter);
        barDataSetCarbohydrates.setColors(colorCarbohydrates);
        barDataSetCarbohydrates.setValueTextSize(10);

        BarData dataCarbohydrates = new BarData(barDataSetCarbohydrates);
        dataCarbohydrates.setHighlightEnabled(false);
        chartCarbohydratesWeek.setData(dataCarbohydrates);

        XAxis xAxisCarbohydrates = chartCarbohydratesWeek.getXAxis();
        xAxisCarbohydrates.setGranularity(1);
        xAxisCarbohydrates.setValueFormatter(XAxisValueFormatter);
        xAxisCarbohydrates.setPosition(XAxis.XAxisPosition.BOTTOM);

        //wykres białka
        chartProteinWeek.getDescription().setEnabled(false);
        chartProteinWeek.getLegend().setEnabled(false);
        chartProteinWeek.setScaleEnabled(false);

        if (macroMg.getProteinFromDay(foodSystemWeek.get(0)) == 0 && macroMg.getProteinFromDay(foodSystemWeek.get(1)) == 0 &&
                macroMg.getProteinFromDay(foodSystemWeek.get(2)) == 0 && macroMg.getProteinFromDay(foodSystemWeek.get(3)) == 0 &&
                macroMg.getProteinFromDay(foodSystemWeek.get(4)) == 0 && macroMg.getProteinFromDay(foodSystemWeek.get(5)) == 0 &&
                macroMg.getProteinFromDay(foodSystemWeek.get(6)) == 0) {
            YAxis yAxisLeft = chartProteinWeek.getAxisLeft();
            YAxis yAxisRight = chartProteinWeek.getAxisRight();
            yAxisLeft.setAxisMinimum(0);
            yAxisRight.setAxisMinimum(0);
        }

        ArrayList<BarEntry> barEntriesProtein = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            barEntriesProtein.add(new BarEntry(i, (float) macroMg.getProteinFromDay(foodSystemWeek.get(i))));

        BarDataSet barDataSetProtein = new BarDataSet(barEntriesProtein, journalFragment.getString(R.string.protein2));
        barDataSetProtein.setValueFormatter(doubleValueFormatter);
        barDataSetProtein.setColors(colorProtein);
        barDataSetProtein.setValueTextSize(10);

        BarData dataProtein = new BarData(barDataSetProtein);
        dataProtein.setHighlightEnabled(false);
        chartProteinWeek.setData(dataProtein);

        XAxis xAxisProtein = chartProteinWeek.getXAxis();
        xAxisProtein.setGranularity(1);
        xAxisProtein.setValueFormatter(XAxisValueFormatter);
        xAxisProtein.setPosition(XAxis.XAxisPosition.BOTTOM);

        //wykres tłuszczu
        chartFatWeek.getDescription().setEnabled(false);
        chartFatWeek.getLegend().setEnabled(false);
        chartFatWeek.setScaleEnabled(false);

        if (macroMg.getFatFromDay(foodSystemWeek.get(0)) == 0 && macroMg.getFatFromDay(foodSystemWeek.get(1)) == 0 &&
                macroMg.getFatFromDay(foodSystemWeek.get(2)) == 0 && macroMg.getFatFromDay(foodSystemWeek.get(3)) == 0 &&
                macroMg.getFatFromDay(foodSystemWeek.get(4)) == 0 && macroMg.getFatFromDay(foodSystemWeek.get(5)) == 0 &&
                macroMg.getFatFromDay(foodSystemWeek.get(6)) == 0) {
            YAxis yAxisLeft = chartFatWeek.getAxisLeft();
            YAxis yAxisRight = chartFatWeek.getAxisRight();
            yAxisLeft.setAxisMinimum(0);
            yAxisRight.setAxisMinimum(0);
        }

        ArrayList<BarEntry> barEntriesFat = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            barEntriesFat.add(new BarEntry(i, (float) macroMg.getFatFromDay(foodSystemWeek.get(i))));

        BarDataSet barDataSetFat = new BarDataSet(barEntriesFat, journalFragment.getString(R.string.fat2));
        barDataSetFat.setValueFormatter(doubleValueFormatter);
        barDataSetFat.setColors(colorFat);
        barDataSetFat.setValueTextSize(10);

        BarData dataFat = new BarData(barDataSetFat);
        dataFat.setHighlightEnabled(false);
        chartFatWeek.setData(dataFat);

        XAxis xAxisFat = chartFatWeek.getXAxis();
        xAxisFat.setGranularity(1);
        xAxisFat.setValueFormatter(XAxisValueFormatter);
        xAxisFat.setPosition(XAxis.XAxisPosition.BOTTOM);
    }
}
