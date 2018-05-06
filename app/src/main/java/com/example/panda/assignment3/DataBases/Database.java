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


    public Database()
    {

        database= FirebaseDatabase.getInstance();
        dauth = FirebaseAuth.getInstance();
        databaseref = database.getReference();

    }


    public void writeNewUser(User userdata)
    {
       databaseref = database.getReference(Global.USER_KEY+"/"+userdata.getID());
       /*
        Map<String,String> userData = new HashMap<String,String>();
         userData.put(Global.EMAIL_KEY,userdata.getEmail());
        userData.put(Global.PASSWORD_KEY,userdata.getPassword());
         */
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
    public Map<String,String> hashMapUserModel(UserModel userModel)
    {
        Map<String,String> userData = new HashMap<String,String>();
        userData.put(Global.SEX_KEY,userModel.getSex());
        userData.put(Global.HEIGHT_KEY,String.valueOf(userModel.getHeight()));
        userData.put(Global.WEIGHT_KEY,String.valueOf(userModel.getWeight()));
        userData.put(Global.BIRTHDAY_KEY,userModel.getBirthday().toString());
        userData.put(Global.ACTIVITY_KEY,String.valueOf(userModel.getActivityLevel()));
        return userData;
    }
    public void retriveUserModel(String ID)
    {
        final String _Id =ID;
        DatabaseReference childRef = database.getReference(Global.USER_KEY+"/"+ID+"/"+Global.INFORMATION_KEY);
        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserModel value = dataSnapshot.getValue(UserModel.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
