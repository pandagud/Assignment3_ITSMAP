package com.example.panda.assignment3.DataBases;

import com.example.panda.assignment3.Globals.Global;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class Database {
    FirebaseDatabase database;
    DatabaseReference myRef;
    public Database()
    {

        database= FirebaseDatabase.getInstance();
        //myRef= database.getReference("user");
    }


    public void saveUser(User userdata)
    {
        myRef= database.getReference("user/"+userdata.getID());
        Map<String,String> userData = new HashMap<String,String>();
        userData.put(Global.EMAIL_KEY,userdata.getEmail());
        userData.put(Global.PASSWORD_KEY,userdata.getPassword());

        myRef.setValue(userData);

    }
    public void saveDataForUser(User userdata)
    {
        DatabaseReference childRef = myRef.child("user").child(userdata.getID()).getRef();
        /*Map<String,String> userData = new HashMap<String,String>();
        userData.put(Global.SEX_KEY,userdata.getEmail());
        userData.put(Global.PASSWORD_KEY,userdata.getPassword());
        childRef.child("Information").setValue("");
        */
    }


}
