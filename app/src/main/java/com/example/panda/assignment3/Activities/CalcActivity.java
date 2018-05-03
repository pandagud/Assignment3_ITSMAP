package com.example.panda.assignment3.Activities;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.panda.assignment3.R;

public class CalcActivity extends AppCompatActivity {

    private TextView twCalCalorieGoal, twCalcCarbs, twCalcFat, twCalcProt, twCalcSteps, twCalcStepsKcal;
    private Button btCalcEditUser, btCalcLogOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);


    }
}
