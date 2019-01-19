package pl.edu.wat.fitapp.DataExport;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import pl.edu.wat.fitapp.Interface.FoodSystem;
import pl.edu.wat.fitapp.Interface.TrainingSystem;
import pl.edu.wat.fitapp.Mangement.MacrocomponentManagement;

public class PDFGenerator {
    private Fragment fragment;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ArrayList<Date> dates;

    public PDFGenerator(Fragment fragment) {
        this.fragment = fragment;

        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        dates = new ArrayList<>();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        dates.add(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        dates.add(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        dates.add(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        dates.add(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        dates.add(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        dates.add(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        dates.add(calendar.getTime());
    }

    public void generate(ArrayList<Boolean> options, ArrayList<ArrayList<ArrayList<FoodSystem>>> foodSystemWeek, ArrayList<ArrayList<TrainingSystem>> trainingSystemWeek,
                          ArrayList<Integer> caloricDemandWeek, ArrayList<Integer> goalWeek) {
        verifyStoragePermissions(fragment.getActivity());
        try {
            MacrocomponentManagement macroMgn = new MacrocomponentManagement();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(path, "excel.xls");

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
            sheet.addCell(new Label(0, ++row, dateFormat.format(dates.get(dates.size() - 1)) + " - " + dateFormat.format(dates.get(0)), dateCell));

            for (int i = 0; i < 7; i++) {
                sheet.addCell(new Label(0, row += 2, "Data", dateCell));
                sheet.addCell(new Label(1, row, dateFormat.format(dates.get(i)), dateCell));

                sheet.addCell(new Label(0, ++row, "Cel"));
                switch (goalWeek.get(i)) {
                    case 0:
                        sheet.addCell(new Label(1, row, "Utrata wagi"));
                        break;
                    case 1:
                        sheet.addCell(new Label(1, row, "Przybranie wagi"));
                        break;
                    case 2:
                        sheet.addCell(new Label(1, row, "Utrzymanie wagi"));
                        break;
                }

                ArrayList<ArrayList<FoodSystem>> foodSystemXDayBefore = foodSystemWeek.get(i);
                ArrayList<TrainingSystem> trainingSystemXDayBefore = trainingSystemWeek.get(i);
                if (options.get(0)) {
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
                if (options.get(1)) {
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
                if (options.get(2)) {
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
                if (options.get(3)) {
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
                if (options.get(4)) {
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
                if (options.get(5)) {
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
                if (options.get(6)) {
                    row++;
                    sheet.mergeCells(0, row, 1, row);
                    sheet.addCell(new Label(0, row, "Trening", mealTimeCell));

                    if (trainingSystemXDayBefore.size() == 0)
                        sheet.addCell(new Label(0, ++row, "Brak danych", noDataCell));
                    else {
                        sheet.addCell(new Label(0, ++row, "Ćwiczenie / trening", headerInMealTimeCell));
                        sheet.addCell(new Label(1, row, "Serie", headerInMealTimeCell));
                        sheet.addCell(new Label(2, row, "Powtórzenia", headerInMealTimeCell));

                        for (int j = 0; j < trainingSystemXDayBefore.size(); j++) {
                            if (trainingSystemXDayBefore.get(j).getClass() == Exercise.class) {
                                sheet.addCell(new Label(0, ++row, trainingSystemXDayBefore.get(j).getName()));
                                Exercise exercise = (Exercise) trainingSystemXDayBefore.get(j);
                                sheet.addCell(new Number(1, row, exercise.getSeries(), integerCell));
                                sheet.addCell(new Number(2, row, exercise.getRepetitions(), integerCell));
                            }
                        }

                        for (int j = 0; j < trainingSystemXDayBefore.size(); j++) {
                            if (trainingSystemXDayBefore.get(j).getClass() == Training.class) {
                                sheet.addCell(new Label(0, ++row, trainingSystemXDayBefore.get(j).getName()));
                                sheet.addCell(new Label(1, row, "-"));
                                sheet.addCell(new Label(2, row, "-"));
                            }
                        }
                    }
                }
                if (options.get(7)) {
                    row++;
                    sheet.mergeCells(0, row, 1, row);
                    sheet.addCell(new Label(0, row, "Podsumowanie dnia", mealTimeCell));

                    sheet.addCell(new Label(1, ++row, "Zjedzone", headerInMealTimeCell));
                    sheet.addCell(new Label(2, row, "Minimum", headerInMealTimeCell));
                    sheet.addCell(new Label(3, row, "Maksimum", headerInMealTimeCell));

                    sheet.addCell(new Label(0, ++row, "Węglowodany", headerInMealTimeCell));
                    sheet.addCell(new Number(1, row, macroMgn.getCarbohydratesFromDay(foodSystemXDayBefore), decimalCell));
                    sheet.addCell(new Number(2, row, 0.5 * caloricDemandWeek.get(i) / 4, integerCell));
                    sheet.addCell(new Number(3, row, 0.65 * caloricDemandWeek.get(i) / 4, integerCell));

                    sheet.addCell(new Label(0, ++row, "Białko", headerInMealTimeCell));
                    sheet.addCell(new Number(1, row, macroMgn.getProteinFromDay(foodSystemXDayBefore), decimalCell));
                    sheet.addCell(new Number(2, row, 0.15 * caloricDemandWeek.get(i) / 4, integerCell));
                    sheet.addCell(new Number(3, row, 0.25 * caloricDemandWeek.get(i) / 4, integerCell));

                    sheet.addCell(new Label(0, ++row, "Tłuszcz", headerInMealTimeCell));
                    sheet.addCell(new Number(1, row, macroMgn.getFatFromDay(foodSystemXDayBefore), decimalCell));
                    sheet.addCell(new Number(2, row, 0.2 * caloricDemandWeek.get(i) / 9, integerCell));
                    sheet.addCell(new Number(3, row, 0.3 * caloricDemandWeek.get(i) / 9, integerCell));

                    sheet.addCell(new Label(0, ++row, "Kalorie", headerInMealTimeCell));
                    sheet.addCell(new Number(1, row, macroMgn.getCaloriesFromDay(foodSystemXDayBefore), integerCell));
                    sheet.addCell(new Number(2, row, caloricDemandWeek.get(i), integerCell));
                    sheet.addCell(new Number(3, row, caloricDemandWeek.get(i), integerCell));
                }
            }

            for (int i = 0; i < sheet.getColumns(); i++) {
                CellView cellView = sheet.getColumnView(i);
                cellView.setAutosize(true);
                sheet.setColumnView(i, cellView);
            }
            excel.write();
            excel.close();

            Toast.makeText(fragment.getActivity(), "Wygenerowanie plik do: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(fragment.getActivity(), "Brak uprawnień, spróbuj jeszcze raz", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    private static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}