package com.example.panda.assignment3.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.panda.assignment3.DataBases.Database;
import com.example.panda.assignment3.Services.Backgroundservice;
import com.example.panda.assignment3.Globals.Global;
import com.example.panda.assignment3.Model.UserModel;
import com.example.panda.assignment3.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class CalcActivity extends AppCompatActivity {

    private static final String SAVEINSTANCE_CURRENTUSER_CALC = "save_currentuser_calc";
    private static final String LOG = "CALC_ACTIVITY";

    private static final double CARB_INTAKE_PERCENTAGE = 0.45;
    private static final double FAT_INTAKE_PERCENTAGE = 0.25;
    private static final double PROTEIN_INTAKE_PERCENTAGE = 0.3;
    boolean isServiceStopped;

    private int currentCalories = 0;

    private TextView twCalCalorieGoal, twCalcCarbs, twCalcFat, twCalcProt, twCalcSteps, twCalcStepsKcal;
    private Button btCalcEditUser, btCalcLogOff;
    private UserModel currentUser;
    Bundle intentBundle;
    private FirebaseAuth auth;
    private Database database;

    String countedStep;
    String DetectedStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        // Initialization of buttons
        btCalcEditUser = findViewById(R.id.btCalcEditUser);
        btCalcLogOff = findViewById(R.id.btCalcLogOff);

        // Initialization of testViews
        twCalCalorieGoal = findViewById(R.id.twCalcCalorieGoal);
        twCalcCarbs = findViewById(R.id.twCalcCarbs);
        twCalcFat = findViewById(R.id.twCalcFat);
        twCalcProt = findViewById(R.id.twCalcProt);
        twCalcSteps = findViewById(R.id.twCalcSteps);
        twCalcStepsKcal = findViewById(R.id.twCalcStepsKCal);

        auth = FirebaseAuth.getInstance();
        database = new Database();

        // Get information from intent
         intentBundle = getIntent().getExtras();
         if(intentBundle != null){
             if(intentBundle.isEmpty() == false){
                currentUser = (UserModel) intentBundle.getSerializable(Global.INTENT_CODE_TO_CALCACTIVITY);
             }
         }

        // Load information after rotation from savedInstanceState
        if(savedInstanceState != null){
            currentUser = (UserModel) savedInstanceState.getSerializable(SAVEINSTANCE_CURRENTUSER_CALC);
            Log.d(LOG,"Current user loaded");
        }


        // onClickListener for button to edit user information
        btCalcEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

         // onClickListener for button to log off account and go to home page
        btCalcLogOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LogOffIntent = new Intent(CalcActivity.this, LoginActivity.class);
                if(auth.getCurrentUser()!=null)
                auth.signOut();
                finish();
                startActivity(LogOffIntent);
            }
        });
        UpdateUI();
        updateStepCounterFromSharedPref();
    }
    @Override
    public void onStart(){
        super.onStart();
        startService(new Intent(getBaseContext(), Backgroundservice.class));
        // register our BroadcastReceiver by passing in an IntentFilter. * identifying the message that is broadcasted by using static string "BROADCAST_ACTION".
        registerReceiver(broadcastReceiver, new IntentFilter(Backgroundservice.BROADCAST_BACKGROUND_SERVICE_RESULT));
        isServiceStopped = false;
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
        stopService(new Intent(getBaseContext(), Backgroundservice.class));
        isServiceStopped = true;

    }


    // Used for managing results from intent call StartActivityForResult()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Updates the textview elements in the UI
    private void UpdateUI(){
        currentCalories = CalculateCalories(currentUser);
        if(currentCalories > 0) {
            twCalCalorieGoal.setText(currentCalories + " kcal");
            twCalcCarbs.setText(CalculateCarbs(currentCalories) + " g");
            twCalcFat.setText(CalculateFat(currentCalories) + " g");
            twCalcProt.setText(CalculateProtein(currentCalories) + " g");
            twCalcSteps.setText("");
            twCalcStepsKcal.setText("");
            Log.d(LOG,"UI Updated");
        }
    }

    // Calculating the calories needed
    private int CalculateCalories(UserModel userModel){
        if(userModel != null) {
            if (userModel.getSex().equals("male")) {
                return (int)Math.round((10 * userModel.getWeight() + 6.25 * userModel.getHeight() - 5 * CalculateAge(userModel.getBirthday()) + 5) * userModel.getActivityLevel());
            } else if (userModel.getSex().equals("female")) {
                return (int)Math.round((10 * userModel.getWeight() + 6.25 * userModel.getHeight() - 5 * CalculateAge(userModel.getBirthday()) / 161) * userModel.getActivityLevel());
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    // Calculate the age based on the users birthday
    // All credits goes to https://www.quickprogrammingtips.com/java/how-to-calculate-age-from-date-of-birth-in-java.html for the method.
    private int CalculateAge(String birthDate){
        // Get current date
        Calendar today = Calendar.getInstance();

        // Split string to day, month and year
        String[] dobsplit = birthDate.split("/");


        int curYear = today.get(Calendar.YEAR);
        int dobYear = Integer.parseInt(dobsplit[2]);

        int age = curYear - dobYear;
        // if dob is month or day is behind today's month or day
        // reduce age by 1
        int curMonth = today.get(Calendar.MONTH)+1;
        int dobMonth = Integer.parseInt(dobsplit[1]);
        if (dobMonth > curMonth) { // this year can't be counted!
            age--;
        } else if (dobMonth == curMonth) { // same month? check for day
            int curDay = today.get(Calendar.DAY_OF_MONTH);
            int dobDay = Integer.parseInt(dobsplit[0]);
            if (dobDay > curDay) { // this year can't be counted!
                age--;
            }
        }
        if(age > 0) {
            return age;
        } else{
            return 1;
        }
    }

    // Calculate carb intake
    private int CalculateCarbs(int calories){
        if(calories != 0) {
            return (int)Math.round((calories * CARB_INTAKE_PERCENTAGE) / 4);
        } else{
            return 0;
        }
    }

    // Calculate fat intake
    private int CalculateFat(int calories){
        if(calories != 0) {
            return (int)Math.round((calories * FAT_INTAKE_PERCENTAGE) / 9);
        }else{
            return 0;
        }
    }

    // Calculate protein intake
    private double CalculateProtein(int calories){
        if(calories != 0) {
            return (int)Math.round((calories * PROTEIN_INTAKE_PERCENTAGE) / 4);
        }else{
            return 0;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVEINSTANCE_CURRENTUSER_CALC, currentUser);
        Log.d(LOG,"Current user saved");
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Global.INTENTFROMCALCACTIVITY, currentUser);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            updateStepCounter(intent);
        }

        private void updateStepCounter(Intent intent) {
            countedStep = intent.getStringExtra(Global.COUNTEDSTEPS);
            DetectedStep = intent.getStringExtra(Global.DETECTEDSTEPS);
            database.SavingStepCount(countedStep,DetectedStep,getApplicationContext());
            twCalcSteps.setText(countedStep);
        }


    };
    public void updateStepCounterFromSharedPref()
    {
        try
        {
            twCalcSteps.setText(database.RetrievingStepCount(getApplicationContext()));
        }
        catch (Exception e)
        {
            Log.d(Global.STORINGDATALOCAL,"No steps saved");
        }

    }


}
