package com.example.panda.assignment3.Connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.panda.assignment3.Globals.Global;

public class Network {
    public static String getNetworkStatus(Context c)
    {
        ConnectivityManager connect = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =connect.getActiveNetworkInfo();
        if(info!=null && info.isConnected())
        {
            Log.d(Global.CONNECT,"Got connection to the internet" + info.toString());
            return "Got connection to the internet - you can login";
        }
        else{
        Log.d(Global.CONNECT,"You gat no connection to the internet and there for you cant login");
        return "You gat no connection to the internet - you cant login";
    }
    }

}
