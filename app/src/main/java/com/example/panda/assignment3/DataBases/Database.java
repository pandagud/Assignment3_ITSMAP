package com.example.panda.assignment3.DataBases;

import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.example.panda.assignment3.Globals.Global;
import com.example.panda.assignment3.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Database {
    FirebaseDatabase database;
    DatabaseReference databaseref;
    private FirebaseAuth dauth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private  UserModel retrivedata;


    public Database()
    {

        database= FirebaseDatabase.getInstance();
        dauth = FirebaseAuth.getInstance();
        databaseref = database.getReference();

    }


    public void writeNewUser(User userdata)
    {
       databaseref = database.getReference(Global.USER_KEY+"/"+userdata.getID());
        databaseref.setValue(hashMapUser(userdata));

    }
    public Map<String,String > hashMapUser(User userdata)
    {
        Map<String,String> userData = new HashMap<String,String>();
        userData.put(Global.EMAIL_KEY,userdata.getEmail());
        userData.put(Global.PASSWORD_KEY,userdata.getPassword());
        return userData;
    }
    public void saveDataForUser(UserModel usermodeldata,String ID)
    {

        DatabaseReference childRef = database.getReference(Global.USER_KEY+"/"+ID+"/"+Global.INFORMATION_KEY);

        childRef.setValue(usermodeldata);
    }
    public UserModel getRetrivedata()
    {
        return retrivedata;
    }
    public void setRetrivedata(UserModel data)
    {
        if(retrivedata==null)
        {
            retrivedata = new UserModel();
            retrivedata=data;
        }
        else
        {
            retrivedata=data;
        }
    }


}
