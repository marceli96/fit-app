package pl.edu.wat.fitapp.Charts.ChartComponent;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public class DaysXAxisValueFormatter implements IAxisValueFormatter {

    ArrayList<String> values;

    public DaysXAxisValueFormatter(ArrayList<String> values) {
        this.values = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return values.get((int) value);
    }
}
