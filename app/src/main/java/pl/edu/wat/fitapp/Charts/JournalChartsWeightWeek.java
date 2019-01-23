package pl.edu.wat.fitapp.Charts;

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

import pl.edu.wat.fitapp.Charts.ChartComponent.XAxisValueFormatter;
import pl.edu.wat.fitapp.Charts.ChartComponent.DoubleValueFormatter;
import pl.edu.wat.fitapp.View.Main.Fragment.JournalFragment;

public class JournalChartsWeightWeek {
    private JournalFragment journalFragment;
    private ArrayList<Double> weightWeek;
    private ArrayList<String> days;
    private int colorWeight;

    public JournalChartsWeightWeek(JournalFragment journalFragment, ArrayList<Double> weightWeek) {
        this.journalFragment = journalFragment;
        this.weightWeek = weightWeek;
        colorWeight = Color.rgb(237, 41, 57);

        Calendar calendar = Calendar.getInstance();
        days = new ArrayList<>();
        Date today = new Date();

        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1));
    }

    public void drawChartsWeightWeek(){
        journalFragment.getLlWeightWeekly().setVisibility(View.VISIBLE);

        XAxisValueFormatter XAxisValueFormatter = new XAxisValueFormatter(days);
        DoubleValueFormatter doubleValueFormatter = new DoubleValueFormatter();

        BarChart chartWeightWeek = journalFragment.getChartWeightWeek();

        chartWeightWeek.setVisibility(View.VISIBLE);

        //wykres wagi
        chartWeightWeek.getDescription().setEnabled(false);
        chartWeightWeek.getLegend().setEnabled(false);
        chartWeightWeek.setScaleEnabled(false);

        if (weightWeek.get(0) == 0 && weightWeek.get(1) == 0 && weightWeek.get(2) == 0 && weightWeek.get(3) == 0 &&
                weightWeek.get(4) == 0 && weightWeek.get(5) == 0 && weightWeek.get(6) == 0) {
            YAxis yAxisLeft = chartWeightWeek.getAxisLeft();
            YAxis yAxisRight = chartWeightWeek.getAxisRight();
            yAxisLeft.setAxisMinimum(0);
            yAxisRight.setAxisMinimum(0);
        }

        ArrayList<BarEntry> barEntriesWeight = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            barEntriesWeight.add(new BarEntry(i,  weightWeek.get(i).floatValue()));
        }

        BarDataSet barDataSetWeight = new BarDataSet(barEntriesWeight, "Test");
        barDataSetWeight.setValueFormatter(doubleValueFormatter);
        barDataSetWeight.setColors(colorWeight);
        barDataSetWeight.setValueTextSize(10);

        BarData dataWeight = new BarData(barDataSetWeight);
        dataWeight.setHighlightEnabled(false);
        chartWeightWeek.setData(dataWeight);

        XAxis xAxisWeight = chartWeightWeek.getXAxis();
        xAxisWeight.setGranularity(1);
        xAxisWeight.setValueFormatter(XAxisValueFormatter);
        xAxisWeight.setPosition(XAxis.XAxisPosition.BOTTOM);
    }
}
