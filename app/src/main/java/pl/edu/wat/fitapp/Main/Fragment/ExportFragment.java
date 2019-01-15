package pl.edu.wat.fitapp.Main.Fragment;


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
import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.Database.Entity.Meal;
import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Interface.FoodSystem;
import pl.edu.wat.fitapp.Interface.TrainingSystem;
import pl.edu.wat.fitapp.Main.MainActivity;
import pl.edu.wat.fitapp.R;


public class ExportFragment extends Fragment {

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private CheckBox cbBreakfast, cbSecondBreakfast, cbLunch, cbDinner, cbSnack, cbSupper, cbSummary, cbTraining;
    private Button bGenerate;
    private LinearLayout llOptions;
    private ProgressBar pbLoading;

    private User user;
    private ArrayList<ArrayList<FoodSystem>> foodSystem1DayBefore, foodSystem2DayBefore, foodSystem3DayBefore,
            foodSystem4DayBefore, foodSystem5DayBefore, foodSystem6DayBefore, foodSystem7DayBefore;
    private ArrayList<TrainingSystem> trainingSystem1DayBefore, trainingSystem2DayBefore, trainingSystem3DayBefore,
            trainingSystem4DayBefore, trainingSystem5DayBefore, trainingSystem6DayBefore, trainingSystem7DayBefore;
    private int caloricDemand1dayBefore, caloricDemand2dayBefore, caloricDemand3dayBefore, caloricDemand4dayBefore,
            caloricDemand5dayBefore, caloricDemand6dayBefore, caloricDemand7dayBefore;
    private int goal1DayBefore, goal2DayBefore, goal3DayBefore, goal4DayBefore, goal5DayBefore, goal6DayBefore, goal7DayBefore;
    private Date date1Before, date2Before, date3Before, date4Before, date5Before, date6Before, date7Before;

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

        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date1Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date2Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date3Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date4Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date5Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date6Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date7Before = calendar.getTime();

        getFoodSystemFromWeek();

        bGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });

        return view;
    }

    private void generatePDF() {
        boolean breakfast = cbBreakfast.isChecked();
        boolean secondBreakfast = cbSecondBreakfast.isChecked();
        boolean lunch = cbLunch.isChecked();
        boolean dinner = cbDinner.isChecked();
        boolean snack = cbSnack.isChecked();
        boolean supper = cbSupper.isChecked();
        boolean summary = cbSummary.isChecked();
        boolean training = cbTraining.isChecked();

        if (!breakfast && !secondBreakfast && !lunch && !dinner && !snack && !supper && !summary && !training) {
            Toast.makeText(getActivity(), "Wybierz którąś z opcji", Toast.LENGTH_SHORT).show();
        } else {
            verifyStoragePermissions(getActivity());
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();

                File file = new File(path, "excel.xls");

                ArrayList<ArrayList<ArrayList<FoodSystem>>> foodSystemWeek = new ArrayList<>();
                foodSystemWeek.add(foodSystem1DayBefore);
                foodSystemWeek.add(foodSystem2DayBefore);
                foodSystemWeek.add(foodSystem3DayBefore);
                foodSystemWeek.add(foodSystem4DayBefore);
                foodSystemWeek.add(foodSystem5DayBefore);
                foodSystemWeek.add(foodSystem6DayBefore);
                foodSystemWeek.add(foodSystem7DayBefore);

                ArrayList<ArrayList<TrainingSystem>> trainingSystemWeek = new ArrayList<>();
                trainingSystemWeek.add(trainingSystem1DayBefore);
                trainingSystemWeek.add(trainingSystem2DayBefore);
                trainingSystemWeek.add(trainingSystem3DayBefore);
                trainingSystemWeek.add(trainingSystem4DayBefore);
                trainingSystemWeek.add(trainingSystem5DayBefore);
                trainingSystemWeek.add(trainingSystem6DayBefore);
                trainingSystemWeek.add(trainingSystem7DayBefore);

                ArrayList<Date> dates = new ArrayList<>();
                dates.add(date1Before);
                dates.add(date2Before);
                dates.add(date3Before);
                dates.add(date4Before);
                dates.add(date5Before);
                dates.add(date6Before);
                dates.add(date7Before);

                ArrayList<Integer> caloricDemands = new ArrayList<>();
                caloricDemands.add(caloricDemand1dayBefore);
                caloricDemands.add(caloricDemand2dayBefore);
                caloricDemands.add(caloricDemand3dayBefore);
                caloricDemands.add(caloricDemand4dayBefore);
                caloricDemands.add(caloricDemand5dayBefore);
                caloricDemands.add(caloricDemand6dayBefore);
                caloricDemands.add(caloricDemand7dayBefore);

                ArrayList<Integer> goals = new ArrayList<>();
                goals.add(goal1DayBefore);
                goals.add(goal2DayBefore);
                goals.add(goal3DayBefore);
                goals.add(goal4DayBefore);
                goals.add(goal5DayBefore);
                goals.add(goal6DayBefore);
                goals.add(goal7DayBefore);


                WritableFont dateFont = new WritableFont(WritableFont.ARIAL, 12);
                dateFont.setBoldStyle(WritableFont.BOLD);
                WritableCellFormat dateCell = new WritableCellFormat(dateFont);

                WritableFont mealTimeFont = new WritableFont(WritableFont.ARIAL, 11);
                mealTimeFont.setUnderlineStyle(UnderlineStyle.SINGLE);
                WritableCellFormat mealTimeCell = new WritableCellFormat(mealTimeFont);
                mealTimeCell.setAlignment(Alignment.CENTRE);

                WritableCellFormat headerInMealTimeCell = new WritableCellFormat();
                headerInMealTimeCell.setBackground(Colour.LIGHT_GREEN);

                WritableFont noDataFont = new WritableFont(WritableFont.ARIAL);
                noDataFont.setColour(Colour.RED);
                WritableCellFormat noDataCell = new WritableCellFormat(noDataFont);

                NumberFormat decimal = new NumberFormat("0.0");
                WritableCellFormat decimalCell = new WritableCellFormat(decimal);

                WritableCellFormat integerCell = new WritableCellFormat(NumberFormats.INTEGER);

                WritableWorkbook excel = Workbook.createWorkbook(file);
                WritableSheet sheet = excel.createSheet("Podsumowanie", 0);

                int row = 0;
                sheet.addCell(new Label(0, row, "Podsumowanie z dni:", dateCell));
                sheet.addCell(new Label(0, ++row, dateFormat.format(date7Before) + " - " + dateFormat.format(date1Before), dateCell));

                for (int i = 0; i < 7; i++) {
                    sheet.addCell(new Label(0, row += 2, "Data", dateCell));
                    sheet.addCell(new Label(1, row, dateFormat.format(dates.get(i)), dateCell));

                    sheet.addCell(new Label(0, ++row, "Cel"));
                    switch (goals.get(i)){
                        case 0:
                            sheet.addCell(new Label(1, row, "Utrata wagi"));
                            break;
                        case 1:
                            sheet.addCell(new Label(1, row, "Przybranie wagi"));
                            break;
                        case 3:
                            sheet.addCell(new Label(1, row, "Utrzymanie wagi"));
                            break;
                    }

                    ArrayList<ArrayList<FoodSystem>> foodSystemXDayBefore = foodSystemWeek.get(i);
                    ArrayList<TrainingSystem> trainingSystemXDayBefore = trainingSystemWeek.get(i);
                    if (breakfast) {
                        ArrayList<FoodSystem> foodSystemXDayBeforeBreakfast = foodSystemXDayBefore.get(0);

                        row++;
                        sheet.mergeCells(0, row, 1, row);
                        sheet.addCell(new Label(0, row, "Śniadanie", mealTimeCell));

                        if (foodSystemXDayBeforeBreakfast.size() == 0)
                            sheet.addCell(new Label(0, ++row, "Brak danych", noDataCell));
                        else {
                            sheet.addCell(new Label(0, ++row, "Składnik / posiłek", headerInMealTimeCell));
                            sheet.addCell(new Label(1, row, "Waga", headerInMealTimeCell));
                            sheet.addCell(new Label(2, row, "Węglowodany", headerInMealTimeCell));
                            sheet.addCell(new Label(3, row, "Białko", headerInMealTimeCell));
                            sheet.addCell(new Label(4, row, "Tłuszcz", headerInMealTimeCell));
                            sheet.addCell(new Label(5, row, "Kalorie", headerInMealTimeCell));
                            for (int j = 0; j < foodSystemXDayBeforeBreakfast.size(); j++) {
                                sheet.addCell(new Label(0, ++row, foodSystemXDayBeforeBreakfast.get(j).getName()));
                                sheet.addCell(new Number(1, row, foodSystemXDayBeforeBreakfast.get(j).getWeight(), integerCell));
                                if (foodSystemXDayBeforeBreakfast.get(j).getClass() == Ingredient.class) {
                                    Ingredient ingredient = (Ingredient) foodSystemXDayBeforeBreakfast.get(j);
                                    sheet.addCell(new Number(2, row, ingredient.getCarbohydrates() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(3, row, ingredient.getProtein() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(4, row, ingredient.getFat() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(5, row, ingredient.getCalories() * ingredient.getWeight() / 100, integerCell));
                                } else {
                                    Meal meal = (Meal) foodSystemXDayBeforeBreakfast.get(j);
                                    sheet.addCell(new Number(2, row, meal.getCarbohydrates() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(3, row, meal.getProtein() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(4, row, meal.getFat() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(5, row, meal.getCalories() * meal.getWeight() / meal.getTotalWeight(), integerCell));
                                }
                            }
                        }
                    }
                    if (secondBreakfast) {
                        ArrayList<FoodSystem> foodSystemXDayBeforeSecondBreakfast = foodSystemXDayBefore.get(1);

                        row++;
                        sheet.mergeCells(0, row, 1, row);
                        sheet.addCell(new Label(0, row, "Drugie śniadanie", mealTimeCell));

                        if (foodSystemXDayBeforeSecondBreakfast.size() == 0)
                            sheet.addCell(new Label(0, ++row, "Brak danych", noDataCell));
                        else {
                            sheet.addCell(new Label(0, ++row, "Składnik / posiłek", headerInMealTimeCell));
                            sheet.addCell(new Label(1, row, "Waga", headerInMealTimeCell));
                            sheet.addCell(new Label(2, row, "Węglowodany", headerInMealTimeCell));
                            sheet.addCell(new Label(3, row, "Białko", headerInMealTimeCell));
                            sheet.addCell(new Label(4, row, "Tłuszcz", headerInMealTimeCell));
                            sheet.addCell(new Label(5, row, "Kalorie", headerInMealTimeCell));
                            for (int j = 0; j < foodSystemXDayBeforeSecondBreakfast.size(); j++) {
                                sheet.addCell(new Label(0, ++row, foodSystemXDayBeforeSecondBreakfast.get(j).getName()));
                                sheet.addCell(new Number(1, row, foodSystemXDayBeforeSecondBreakfast.get(j).getWeight(), integerCell));
                                if (foodSystemXDayBeforeSecondBreakfast.get(j).getClass() == Ingredient.class) {
                                    Ingredient ingredient = (Ingredient) foodSystemXDayBeforeSecondBreakfast.get(j);
                                    sheet.addCell(new Number(2, row, ingredient.getCarbohydrates() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(3, row, ingredient.getProtein() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(4, row, ingredient.getFat() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(5, row, ingredient.getCalories() * ingredient.getWeight() / 100, integerCell));
                                } else {
                                    Meal meal = (Meal) foodSystemXDayBeforeSecondBreakfast.get(j);
                                    sheet.addCell(new Number(2, row, meal.getCarbohydrates() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(3, row, meal.getProtein() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(4, row, meal.getFat() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(5, row, meal.getCalories() * meal.getWeight() / meal.getTotalWeight(), integerCell));
                                }
                            }
                        }
                    }
                    if (lunch) {
                        ArrayList<FoodSystem> foodSystemXDayBeforeLunch = foodSystemXDayBefore.get(2);

                        row++;
                        sheet.mergeCells(0, row, 1, row);
                        sheet.addCell(new Label(0, row, "Lunch", mealTimeCell));

                        if (foodSystemXDayBeforeLunch.size() == 0)
                            sheet.addCell(new Label(0, ++row, "Brak danych", noDataCell));
                        else {
                            sheet.addCell(new Label(0, ++row, "Składnik / posiłek", headerInMealTimeCell));
                            sheet.addCell(new Label(1, row, "Waga", headerInMealTimeCell));
                            sheet.addCell(new Label(2, row, "Węglowodany", headerInMealTimeCell));
                            sheet.addCell(new Label(3, row, "Białko", headerInMealTimeCell));
                            sheet.addCell(new Label(4, row, "Tłuszcz", headerInMealTimeCell));
                            sheet.addCell(new Label(5, row, "Kalorie", headerInMealTimeCell));
                            for (int j = 0; j < foodSystemXDayBeforeLunch.size(); j++) {
                                sheet.addCell(new Label(0, ++row, foodSystemXDayBeforeLunch.get(j).getName()));
                                sheet.addCell(new Number(1, row, foodSystemXDayBeforeLunch.get(j).getWeight(), integerCell));
                                if (foodSystemXDayBeforeLunch.get(j).getClass() == Ingredient.class) {
                                    Ingredient ingredient = (Ingredient) foodSystemXDayBeforeLunch.get(j);
                                    sheet.addCell(new Number(2, row, ingredient.getCarbohydrates() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(3, row, ingredient.getProtein() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(4, row, ingredient.getFat() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(5, row, ingredient.getCalories() * ingredient.getWeight() / 100, integerCell));
                                } else {
                                    Meal meal = (Meal) foodSystemXDayBeforeLunch.get(j);
                                    sheet.addCell(new Number(2, row, meal.getCarbohydrates() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(3, row, meal.getProtein() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(4, row, meal.getFat() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(5, row, meal.getCalories() * meal.getWeight() / meal.getTotalWeight(), integerCell));
                                }
                            }
                        }
                    }
                    if (dinner) {
                        ArrayList<FoodSystem> foodSystemXDayBeforeDinner = foodSystemXDayBefore.get(3);

                        row++;
                        sheet.mergeCells(0, row, 1, row);
                        sheet.addCell(new Label(0, row, "Obiad", mealTimeCell));

                        if (foodSystemXDayBeforeDinner.size() == 0)
                            sheet.addCell(new Label(0, ++row, "Brak danych", noDataCell));
                        else {
                            sheet.addCell(new Label(0, ++row, "Składnik / posiłek", headerInMealTimeCell));
                            sheet.addCell(new Label(1, row, "Waga", headerInMealTimeCell));
                            sheet.addCell(new Label(2, row, "Węglowodany", headerInMealTimeCell));
                            sheet.addCell(new Label(3, row, "Białko", headerInMealTimeCell));
                            sheet.addCell(new Label(4, row, "Tłuszcz", headerInMealTimeCell));
                            sheet.addCell(new Label(5, row, "Kalorie", headerInMealTimeCell));
                            for (int j = 0; j < foodSystemXDayBeforeDinner.size(); j++) {
                                sheet.addCell(new Label(0, ++row, foodSystemXDayBeforeDinner.get(j).getName()));
                                sheet.addCell(new Number(1, row, foodSystemXDayBeforeDinner.get(j).getWeight(), integerCell));
                                if (foodSystemXDayBeforeDinner.get(j).getClass() == Ingredient.class) {
                                    Ingredient ingredient = (Ingredient) foodSystemXDayBeforeDinner.get(j);
                                    sheet.addCell(new Number(2, row, ingredient.getCarbohydrates() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(3, row, ingredient.getProtein() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(4, row, ingredient.getFat() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(5, row, ingredient.getCalories() * ingredient.getWeight() / 100, integerCell));
                                } else {
                                    Meal meal = (Meal) foodSystemXDayBeforeDinner.get(j);
                                    sheet.addCell(new Number(2, row, meal.getCarbohydrates() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(3, row, meal.getProtein() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(4, row, meal.getFat() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(5, row, meal.getCalories() * meal.getWeight() / meal.getTotalWeight(), integerCell));
                                }
                            }
                        }
                    }
                    if (snack) {
                        ArrayList<FoodSystem> foodSystemXDayBeforeSnack = foodSystemXDayBefore.get(4);

                        row++;
                        sheet.mergeCells(0, row, 1, row);
                        sheet.addCell(new Label(0, row, "Przekąska", mealTimeCell));

                        if (foodSystemXDayBeforeSnack.size() == 0)
                            sheet.addCell(new Label(0, ++row, "Brak danych", noDataCell));
                        else {
                            sheet.addCell(new Label(0, ++row, "Składnik / posiłek", headerInMealTimeCell));
                            sheet.addCell(new Label(1, row, "Waga", headerInMealTimeCell));
                            sheet.addCell(new Label(2, row, "Węglowodany", headerInMealTimeCell));
                            sheet.addCell(new Label(3, row, "Białko", headerInMealTimeCell));
                            sheet.addCell(new Label(4, row, "Tłuszcz", headerInMealTimeCell));
                            sheet.addCell(new Label(5, row, "Kalorie", headerInMealTimeCell));
                            for (int j = 0; j < foodSystemXDayBeforeSnack.size(); j++) {
                                sheet.addCell(new Label(0, ++row, foodSystemXDayBeforeSnack.get(j).getName()));
                                sheet.addCell(new Number(1, row, foodSystemXDayBeforeSnack.get(j).getWeight(), integerCell));
                                if (foodSystemXDayBeforeSnack.get(j).getClass() == Ingredient.class) {
                                    Ingredient ingredient = (Ingredient) foodSystemXDayBeforeSnack.get(j);
                                    sheet.addCell(new Number(2, row, ingredient.getCarbohydrates() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(3, row, ingredient.getProtein() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(4, row, ingredient.getFat() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(5, row, ingredient.getCalories() * ingredient.getWeight() / 100, integerCell));
                                } else {
                                    Meal meal = (Meal) foodSystemXDayBeforeSnack.get(j);
                                    sheet.addCell(new Number(2, row, meal.getCarbohydrates() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(3, row, meal.getProtein() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(4, row, meal.getFat() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(5, row, meal.getCalories() * meal.getWeight() / meal.getTotalWeight(), integerCell));
                                }
                            }
                        }
                    }
                    if (supper) {
                        ArrayList<FoodSystem> foodSystemXDayBeforeSupper = foodSystemXDayBefore.get(5);

                        row++;
                        sheet.mergeCells(0, row, 1, row);
                        sheet.addCell(new Label(0, row, "Kolacja", mealTimeCell));

                        if (foodSystemXDayBeforeSupper.size() == 0)
                            sheet.addCell(new Label(0, ++row, "Brak danych", noDataCell));
                        else {
                            sheet.addCell(new Label(0, ++row, "Składnik / posiłek", headerInMealTimeCell));
                            sheet.addCell(new Label(1, row, "Waga", headerInMealTimeCell));
                            sheet.addCell(new Label(2, row, "Węglowodany", headerInMealTimeCell));
                            sheet.addCell(new Label(3, row, "Białko", headerInMealTimeCell));
                            sheet.addCell(new Label(4, row, "Tłuszcz", headerInMealTimeCell));
                            sheet.addCell(new Label(5, row, "Kalorie", headerInMealTimeCell));
                            for (int j = 0; j < foodSystemXDayBeforeSupper.size(); j++) {
                                sheet.addCell(new Label(0, ++row, foodSystemXDayBeforeSupper.get(j).getName()));
                                sheet.addCell(new Number(1, row, foodSystemXDayBeforeSupper.get(j).getWeight(), integerCell));
                                if (foodSystemXDayBeforeSupper.get(j).getClass() == Ingredient.class) {
                                    Ingredient ingredient = (Ingredient) foodSystemXDayBeforeSupper.get(j);
                                    sheet.addCell(new Number(2, row, ingredient.getCarbohydrates() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(3, row, ingredient.getProtein() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(4, row, ingredient.getFat() * ingredient.getWeight() / 100, decimalCell));
                                    sheet.addCell(new Number(5, row, ingredient.getCalories() * ingredient.getWeight() / 100, integerCell));
                                } else {
                                    Meal meal = (Meal) foodSystemXDayBeforeSupper.get(j);
                                    sheet.addCell(new Number(2, row, meal.getCarbohydrates() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(3, row, meal.getProtein() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(4, row, meal.getFat() * meal.getWeight() / meal.getTotalWeight(), decimalCell));
                                    sheet.addCell(new Number(5, row, meal.getCalories() * meal.getWeight() / meal.getTotalWeight(), integerCell));
                                }
                            }
                        }
                    }
                    if (training) {
                        row++;
                        sheet.mergeCells(0, row, 1, row);
                        sheet.addCell(new Label(0, row, "Trening", mealTimeCell));

                        if (trainingSystemXDayBefore.size() == 0)
                            sheet.addCell(new Label(0, ++row, "Brak danych", noDataCell));
                        else {
                            sheet.addCell(new Label(0, ++row, "Ćwiczenie / trening", headerInMealTimeCell));
                            sheet.addCell(new Label(1, row, "Serie", headerInMealTimeCell));
                            sheet.addCell(new Label(2, row, "Powtórzenia", headerInMealTimeCell));

                            for(int j = 0; j < trainingSystemXDayBefore.size(); j++){
                                if(trainingSystemXDayBefore.get(j).getClass() == Exercise.class){
                                    sheet.addCell(new Label(0, ++row, trainingSystemXDayBefore.get(j).getName()));
                                    Exercise exercise = (Exercise) trainingSystemXDayBefore.get(j);
                                    sheet.addCell(new Number(1, row, exercise.getSeries(), integerCell));
                                    sheet.addCell(new Number(2, row, exercise.getRepetitions(), integerCell));
                                }
                            }

                            for(int j = 0; j < trainingSystemXDayBefore.size(); j++){
                                if(trainingSystemXDayBefore.get(j).getClass() == Training.class){
                                    sheet.addCell(new Label(0, ++row, trainingSystemXDayBefore.get(j).getName()));
                                    sheet.addCell(new Label(1, row, "-"));
                                    sheet.addCell(new Label(2, row, "-"));
                                }
                            }
                        }
                    }
                    if (summary) {
                        row++;
                        sheet.mergeCells(0, row, 1, row);
                        sheet.addCell(new Label(0, row, "Podsumowanie dnia", mealTimeCell));

                        sheet.addCell(new Label(1, ++row, "Zjedzone", headerInMealTimeCell));
                        sheet.addCell(new Label(2, row, "Minimum", headerInMealTimeCell));
                        sheet.addCell(new Label(3, row, "Maksimum", headerInMealTimeCell));

                        sheet.addCell(new Label(0, ++row, "Węglowodany", headerInMealTimeCell));
                        sheet.addCell(new Number(1, row, getCarbohydratesFromDate(foodSystemXDayBefore), decimalCell));
                        sheet.addCell(new Number(2, row, 0.5 * caloricDemands.get(i) / 4, integerCell));
                        sheet.addCell(new Number(3, row, 0.65 * caloricDemands.get(i) / 4, integerCell));

                        sheet.addCell(new Label(0, ++row, "Białko", headerInMealTimeCell));
                        sheet.addCell(new Number(1, row, getProteinFromDate(foodSystemXDayBefore), decimalCell));
                        sheet.addCell(new Number(2, row, 0.15 * caloricDemands.get(i) / 4, integerCell));
                        sheet.addCell(new Number(3, row, 0.25 * caloricDemands.get(i) / 4, integerCell));

                        sheet.addCell(new Label(0, ++row, "Tłuszcz", headerInMealTimeCell));
                        sheet.addCell(new Number(1, row, getFatFromDate(foodSystemXDayBefore), decimalCell));
                        sheet.addCell(new Number(2, row, 0.2 * caloricDemands.get(i) / 9, integerCell));
                        sheet.addCell(new Number(3, row, 0.3 * caloricDemands.get(i) / 9, integerCell));

                        sheet.addCell(new Label(0, ++row, "Kalorie", headerInMealTimeCell));
                        sheet.addCell(new Number(1, row, getCaloriesFromDate(foodSystemXDayBefore), integerCell));
                        sheet.addCell(new Number(2, row, caloricDemands.get(i), integerCell));
                        sheet.addCell(new Label(3, row, "-"));
                    }
                }

                for (int i = 0; i < sheet.getColumns(); i++) {
                    CellView cellView = sheet.getColumnView(i);
                    cellView.setAutosize(true);
                    sheet.setColumnView(i, cellView);
                }
                excel.write();
                excel.close();

                Toast.makeText(getActivity(), "Wygenerowanie plik do: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Brak uprawnień, spróbuj jeszcze raz", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
    }

    private double getCarbohydratesFromDate(ArrayList<ArrayList<FoodSystem>> foodSystemXDayBefore) {
        double carbohydrates = 0;
        for(int i = 0; i < foodSystemXDayBefore.size(); i++){
            ArrayList<FoodSystem> mealTime = foodSystemXDayBefore.get(i);
            for(int j = 0; j < mealTime.size(); j++){
                if(mealTime.get(j).getClass() == Ingredient.class){
                    Ingredient ingredient = (Ingredient) mealTime.get(j);
                    carbohydrates += ingredient.getCarbohydrates() * ingredient.getWeight() / 100;
                } else {
                    Meal meal = (Meal) mealTime.get(j);
                    carbohydrates += meal.getCarbohydrates() * meal.getWeight() / meal.getTotalWeight();
                }
            }
        }
        return carbohydrates;
    }

    private double getProteinFromDate(ArrayList<ArrayList<FoodSystem>> foodSystemXDayBefore) {
        double protein = 0;
        for(int i = 0; i < foodSystemXDayBefore.size(); i++){
            ArrayList<FoodSystem> mealTime = foodSystemXDayBefore.get(i);
            for(int j = 0; j < mealTime.size(); j++){
                if(mealTime.get(j).getClass() == Ingredient.class){
                    Ingredient ingredient = (Ingredient) mealTime.get(j);
                    protein += ingredient.getProtein() * ingredient.getWeight() / 100;
                } else {
                    Meal meal = (Meal) mealTime.get(j);
                    protein += meal.getProtein() * meal.getWeight() / meal.getTotalWeight();
                }
            }
        }
        return protein;
    }

    private double getFatFromDate(ArrayList<ArrayList<FoodSystem>> foodSystemXDayBefore) {
        double fat = 0;
        for(int i = 0; i < foodSystemXDayBefore.size(); i++){
            ArrayList<FoodSystem> mealTime = foodSystemXDayBefore.get(i);
            for(int j = 0; j < mealTime.size(); j++){
                if(mealTime.get(j).getClass() == Ingredient.class){
                    Ingredient ingredient = (Ingredient) mealTime.get(j);
                    fat += ingredient.getFat() * ingredient.getWeight() / 100;
                } else {
                    Meal meal = (Meal) mealTime.get(j);
                    fat += meal.getFat() * meal.getWeight() / meal.getTotalWeight();
                }
            }
        }
        return fat;
    }

    private int getCaloriesFromDate(ArrayList<ArrayList<FoodSystem>> foodSystemXDayBefore) {
        int calories = 0;
        for(int i = 0; i < foodSystemXDayBefore.size(); i++){
            ArrayList<FoodSystem> mealTime = foodSystemXDayBefore.get(i);
            for(int j = 0; j < mealTime.size(); j++){
                if(mealTime.get(j).getClass() == Ingredient.class){
                    Ingredient ingredient = (Ingredient) mealTime.get(j);
                    calories += ingredient.getCalories() * ingredient.getWeight() / 100;
                } else {
                    Meal meal = (Meal) mealTime.get(j);
                    calories += meal.getCalories() * meal.getWeight() / meal.getTotalWeight();
                }
            }
        }
        return calories;
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private void initializeArrays() {
        foodSystem1DayBefore = new ArrayList<>();
        foodSystem2DayBefore = new ArrayList<>();
        foodSystem3DayBefore = new ArrayList<>();
        foodSystem4DayBefore = new ArrayList<>();
        foodSystem5DayBefore = new ArrayList<>();
        foodSystem6DayBefore = new ArrayList<>();
        foodSystem7DayBefore = new ArrayList<>();
        trainingSystem1DayBefore = new ArrayList<>();
        trainingSystem2DayBefore = new ArrayList<>();
        trainingSystem3DayBefore = new ArrayList<>();
        trainingSystem4DayBefore = new ArrayList<>();
        trainingSystem5DayBefore = new ArrayList<>();
        trainingSystem6DayBefore = new ArrayList<>();
        trainingSystem7DayBefore = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            foodSystem1DayBefore.add(new ArrayList<FoodSystem>());
            foodSystem2DayBefore.add(new ArrayList<FoodSystem>());
            foodSystem3DayBefore.add(new ArrayList<FoodSystem>());
            foodSystem4DayBefore.add(new ArrayList<FoodSystem>());
            foodSystem5DayBefore.add(new ArrayList<FoodSystem>());
            foodSystem6DayBefore.add(new ArrayList<FoodSystem>());
            foodSystem7DayBefore.add(new ArrayList<FoodSystem>());
        }
    }


    private void getFoodSystemFromWeek() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 3; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String dateString = row.getString("FoodDate");
                            Date date = sdf.parse(dateString);
                            if (row.getString("type").equals("meal")) {
                                int mealPosition = checkMealPositionInListForDate(row.getInt("ID_MyMeal"), date, row.getInt("MealTime"));
                                Meal tempMeal;
                                if (mealPosition == -1) {
                                    tempMeal = new Meal(row.getInt("ID_MyMeal"), row.getString("MealName"));
                                    Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                            row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                    tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                    tempMeal.addIngredientToList(tempIngredient);
                                    tempMeal.setWeight(row.getInt("Weight"));
                                    addMealToFoodSystemListForDate(tempMeal, date, row.getInt("MealTime"));
                                } else {
                                    Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                            row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                    tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                    updateMealInFoodSystemListForDate(mealPosition, tempIngredient, date, row.getInt("MealTime"));
                                }
                            } else {
                                Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                        row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                tempIngredient.setWeight(row.getInt("Weight"));
                                addIngredientToFoodSystemListForDate(tempIngredient, date, row.getInt("MealTime"));
                            }
                        }
                        getTrainingSystemFromWeek();
                    } else
                        Toast.makeText(getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd połączenia z bazą " + e.toString(), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getFoodSystemFromWeek");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private int checkMealPositionInListForDate(int mealId, Date date, int mealTime) {
        if (date.toString().equals(date1Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystem1DayBefore, mealTime);
        } else if (date.toString().equals(date2Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystem2DayBefore, mealTime);
        } else if (date.toString().equals(date3Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystem3DayBefore, mealTime);
        } else if (date.toString().equals(date4Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystem4DayBefore, mealTime);
        } else if (date.toString().equals(date5Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystem5DayBefore, mealTime);
        } else if (date.toString().equals(date6Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystem6DayBefore, mealTime);
        } else if (date.toString().equals(date7Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystem7DayBefore, mealTime);
        }
        return -1;
    }

    private int checkMealPositionInMealTimeList(int mealId, ArrayList<ArrayList<FoodSystem>> foodSystemXDayBefore, int mealTime) {
        Meal tempMeal;
        ArrayList<FoodSystem> tempList;
        switch (mealTime) {
            case 0:
                tempList = foodSystemXDayBefore.get(0);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 1:
                tempList = foodSystemXDayBefore.get(1);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 2:
                tempList = foodSystemXDayBefore.get(2);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 3:
                tempList = foodSystemXDayBefore.get(3);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 4:
                tempList = foodSystemXDayBefore.get(4);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 5:
                tempList = foodSystemXDayBefore.get(5);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
        }
        return -1;
    }

    private void addMealToFoodSystemListForDate(Meal meal, Date date, int mealTime) {
        if (date.toString().equals(date1Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystem1DayBefore, mealTime);
        } else if (date.toString().equals(date2Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystem2DayBefore, mealTime);
        } else if (date.toString().equals(date3Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystem3DayBefore, mealTime);
        } else if (date.toString().equals(date4Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystem4DayBefore, mealTime);
        } else if (date.toString().equals(date5Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystem5DayBefore, mealTime);
        } else if (date.toString().equals(date6Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystem6DayBefore, mealTime);
        } else if (date.toString().equals(date7Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystem7DayBefore, mealTime);
        }
    }

    private void addMealToFoodSystemMealTimeList(Meal meal, ArrayList<ArrayList<FoodSystem>> foodSystemXDayBefore, int mealTime) {
        switch (mealTime) {
            case 0:
                foodSystemXDayBefore.get(0).add(meal);
                break;
            case 1:
                foodSystemXDayBefore.get(1).add(meal);
                break;
            case 2:
                foodSystemXDayBefore.get(2).add(meal);
                break;
            case 3:
                foodSystemXDayBefore.get(3).add(meal);
                break;
            case 4:
                foodSystemXDayBefore.get(4).add(meal);
                break;
            case 5:
                foodSystemXDayBefore.get(5).add(meal);
                break;
        }
    }

    private void updateMealInFoodSystemListForDate(int position, Ingredient ingredient, Date date, int mealTime) {
        if (date.toString().equals(date1Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystem1DayBefore, mealTime);
        } else if (date.toString().equals(date2Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystem2DayBefore, mealTime);
        } else if (date.toString().equals(date3Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystem3DayBefore, mealTime);
        } else if (date.toString().equals(date4Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystem4DayBefore, mealTime);
        } else if (date.toString().equals(date5Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystem5DayBefore, mealTime);
        } else if (date.toString().equals(date6Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystem6DayBefore, mealTime);
        } else if (date.toString().equals(date7Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystem7DayBefore, mealTime);
        }
    }

    private void updateMealInFoodSystemMealTimeList(int position, Ingredient ingredient, ArrayList<ArrayList<FoodSystem>> foodSystemXDayBefore, int mealTime) {
        Meal tempMeal;
        switch (mealTime) {
            case 0:
                tempMeal = (Meal) foodSystemXDayBefore.get(0).get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 1:
                tempMeal = (Meal) foodSystemXDayBefore.get(1).get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 2:
                tempMeal = (Meal) foodSystemXDayBefore.get(2).get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 3:
                tempMeal = (Meal) foodSystemXDayBefore.get(3).get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 4:
                tempMeal = (Meal) foodSystemXDayBefore.get(4).get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 5:
                tempMeal = (Meal) foodSystemXDayBefore.get(5).get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
        }
    }

    private void addIngredientToFoodSystemListForDate(Ingredient ingredient, Date date, int mealTime) {
        if (date.toString().equals(date1Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystem1DayBefore, mealTime);
        } else if (date.toString().equals(date2Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystem2DayBefore, mealTime);
        } else if (date.toString().equals(date3Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystem3DayBefore, mealTime);
        } else if (date.toString().equals(date4Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystem4DayBefore, mealTime);
        } else if (date.toString().equals(date5Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystem5DayBefore, mealTime);
        } else if (date.toString().equals(date6Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystem6DayBefore, mealTime);
        } else if (date.toString().equals(date7Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystem7DayBefore, mealTime);
        }
    }

    private void addIngredientToFoodSystemMealTimeList(Ingredient ingredient, ArrayList<ArrayList<FoodSystem>> foodSystemXDayBefore, int mealTime) {
        switch (mealTime) {
            case 0:
                foodSystemXDayBefore.get(0).add(ingredient);
                break;
            case 1:
                foodSystemXDayBefore.get(1).add(ingredient);
                break;
            case 2:
                foodSystemXDayBefore.get(2).add(ingredient);
                break;
            case 3:
                foodSystemXDayBefore.get(3).add(ingredient);
                break;
            case 4:
                foodSystemXDayBefore.get(4).add(ingredient);
                break;
            case 5:
                foodSystemXDayBefore.get(5).add(ingredient);
                break;
        }
    }


    private void getTrainingSystemFromWeek() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 3; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String dateString = row.getString("TrainingDate");
                            Date date = sdf.parse(dateString);
                            if (row.getString("type").equals("training")) {
                                int trainingPosition = checkTrainingPositionInListForDate(row.getInt("ID_MyTraining"), date);
                                if (trainingPosition == -1) {
                                    Training tempTraining = new Training(row.getInt("ID_MyTraining"), row.getString("TrainingName"));
                                    Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                    tempExercise.setSeries(row.getInt("Series"));
                                    tempExercise.setRepetitions(row.getInt("Repetitions"));
                                    tempTraining.addExerciseToList(tempExercise);
                                    addTrainingToTrainingSystemListForDate(tempTraining, date);
                                } else {
                                    Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                    tempExercise.setSeries(row.getInt("Series"));
                                    tempExercise.setRepetitions(row.getInt("Repetitions"));
                                    updateTrainingInTrainingSystemListForDate(trainingPosition, tempExercise, date);
                                }
                            } else {
                                Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                tempExercise.setSeries(row.getInt("Series"));
                                tempExercise.setRepetitions(row.getInt("Repetitions"));
                                addExerciseToFoodSystemListForDate(tempExercise, date);
                            }
                        }
                        getCaloricDemandFromWeek();
                    } else
                        Toast.makeText(getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd połączenia z bazą " + e.toString(), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getTrainingSystemFromWeek");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private int checkTrainingPositionInListForDate(int trainingId, Date date) {
        if (date.toString().equals(date1Before.toString())) {
            return checkTrainingPositionInList(trainingSystem1DayBefore, trainingId);
        } else if (date.toString().equals(date2Before.toString())) {
            return checkTrainingPositionInList(trainingSystem2DayBefore, trainingId);
        } else if (date.toString().equals(date3Before.toString())) {
            return checkTrainingPositionInList(trainingSystem3DayBefore, trainingId);
        } else if (date.toString().equals(date4Before.toString())) {
            return checkTrainingPositionInList(trainingSystem4DayBefore, trainingId);
        } else if (date.toString().equals(date5Before.toString())) {
            return checkTrainingPositionInList(trainingSystem5DayBefore, trainingId);
        } else if (date.toString().equals(date6Before.toString())) {
            return checkTrainingPositionInList(trainingSystem6DayBefore, trainingId);
        } else if (date.toString().equals(date7Before.toString())) {
            return checkTrainingPositionInList(trainingSystem7DayBefore, trainingId);
        }
        return -1;
    }

    private int checkTrainingPositionInList(ArrayList<TrainingSystem> trainingSystemXDayBefore, int trainingId) {
        Training tempTraining;
        for (int i = 0; i < trainingSystemXDayBefore.size(); i++) {
            if (trainingSystemXDayBefore.get(i).getClass() == Training.class) {
                tempTraining = (Training) trainingSystemXDayBefore.get(i);
                if (tempTraining.getID() == trainingId)
                    return i;
            }
        }
        return -1;
    }

    private void addTrainingToTrainingSystemListForDate(Training tempTraining, Date date) {
        if (date.toString().equals(date1Before.toString())) {
            trainingSystem1DayBefore.add(tempTraining);
        } else if (date.toString().equals(date2Before.toString())) {
            trainingSystem2DayBefore.add(tempTraining);
        } else if (date.toString().equals(date3Before.toString())) {
            trainingSystem3DayBefore.add(tempTraining);
        } else if (date.toString().equals(date4Before.toString())) {
            trainingSystem4DayBefore.add(tempTraining);
        } else if (date.toString().equals(date5Before.toString())) {
            trainingSystem5DayBefore.add(tempTraining);
        } else if (date.toString().equals(date6Before.toString())) {
            trainingSystem6DayBefore.add(tempTraining);
        } else if (date.toString().equals(date7Before.toString())) {
            trainingSystem7DayBefore.add(tempTraining);
        }
    }

    private void updateTrainingInTrainingSystemListForDate(int trainingPosition, Exercise tempExercise, Date date) {
        Training tempTraining;
        if (date.toString().equals(date1Before.toString())) {
            tempTraining = (Training) trainingSystem1DayBefore.get(trainingPosition);
            tempTraining.addExerciseToList(tempExercise);
        } else if (date.toString().equals(date2Before.toString())) {
            tempTraining = (Training) trainingSystem2DayBefore.get(trainingPosition);
            tempTraining.addExerciseToList(tempExercise);
        } else if (date.toString().equals(date3Before.toString())) {
            tempTraining = (Training) trainingSystem3DayBefore.get(trainingPosition);
            tempTraining.addExerciseToList(tempExercise);
        } else if (date.toString().equals(date4Before.toString())) {
            tempTraining = (Training) trainingSystem4DayBefore.get(trainingPosition);
            tempTraining.addExerciseToList(tempExercise);
        } else if (date.toString().equals(date5Before.toString())) {
            tempTraining = (Training) trainingSystem5DayBefore.get(trainingPosition);
            tempTraining.addExerciseToList(tempExercise);
        } else if (date.toString().equals(date6Before.toString())) {
            tempTraining = (Training) trainingSystem6DayBefore.get(trainingPosition);
            tempTraining.addExerciseToList(tempExercise);
        } else if (date.toString().equals(date7Before.toString())) {
            tempTraining = (Training) trainingSystem7DayBefore.get(trainingPosition);
            tempTraining.addExerciseToList(tempExercise);
        }
    }

    private void addExerciseToFoodSystemListForDate(Exercise tempExercise, Date date) {
        if (date.toString().equals(date1Before.toString())) {
            trainingSystem1DayBefore.add(tempExercise);
        } else if (date.toString().equals(date2Before.toString())) {
            trainingSystem2DayBefore.add(tempExercise);
        } else if (date.toString().equals(date3Before.toString())) {
            trainingSystem3DayBefore.add(tempExercise);
        } else if (date.toString().equals(date4Before.toString())) {
            trainingSystem4DayBefore.add(tempExercise);
        } else if (date.toString().equals(date5Before.toString())) {
            trainingSystem5DayBefore.add(tempExercise);
        } else if (date.toString().equals(date6Before.toString())) {
            trainingSystem6DayBefore.add(tempExercise);
        } else if (date.toString().equals(date7Before.toString())) {
            trainingSystem7DayBefore.add(tempExercise);
        }
    }


    private void getCaloricDemandFromWeek() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String dateString = row.getString("WeightDate");
                            Date date = sdf.parse(dateString);
                            addCaloricDemandForDate(row.getInt("CaloricDemend"), date);
                        }
                        getGoalFromWeek();
                    } else
                        Toast.makeText(getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd połączenia z bazą " + e.toString(), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getCaloricDemendFromWeek");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void addCaloricDemandForDate(int caloricDemand, Date date) {
        if (date.toString().equals(date1Before.toString())) {
            caloricDemand1dayBefore = caloricDemand;
        } else if (date.toString().equals(date2Before.toString())) {
            caloricDemand2dayBefore = caloricDemand;
        } else if (date.toString().equals(date3Before.toString())) {
            caloricDemand3dayBefore = caloricDemand;
        } else if (date.toString().equals(date4Before.toString())) {
            caloricDemand4dayBefore = caloricDemand;
        } else if (date.toString().equals(date5Before.toString())) {
            caloricDemand5dayBefore = caloricDemand;
        } else if (date.toString().equals(date6Before.toString())) {
            caloricDemand6dayBefore = caloricDemand;
        } else if (date.toString().equals(date7Before.toString())) {
            caloricDemand7dayBefore = caloricDemand;
        }
    }


    private void getGoalFromWeek() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String dateString = row.getString("WeightDate");
                            Date date = sdf.parse(dateString);
                            addGoalForDate(row.getInt("Goal"), date);
                        }
                        pbLoading.setVisibility(View.GONE);
                        llOptions.setVisibility(View.VISIBLE);
                    } else
                        Toast.makeText(getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd połączenia z bazą " + e.toString(), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getGoalFromWeek");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void addGoalForDate(int goal, Date date) {
        if (date.toString().equals(date1Before.toString())) {
            goal1DayBefore = goal;
        } else if (date.toString().equals(date2Before.toString())) {
            goal2DayBefore = goal;
        } else if (date.toString().equals(date3Before.toString())) {
            goal3DayBefore = goal;
        } else if (date.toString().equals(date4Before.toString())) {
            goal4DayBefore = goal;
        } else if (date.toString().equals(date5Before.toString())) {
            goal5DayBefore = goal;
        } else if (date.toString().equals(date6Before.toString())) {
            goal6DayBefore = goal;
        } else if (date.toString().equals(date7Before.toString())) {
            goal7DayBefore = goal;
        }
    }
}
