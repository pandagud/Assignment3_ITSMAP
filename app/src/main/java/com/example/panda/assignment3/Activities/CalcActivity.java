package com.example.panda.assignment3.Activities;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.panda.assignment3.Globals.Global;
import com.example.panda.assignment3.Model.UserModel;
import com.example.panda.assignment3.R;

import java.time.Period;
import java.util.Date;
import java.util.Calendar;

public class CalcActivity extends AppCompatActivity {

    private static final String SAVEINSTANCE_CURRENTUSER_CALC = "save_currentuser_calc";
    private static final String LOG = "CALC_ACTIVITY";

    private static final double CARB_INTAKE_PERCENTAGE = 0.5;
    private static final double FAT_INTAKE_PERCENTAGE = 0.2;
    private static final double PROTEIN_INTAKE_PERCENTAGE = 0.3;

    private double currentCalories = 0;

    private TextView twCalCalorieGoal, twCalcCarbs, twCalcFat, twCalcProt, twCalcSteps, twCalcStepsKcal;
    private Button btCalcEditUser, btCalcLogOff;
    private UserModel currentUser;
    Bundle intentBundle;

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
                Intent InfoIntent = new Intent(CalcActivity.this,InformationActivity.class);
                InfoIntent.putExtra(Global.INTENT_CODE_TO_INFORMATIONACTIVITY,currentUser);
                finish();
                startActivity(InfoIntent);

            }
        });

         // onClickListener for button to log off account and go to home page
        btCalcLogOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LogOffIntent = new Intent(CalcActivity.this, LoginActivity.class);
                finish();
                startActivity(LogOffIntent);
            }
        });
        UpdateUI();
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
    private double CalculateCalories(UserModel userModel){
        if(userModel != null) {
            if (userModel.getSex() == "Male") {
                return (10 * userModel.getWeight() + 6.25 * userModel.getHeight() - 5 * CalculateAge(userModel.getBirthday()) + 5);
            } else if (userModel.getSex() == "Female") {
                return (10 * userModel.getWeight() + 6.25 * userModel.getHeight() - 5 * CalculateAge(userModel.getBirthday()) / 161);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    // Calculate the age based on the users birthday
    private int CalculateAge(Date birthDate){
        Date currentDate = Calendar.getInstance().getTime();
        return currentDate.getYear() - birthDate.getYear();
    }

    // Calculate carb intake
    private double CalculateCarbs(double calories){
        if(calories != 0) {
            return (calories * CARB_INTAKE_PERCENTAGE) / 4;
        } else{
            return 0;
        }
    }

    // Calculate fat intake
    private double CalculateFat(double calories){
        if(calories != 0) {
            return (calories * FAT_INTAKE_PERCENTAGE) / 9;
        }else{
            return 0;
        }
    }

    // Calculate protein intake
    private double CalculateProtein(double calories){
        if(calories != 0) {
            return (calories * PROTEIN_INTAKE_PERCENTAGE) / 4;
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
}
