package com.example.panda.assignment3.Model;

public class User {
    private String email;
    private String password;
    private String ID;


    public User(){}

    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public String getID(){return ID;}
    public void setEmail(String data){this.email=data;}
    public void setPassword(String data){this.password=data;}
    public void setID(String data){this.ID=data;}
}
