package pl.edu.wat.fitapp.charts.chartComponent;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public class XAxisValueFormatter implements IAxisValueFormatter {

    ArrayList<String> values;

    public XAxisValueFormatter(ArrayList<String> values) {
        this.values = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return values.get((int) value);
    }
}
