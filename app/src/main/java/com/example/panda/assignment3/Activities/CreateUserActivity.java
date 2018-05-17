package com.example.panda.assignment3.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.panda.assignment3.DataBases.Database;
import com.example.panda.assignment3.Model.User;
import com.example.panda.assignment3.Globals.ActivityParser;
import com.example.panda.assignment3.Model.UserModel;
import com.example.panda.assignment3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateUserActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etRepeatPassword, etBirthday, etHeight, etWeight;
    private RadioButton rbMan, rbWoman;
    private Button btCreateUserCancel, btCreateUserCreate;
    private Spinner activitySponnerCreateUser;
    private FirebaseAuth auth;
    private Database database;
    private User currentUser;
    private UserModel currentUserModel;
    private ActivityParser activityParser;
    Spinner activitySpinner;

    private DatePickerDialog.OnDateSetListener mDatasetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);
        btCreateUserCreate = findViewById(R.id.btCreateUserCreate);
        btCreateUserCancel = findViewById(R.id.btCreateUserCancel);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);

        etHeight=findViewById(R.id.etHeight);
        etWeight=findViewById(R.id.etWeight);
        rbMan = findViewById(R.id.rbMan);
        rbWoman=findViewById(R.id.rbWoman);
        etBirthday = findViewById(R.id.etBirthday);

        activityParser= new ActivityParser();

        auth = FirebaseAuth.getInstance();
        activitySpinner = findViewById(R.id.activitySpinnerCreateUSer);
        database = new Database();
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.activity_level, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        activitySpinner.setAdapter(activityAdapter);

        etBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calender = Calendar.getInstance();
                int year  = calender.get(Calendar.YEAR);
                int month = calender.get(Calendar.MONTH);
                int day = calender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateUserActivity.this,R.style.Theme_AppCompat_DialogWhenLarge,mDatasetListener,year,month,day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDatasetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int monthwithindex = new Integer(month+1);
                String data = dayOfMonth+"/"+monthwithindex+"/"+year;
                etBirthday.setText(data);
            }
        };





        btCreateUserCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = etUsername.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();
                String repeatPassword= etRepeatPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(repeatPassword))
                {
                    Toast.makeText(getApplicationContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
                    return;
                }


                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(CreateUserActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(CreateUserActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(CreateUserActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(CreateUserActivity.this, LoginActivity.class));
                                    currentUser=setUserData(currentUser,password,email,auth.getCurrentUser().getUid());
                                    database.writeNewUser(currentUser);
                                    CheckIfUserHasData();
                                    finish();
                                }
                            }
                        });

            }
        });

        btCreateUserCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });
    }

    public User setUserData(User userData,String password,String Email,String ID)
    {
        if(userData==null)
            userData = new User();
        userData.setPassword(password);
        userData.setEmail(Email);
        userData.setID(ID);
        return userData;
    }
    public void CheckIfUserHasData()
    {
        final String height = etHeight.getText().toString().trim();
        final String weight = etWeight.getText().toString().trim();
        String birthday= etBirthday.getText().toString().trim();
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
        if(!rbMan.isChecked()&&!rbWoman.isChecked())
        {
            Toast.makeText(getApplicationContext(), "Pick a gender!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(rbMan.isChecked())
        {
            sex="male";
        }
        else if(rbWoman.isChecked())
        {
            sex="female";
        }

        currentUserModel = new UserModel(sex, birthday.toString(), Double.parseDouble(height), Double.parseDouble(weight), activityParser.getActivityDouble(getBaseContext(),activitySpinner.getSelectedItem().toString()));
        database.saveDataForUser(currentUserModel, auth.getCurrentUser().getUid());
    }
}
