package com.example.panda.assignment3.Globals;

import android.content.Context;
import com.example.panda.assignment3.R;

public class ActivityParser {

    public double getActivityDouble(Context c, String actStr){
        String actlevels[] = c.getResources().getStringArray(R.array.activity_level);
        if(actStr.equals(actlevels[0])){
            return 1.0;
        }
        else if(actStr.equals(actlevels[1])){
            return 1.375;
        }
        else if(actStr.equals(actlevels[2])){
            return 1.55;
        }
        else if(actStr.equals(actlevels[3])){
            return 1.725;
        }
        else if(actStr.equals(actlevels[4])){
            return 1.9;
        } else{
            return 1;
        }
    }

    public String getActivityString(Context c, double actdou){
        String actlevels[] = c.getResources().getStringArray(R.array.activity_level);
        if(actdou == 1.0){
            return actlevels[0].toString();
        }
        else if(actdou == 1.375){
            return actlevels[1].toString();
        }
        else if(actdou == 1.55){
            return actlevels[2].toString();
        }
        else if(actdou == 1.725){
            return actlevels[3].toString();
        }
        else if(actdou == 1.9){
            return actlevels[4].toString();
        } else{
            return actlevels[0].toString();
        }
    }
}
