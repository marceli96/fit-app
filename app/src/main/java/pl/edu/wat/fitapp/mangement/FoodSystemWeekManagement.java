package pl.edu.wat.fitapp.mangement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pl.edu.wat.fitapp.database.entity.Ingredient;
import pl.edu.wat.fitapp.database.entity.Meal;
import pl.edu.wat.fitapp.interfaces.FoodSystem;

public class FoodSystemWeekManagement {
    private Date date1Before, date2Before, date3Before, date4Before, date5Before, date6Before, date7Before;

    public FoodSystemWeekManagement() {
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
    }

    public int checkMealPositionInListForDate(int mealId, Date date, int mealTime, ArrayList<ArrayList<ArrayList<FoodSystem>>> foodSystemWeek) {
        if (date.toString().equals(date1Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystemWeek.get(0), mealTime);
        } else if (date.toString().equals(date2Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystemWeek.get(1), mealTime);
        } else if (date.toString().equals(date3Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystemWeek.get(2), mealTime);
        } else if (date.toString().equals(date4Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystemWeek.get(3), mealTime);
        } else if (date.toString().equals(date5Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystemWeek.get(4), mealTime);
        } else if (date.toString().equals(date6Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystemWeek.get(5), mealTime);
        } else if (date.toString().equals(date7Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystemWeek.get(6), mealTime);
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


    public void addMealToFoodSystemListForDate(Meal meal, Date date, int mealTime, ArrayList<ArrayList<ArrayList<FoodSystem>>> foodSystemWeek) {
        if (date.toString().equals(date1Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystemWeek.get(0), mealTime);
        } else if (date.toString().equals(date2Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystemWeek.get(1), mealTime);
        } else if (date.toString().equals(date3Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystemWeek.get(2), mealTime);
        } else if (date.toString().equals(date4Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystemWeek.get(3), mealTime);
        } else if (date.toString().equals(date5Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystemWeek.get(4), mealTime);
        } else if (date.toString().equals(date6Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystemWeek.get(5), mealTime);
        } else if (date.toString().equals(date7Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystemWeek.get(6), mealTime);
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


    public void updateMealInFoodSystemListForDate(int position, Ingredient ingredient, Date date, int mealTime, ArrayList<ArrayList<ArrayList<FoodSystem>>> foodSystemWeek) {
        if (date.toString().equals(date1Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystemWeek.get(0), mealTime);
        } else if (date.toString().equals(date2Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystemWeek.get(1), mealTime);
        } else if (date.toString().equals(date3Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystemWeek.get(2), mealTime);
        } else if (date.toString().equals(date4Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystemWeek.get(3), mealTime);
        } else if (date.toString().equals(date5Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystemWeek.get(4), mealTime);
        } else if (date.toString().equals(date6Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystemWeek.get(5), mealTime);
        } else if (date.toString().equals(date7Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystemWeek.get(6), mealTime);
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


    public void addIngredientToFoodSystemListForDate(Ingredient ingredient, Date date, int mealTime, ArrayList<ArrayList<ArrayList<FoodSystem>>> foodSystemWeek) {
        if (date.toString().equals(date1Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystemWeek.get(0), mealTime);
        } else if (date.toString().equals(date2Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystemWeek.get(1), mealTime);
        } else if (date.toString().equals(date3Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystemWeek.get(2), mealTime);
        } else if (date.toString().equals(date4Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystemWeek.get(3), mealTime);
        } else if (date.toString().equals(date5Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystemWeek.get(4), mealTime);
        } else if (date.toString().equals(date6Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystemWeek.get(5), mealTime);
        } else if (date.toString().equals(date7Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystemWeek.get(6), mealTime);
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
}
