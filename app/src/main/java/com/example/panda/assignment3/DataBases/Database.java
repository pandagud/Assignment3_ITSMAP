package com.example.panda.assignment3.DataBases;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.example.panda.assignment3.Activities.CalcActivity;
import com.example.panda.assignment3.Globals.Global;
import com.example.panda.assignment3.Model.User;
import com.example.panda.assignment3.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Database  {
    FirebaseDatabase database;
    DatabaseReference databaseref;
    private FirebaseAuth dauth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private  UserModel retrivedata;
    private SharedPreferences sharedPreferences;


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
    //https://stackoverflow.com/questions/6537535/check-date-with-todays-date
    public static boolean isToday(Date date){
        Calendar today = Calendar.getInstance();
        Calendar specifiedDate  = Calendar.getInstance();
        specifiedDate.setTime(date);

        return today.get(Calendar.DAY_OF_MONTH) == specifiedDate.get(Calendar.DAY_OF_MONTH)
                &&  today.get(Calendar.MONTH) == specifiedDate.get(Calendar.MONTH)
                &&  today.get(Calendar.YEAR) == specifiedDate.get(Calendar.YEAR);
    }

    public void SavingStepCount(String countedSteps, String detectedSteps,Context context)
    {
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        Log.d(Global.STORINGDATALOCAL,"Storing steps in shared pref.");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Boolean test = isToday(retriveDate(sharedPreferences));
        if(!isToday(retriveDate(sharedPreferences)))
        {
            Calendar today = Calendar.getInstance();
            savingDate(today,editor);
            editor.putString(Global.STORINGSTEPSLOCAL,countedSteps);
            editor.putString(Global.STORINGDETECTEDSTEPSLOCAL,detectedSteps);
            Global.RESETSTEPS=true;
        }

        editor.commit();
    }
    public String RetrievingStepCount(Context context)
    {
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        try{
            String countedSteps = sharedPreferences.getString(Global.STORINGSTEPSLOCAL,"");
            Date oldDate = retriveDate(sharedPreferences);
            return countedSteps;

        }
        catch (Exception e)
        {
            Log.d(Global.STORINGDATALOCAL,"No data to retrive");
            return null;

        }
    }
    public void savingDate(Calendar date,SharedPreferences.Editor editor)
    {
        int day = date.get(Calendar.DATE);
        int month = date.get(Calendar.MONTH)+1;
        int year = date.get(Calendar.YEAR);
        editor.putInt(Global.STORINGDAYLOCAL,day);
        editor.putInt(Global.STORINGMONTHLOCAL,month);
        editor.putInt(Global.STORINGYEARLOCAL,year);
    }
    public Date retriveDate(SharedPreferences sharedPreferences)
    {
        int oldDay = sharedPreferences.getInt(Global.STORINGDAYLOCAL,0);
        int oldMonth = sharedPreferences.getInt(Global.STORINGMONTHLOCAL,0)-1;
        int oldYear = sharedPreferences.getInt(Global.STORINGYEARLOCAL,0);
        Calendar oldCalenderDate = Calendar.getInstance();
        oldCalenderDate.set(oldYear,oldMonth,oldDay);
        Date oldDate = oldCalenderDate.getTime();

        return oldDate;


    }

}
