package pl.edu.wat.fitapp;

import java.io.Serializable;

public class Ingredient implements Serializable, FoodSystem {
    private int ID;
    private String name;
    private double carbohydrates;
    private double protein;
    private double fat;
    private int calories;

    public Ingredient(int ingredientId, String ingredientName, double ingredientCarbohydrates, double ingredientProtein, double ingredientFat, int ingredientCalories) {
        this.ID = ingredientId;
        this.name = ingredientName;
        this.carbohydrates = ingredientCarbohydrates;
        this.protein = ingredientProtein;
        this.fat = ingredientFat;
        this.calories = ingredientCalories;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public int getCalories() {
        return calories;
    }
}
