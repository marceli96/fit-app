package pl.edu.wat.fitapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Meal implements Serializable, FoodSystem {
    private int ID;
    private String name;
    private int weight;
    private ArrayList<Ingredient> ingredientList;

    public Meal(int mealID, String mealName) {
        this.ID = mealID;
        this.name = mealName;
        ingredientList = new ArrayList<>();
    }

    public int getTotalWeight(){
        int weight = 0;
        for(int i = 0; i < ingredientList.size(); i++)
            weight += ingredientList.get(i).getWeight();
        return weight;
    }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
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
        double sum = 0;
        for (int i = 0; i < ingredientList.size(); i++)
            sum += ingredientList.get(i).getCarbohydrates() * ingredientList.get(i).getWeight() / 100;
        return sum;
    }

    public double getProtein() {
        double sum = 0;
        for (int i = 0; i < ingredientList.size(); i++)
            sum += ingredientList.get(i).getProtein() * ingredientList.get(i).getWeight() / 100;
        return sum;
    }

    public double getFat() {
        double sum = 0;
        for (int i = 0; i < ingredientList.size(); i++)
            sum += ingredientList.get(i).getFat() * ingredientList.get(i).getWeight() / 100;
        return sum;
    }

    public int getCalories() {
        int sum = 0;
        for (int i = 0; i < ingredientList.size(); i++)
            sum += ingredientList.get(i).getCalories() * ingredientList.get(i).getWeight() / 100;
        return sum;
    }

    public void addIngredientToList(Ingredient ingredient) {
        ingredientList.add(ingredient);
    }
}
