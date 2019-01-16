package pl.edu.wat.fitapp.Mangement;

import java.util.ArrayList;

import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.Database.Entity.Meal;
import pl.edu.wat.fitapp.Interface.FoodSystem;

public class FoodSystemDayManagement {

    public int checkMealPositionInList(int mealId, int mealTime, ArrayList<ArrayList<FoodSystem>> foodSystem) {
        Meal tempMeal;
        ArrayList<FoodSystem> tempList;
        switch (mealTime) {
            case 0:
                tempList = foodSystem.get(0);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 1:
                tempList = foodSystem.get(1);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 2:
                tempList = foodSystem.get(2);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 3:
                tempList = foodSystem.get(3);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 4:
                tempList = foodSystem.get(4);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 5:
                tempList = foodSystem.get(5);
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

    public void addMealToFoodSystemList(Meal meal, int mealTime, ArrayList<ArrayList<FoodSystem>> foodSystem) {
        switch (mealTime) {
            case 0:
                foodSystem.get(0).add(meal);
                break;
            case 1:
                foodSystem.get(1).add(meal);
                break;
            case 2:
                foodSystem.get(2).add(meal);
                break;
            case 3:
                foodSystem.get(3).add(meal);
                break;
            case 4:
                foodSystem.get(4).add(meal);
                break;
            case 5:
                foodSystem.get(5).add(meal);
                break;
        }
    }

    public void updateMealInFoodSystemList(int position, Ingredient ingredient, int mealTime, ArrayList<ArrayList<FoodSystem>> foodSystem) {
        Meal tempMeal;
        ArrayList<FoodSystem> tempList;
        switch (mealTime) {
            case 0:
                tempList = foodSystem.get(0);
                tempMeal = (Meal) tempList.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 1:
                tempList = foodSystem.get(1);
                tempMeal = (Meal) tempList.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 2:
                tempList = foodSystem.get(2);
                tempMeal = (Meal) tempList.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 3:
                tempList = foodSystem.get(3);
                tempMeal = (Meal) tempList.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 4:
                tempList = foodSystem.get(4);
                tempMeal = (Meal) tempList.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 5:
                tempList = foodSystem.get(5);
                tempMeal = (Meal) tempList.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
        }
    }

    public void addIngredientToFoodSystemList(Ingredient ingredient, int mealTime, ArrayList<ArrayList<FoodSystem>> foodSystem) {
        switch (mealTime) {
            case 0:
                foodSystem.get(0).add(ingredient);
                break;
            case 1:
                foodSystem.get(1).add(ingredient);
                break;
            case 2:
                foodSystem.get(2).add(ingredient);
                break;
            case 3:
                foodSystem.get(3).add(ingredient);
                break;
            case 4:
                foodSystem.get(4).add(ingredient);
                break;
            case 5:
                foodSystem.get(5).add(ingredient);
                break;
        }
    }


}
