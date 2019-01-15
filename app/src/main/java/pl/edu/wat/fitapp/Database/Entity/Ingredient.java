package pl.edu.wat.fitapp.Database.Entity;

import java.io.Serializable;

import pl.edu.wat.fitapp.Interface.FoodSystem;

public class Ingredient implements Serializable, FoodSystem, Cloneable {
    private int ID;
    private String name;
    private double carbohydrates;
    private double protein;
    private double fat;
    private int calories;
    private int weight;

    public Ingredient(int ingredientId, String ingredientName, double ingredientCarbohydrates, double ingredientProtein, double ingredientFat, int ingredientCalories) {
        this.ID = ingredientId;
        this.name = ingredientName;
        this.carbohydrates = ingredientCarbohydrates;
        this.protein = ingredientProtein;
        this.fat = ingredientFat;
        this.calories = ingredientCalories;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
