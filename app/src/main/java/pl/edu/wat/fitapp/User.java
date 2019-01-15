package pl.edu.wat.fitapp;

import java.io.Serializable;

public class User implements Serializable {
    private int userID;
    private String userName;
    private String email;
    private int sex;
    private int age;
    private int height;
    private int activityLevel;
    private double weight;
    private int caloricDemand;
    private int goal;



    public User(int userID, String userName, String email, int sex, int age, int height, int activityLevel, double weight, int caloricDemand, int goal) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.activityLevel = activityLevel;
        this.weight = weight;
        this.caloricDemand = caloricDemand;
        this.goal = goal;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public int getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public int getHeight() {
        return height;
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    public double getWeight() {
        return weight;
    }

    public int getCaloricDemand() {
        return caloricDemand;
    }

    public int getGoal() {
        return goal;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setCaloricDemand(int caloricDemand) {
        this.caloricDemand = caloricDemand;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }
}
