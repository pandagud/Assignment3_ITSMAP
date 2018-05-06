package com.example.panda.assignment3.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.panda.assignment3.DataBases.Database;
import com.example.panda.assignment3.Model.UserModel;
import com.example.panda.assignment3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InformationActivity extends AppCompatActivity {

    private EditText etInfoBirthday, etInfoHeight, etInfoWeight;
    private RadioButton rbInfoMan, rbInfoWoman;
    private Button btInfoCancel, btInfoCreate,btnTest;
    private Spinner activitySponnerInfo;
    private FirebaseAuth auth;
    private UserModel currentUser;
    private Database database;
    private DatePickerDialog.OnDateSetListener mDatasetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        activitySponnerInfo = findViewById(R.id.activitySpinnerInfo);

        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(this, R.array.activity_level, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        activitySponnerInfo.setAdapter(activityAdapter);
        btInfoCancel = findViewById(R.id.btInfoCancel);
        btInfoCreate = findViewById(R.id.btInfoContinue);

        etInfoBirthday = findViewById(R.id.etInfoBirthday);

        etInfoBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calender = Calendar.getInstance();
                int year  = calender.get(Calendar.YEAR);
                int month = calender.get(Calendar.MONTH);
                int day = calender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(InformationActivity.this,R.style.Theme_AppCompat_DialogWhenLarge,mDatasetListener,year,month,day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDatasetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int monthwithindex = new Integer(month+1);
                String data = dayOfMonth+"/"+monthwithindex+"/"+year;
                etInfoBirthday.setText(data);
            }
        };


        etInfoHeight=findViewById(R.id.etInfoHeight);
        etInfoWeight=findViewById(R.id.etInfoWeight);
        rbInfoMan = findViewById(R.id.rbInfoMan);
        rbInfoWoman=findViewById(R.id.rbInfoWoman);




        auth = FirebaseAuth.getInstance();
        database = new Database();
        btInfoCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(InformationActivity.this, LoginActivity.class));

            }
        });
        btInfoCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String height = etInfoHeight.getText().toString().trim();
                final String weight = etInfoWeight.getText().toString().trim();
                String birthday= etInfoBirthday.getText().toString().trim();
                String sex = "";


                if (TextUtils.isEmpty(height)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(weight)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(birthday)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!rbInfoMan.isChecked()&&!rbInfoWoman.isChecked())
                {
                    Toast.makeText(getApplicationContext(), "Pick a gender!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(rbInfoMan.isChecked())
                {
                    sex="male";
                }
                else if(rbInfoWoman.isChecked())
                {
                    sex="female";
                }
                if(currentUser==null)
                {
                    // Test data
                    ArrayList<Double> stepsList = new ArrayList<Double>();




                        currentUser = new UserModel(sex,birthday.toString(),1.89,89,85,stepsList);
                        database.saveDataForUser(currentUser,auth.getCurrentUser().getUid());



                }


            }
        });

        btnTest = findViewById(R.id.btntest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String test = auth.getCurrentUser().getUid();
                database.retriveUserModel(auth.getCurrentUser().getUid());
            }
        });

    }

}
