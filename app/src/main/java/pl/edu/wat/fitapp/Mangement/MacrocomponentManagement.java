package pl.edu.wat.fitapp.Mangement;

import java.util.ArrayList;

import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.Database.Entity.Meal;
import pl.edu.wat.fitapp.Interface.FoodSystem;

public class MacrocomponentManagement {

    public int getCaloriesFromDay(ArrayList<ArrayList<FoodSystem>> foodSystemDay) {
        int calories = 0;

        for (int i = 0; i < foodSystemDay.size(); i++) {
            ArrayList<FoodSystem> tempList = foodSystemDay.get(i);
            for (int j = 0; j < tempList.size(); j++) {
                if (tempList.get(j).getClass() == Ingredient.class) {
                    calories += tempList.get(j).getCalories() * tempList.get(j).getWeight() / 100;
                } else {
                    Meal tempMeal = (Meal) tempList.get(j);
                    calories += tempMeal.getCalories() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                }
            }
        }
        return calories;
    }

    public double getCarbohydratesFromDay(ArrayList<ArrayList<FoodSystem>> foodSystemDay) {
        double carbohydrates = 0;

        for (int i = 0; i < foodSystemDay.size(); i++) {
            ArrayList<FoodSystem> tempList = foodSystemDay.get(i);
            for (int j = 0; j < tempList.size(); j++) {
                if (tempList.get(j).getClass() == Ingredient.class) {
                    carbohydrates += tempList.get(j).getCarbohydrates() * tempList.get(j).getWeight() / 100;
                } else {
                    Meal tempMeal = (Meal) tempList.get(j);
                    carbohydrates += tempMeal.getCarbohydrates() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                }
            }
        }
        return carbohydrates;
    }

    public double getProteinFromDay(ArrayList<ArrayList<FoodSystem>> foodSystemDay) {
        double protein = 0;

        for (int i = 0; i < foodSystemDay.size(); i++) {
            ArrayList<FoodSystem> tempList = foodSystemDay.get(i);
            for (int j = 0; j < tempList.size(); j++) {
                if (tempList.get(j).getClass() == Ingredient.class) {
                    protein += tempList.get(j).getProtein() * tempList.get(j).getWeight() / 100;
                } else {
                    Meal tempMeal = (Meal) tempList.get(j);
                    protein += tempMeal.getProtein() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                }
            }
        }
        return protein;
    }

    public double getFatFromDay(ArrayList<ArrayList<FoodSystem>> foodSystemDay) {
        double fat = 0;

        for (int i = 0; i < foodSystemDay.size(); i++) {
            ArrayList<FoodSystem> tempList = foodSystemDay.get(i);
            for (int j = 0; j < tempList.size(); j++) {
                if (tempList.get(j).getClass() == Ingredient.class) {
                    fat += tempList.get(j).getFat() * tempList.get(j).getWeight() / 100;
                } else {
                    Meal tempMeal = (Meal) tempList.get(j);
                    fat += tempMeal.getFat() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                }
            }
        }
        return fat;
    }
}
