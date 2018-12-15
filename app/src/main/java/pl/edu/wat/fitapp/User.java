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

    public User(int userID, String userName, String email, int sex, int age, int height, int activityLevel) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.activityLevel = activityLevel;
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
}
