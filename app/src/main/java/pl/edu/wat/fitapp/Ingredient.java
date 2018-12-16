package pl.edu.wat.fitapp;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private int ingredientId;
    private String ingredientName;
    private double ingredientCarbohydrates;
    private double ingredientProtein;
    private double ingredientFat;
    private int ingredientCalories;

    public Ingredient(int ingredientId, String ingredientName, double ingredientCarbohydrates, double ingredientProtein, double ingredientFat, int ingredientCalories) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.ingredientCarbohydrates = ingredientCarbohydrates;
        this.ingredientProtein = ingredientProtein;
        this.ingredientFat = ingredientFat;
        this.ingredientCalories = ingredientCalories;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public double getIngredientCarbohydrates() {
        return ingredientCarbohydrates;
    }

    public double getIngredientProtein() {
        return ingredientProtein;
    }

    public double getIngredientFat() {
        return ingredientFat;
    }

    public int getIngredientCalories() {
        return ingredientCalories;
    }
}
