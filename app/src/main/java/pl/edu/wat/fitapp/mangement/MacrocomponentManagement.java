package pl.edu.wat.fitapp.mangement;

import java.util.ArrayList;

import pl.edu.wat.fitapp.database.entity.Ingredient;
import pl.edu.wat.fitapp.database.entity.Meal;
import pl.edu.wat.fitapp.interfaces.FoodSystem;

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

    public int getCaloriesForMealTimeFromDay(ArrayList<FoodSystem> foodSystemMealTime){
        int calories = 0;
        for(int i = 0; i < foodSystemMealTime.size(); i++){
            if(foodSystemMealTime.get(i).getClass() == Ingredient.class)
                calories += foodSystemMealTime.get(i).getCalories() * foodSystemMealTime.get(i).getWeight() / 100;
            else {
                Meal tempMeal = (Meal) foodSystemMealTime.get(i);
                calories += tempMeal.getCalories() * tempMeal.getWeight() / tempMeal.getTotalWeight();
            }
        }
        return calories;
    }

    public double getCarbohydratesForMealTimeFromDay(ArrayList<FoodSystem> foodSystemMealTime){
        double carbohydrates = 0;
        for(int i = 0; i < foodSystemMealTime.size(); i++){
            if(foodSystemMealTime.get(i).getClass() == Ingredient.class)
                carbohydrates += foodSystemMealTime.get(i).getCarbohydrates() * foodSystemMealTime.get(i).getWeight() / 100;
            else {
                Meal tempMeal = (Meal) foodSystemMealTime.get(i);
                carbohydrates += tempMeal.getCarbohydrates() * tempMeal.getWeight() / tempMeal.getTotalWeight();
            }
        }
        return carbohydrates;
    }

    public double getProteinForMealTimeFromDay(ArrayList<FoodSystem> foodSystemMealTime){
        double protein = 0;
        for(int i = 0; i < foodSystemMealTime.size(); i++){
            if(foodSystemMealTime.get(i).getClass() == Ingredient.class)
                protein += foodSystemMealTime.get(i).getProtein() * foodSystemMealTime.get(i).getWeight() / 100;
            else {
                Meal tempMeal = (Meal) foodSystemMealTime.get(i);
                protein += tempMeal.getProtein() * tempMeal.getWeight() / tempMeal.getTotalWeight();
            }
        }
        return protein;
    }

    public double getFatForMealTimeFromDay(ArrayList<FoodSystem> foodSystemMealTime){
        double fat = 0;
        for(int i = 0; i < foodSystemMealTime.size(); i++){
            if(foodSystemMealTime.get(i).getClass() == Ingredient.class)
                fat += foodSystemMealTime.get(i).getFat() * foodSystemMealTime.get(i).getWeight() / 100;
            else {
                Meal tempMeal = (Meal) foodSystemMealTime.get(i);
                fat += tempMeal.getFat() * tempMeal.getWeight() / tempMeal.getTotalWeight();
            }
        }
        return fat;
    }

    public int getCaloriesForMeal(ArrayList<Ingredient> mealIngredients){
        int calories = 0;
        for(int i = 0; i < mealIngredients.size(); i++){
            Ingredient ingredient = mealIngredients.get(i);
            calories += ingredient.getCalories() * ingredient.getWeight() / 100;
        }
        return calories;
    }

    public double getCarbohydratesForMeal(ArrayList<Ingredient> mealIngredients){
        double carbohydrates = 0;
        for(int i = 0; i < mealIngredients.size(); i++){
            Ingredient ingredient = mealIngredients.get(i);
            carbohydrates += ingredient.getCarbohydrates() * ingredient.getWeight() / 100;
        }
        return carbohydrates;
    }

    public double getProteinForMeal(ArrayList<Ingredient> mealIngredients){
        double protein = 0;
        for(int i = 0; i < mealIngredients.size(); i++){
            Ingredient ingredient = mealIngredients.get(i);
            protein += ingredient.getProtein() * ingredient.getWeight() / 100;
        }
        return protein;
    }

    public double getFatForMeal(ArrayList<Ingredient> mealIngredients){
        double fat = 0;
        for(int i = 0; i < mealIngredients.size(); i++){
            Ingredient ingredient = mealIngredients.get(i);
            fat += ingredient.getFat() * ingredient.getWeight() / 100;
        }
        return fat;
    }
}
