package pl.edu.wat.fitapp;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private int ingredientId;
    private String ingredientName;
    private int ingredientCarbohydrates;
    private int ingredientProtein;
    private int ingredientFat;
    private int ingredientCalories;

    public Ingredient(int ingredientId, String ingredientName, int ingredientCarbohydrates, int ingredientProtein, int ingredientFat, int ingredientCalories) {
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

    public int getIngredientCarbohydrates() {
        return ingredientCarbohydrates;
    }

    public int getIngredientProtein() {
        return ingredientProtein;
    }

    public int getIngredientFat() {
        return ingredientFat;
    }

    public int getIngredientCalories() {
        return ingredientCalories;
    }
}
