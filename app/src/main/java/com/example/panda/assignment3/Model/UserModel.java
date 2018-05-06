package com.example.panda.assignment3.Model;

import java.util.ArrayList;
import java.util.Date;

public class UserModel {

    private String sex;
    private String birthday;
    private double height;
    private double weight;
    private double activityLevel;
    private ArrayList<Double> stepsList;

    public UserModel(String sex, String birthday, double height, double weight, double activityLevel, ArrayList<Double> stepsList) {
        this.sex = sex;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.activityLevel = activityLevel;
        this.stepsList = stepsList;
    }
    public UserModel()
    {
    }

    public ArrayList<Double> getStepsList() {
        return stepsList;
    }

    public void setStepsList(ArrayList<Double> stepsList) {
        this.stepsList = stepsList;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        if(sex == "Male" || sex == "Female") {
            this.sex = sex;
        } else{
            this.sex = "Male";
        }
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        if(height > 0) {
            this.height = height;
        } else{
            this.height = 0;
        }
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if(weight > 0) {
            this.weight = weight;
        } else {
            this.weight = 0;
        }
    }

    public double getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(double activityLevel) {
        this.activityLevel = activityLevel;
    }

}
