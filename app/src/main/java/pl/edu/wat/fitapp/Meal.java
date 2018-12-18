package pl.edu.wat.fitapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Meal implements Serializable, FoodSystem {
    private int ID;
    private String name;
    private ArrayList<Ingredient> ingredientList;
    private ArrayList<Integer> ingredientWeightList;

    public Meal(int mealID, String mealName) {
        this.ID = mealID;
        this.name = mealName;
        ingredientList = new ArrayList<>();
        ingredientWeightList = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public double getCarbohydrates(){
        double sum = 0;
        for(int i =0; i < ingredientList.size(); i++)
            sum += ingredientList.get(i).getCarbohydrates() * ingredientWeightList.get(i)/100;
        return sum;
    }

    public double getProtein(){
        double sum = 0;
        for(int i =0; i < ingredientList.size(); i++)
            sum += ingredientList.get(i).getProtein() * ingredientWeightList.get(i)/100;
        return sum;
    }

    public double getFat(){
        double sum = 0;
        for(int i =0; i < ingredientList.size(); i++)
            sum += ingredientList.get(i).getFat() * ingredientWeightList.get(i)/100;
        return sum;
    }

    public int getCalories(){
        int sum = 0;
        for(int i =0; i < ingredientList.size(); i++)
            sum += ingredientList.get(i).getCalories() * ingredientWeightList.get(i)/100;
        return sum;
    }

    public void addIngriedientToList(Ingredient ingredient, int weight){
        ingredientList.add(ingredient);
        ingredientWeightList.add(weight);
    }
}
