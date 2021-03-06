package com.example.panda.assignment3.Model;

import com.example.panda.assignment3.Globals.Global;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class UserModel implements Serializable {
    // Usermodel to store Information about user.
    private String sex;
    private String birthday;
    private double height;
    private double weight;
    private double activityLevel;

    public UserModel(String sex, String birthday, double height, double weight, double activityLevel) {
        this.sex = sex;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.activityLevel = activityLevel;

    }

    public UserModel()
    {

    }



    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        if(sex == Global.MALE || sex == Global.FEMALE) {
            this.sex = sex;
        } else{
            this.sex = Global.MALE;
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
