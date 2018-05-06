package com.example.panda.assignment3.Activities;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.panda.assignment3.Model.UserModel;
import com.example.panda.assignment3.R;

import java.time.Period;
import java.util.Date;
import java.util.Calendar;

public class CalcActivity extends AppCompatActivity {

    private double CARB_INTAKE_PERCENTAGE = 0.5;
    private double FAT_INTAKE_PERCENTAGE = 0.2;
    private double PROTEIN_INTAKE_PERCENTAGE = 0.3;

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

             }
         }


        // onClickListener for button to edit user information
        btCalcEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent InfoIntent = new Intent(CalcActivity.this,InformationActivity.class);
                // InfoIntent.put()
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
        currentCalories = CalculateKalories(currentUser);
        if(currentCalories > 0) {
            twCalCalorieGoal.setText(currentCalories + " kcal");
            twCalcCarbs.setText(CalculateCarbs(currentCalories) + " g");
            twCalcFat.setText(CalculateFat(currentCalories) + " g");
            twCalcProt.setText(CalculateProtein(currentCalories) + " g");
            twCalcSteps.setText("");
            twCalcStepsKcal.setText("");
        }
    }

    // Calculating the kalories needed
    private double CalculateKalories(UserModel userModel){
        if(userModel.getSex() == "Male"){
            return (10 * userModel.getWeight() + 6.25 * userModel.getHeight() - 5 );//* CalculateAge(userModel.getBirthday()) + 5);
        } else if(userModel.getSex() == "Female"){
            return (10 * userModel.getWeight() + 6.25 * userModel.getHeight() - 5 );//* CalculateAge(userModel.getBirthday()) / 161);
        } else {
            return 0;
        }
    }

    // Calculate the age based on the users birthday
    private int CalculateAge(Date birthDate){
        /*
        Date currentDate = Calendar.getInstance().getTime();
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
        */
        return 23;
    }

    // Calculate carb intake
    private double CalculateCarbs(double calories){
        return (calories * CARB_INTAKE_PERCENTAGE)/4;
    }

    // Calculate fat intake
    private double CalculateFat(double calories){
        return (calories * FAT_INTAKE_PERCENTAGE)/9;
    }

    // Calculate protein intake
    private double CalculateProtein(double calories){
        return (calories * PROTEIN_INTAKE_PERCENTAGE)/4;
    }





}
