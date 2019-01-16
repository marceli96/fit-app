package pl.edu.wat.fitapp.Charts.ChartComponent;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class DoubleValueFormatter implements IValueFormatter {

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return String.valueOf(decimalFormat.format(value));
    }
}
