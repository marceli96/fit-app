package pl.edu.wat.fitapp.charts.chartComponent;

import android.content.res.Resources;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

import pl.edu.wat.fitapp.R;

public class DoubleValueFormatter implements IValueFormatter {

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        DecimalFormat decimalFormat = new DecimalFormat(Resources.getSystem().getString(R.string.floatZero));
        return String.valueOf(decimalFormat.format(value));
    }
}
