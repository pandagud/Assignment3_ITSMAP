package com.example.panda.assignment3.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.panda.assignment3.R;

public class CreateUserActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etRepeatPassword, etBirthday, etHeight, etWeight;
    private RadioButton rbMan, rbWoman;
    private Button btCreateUserCancel, btCreateUserCreate;
    private Spinner activitySponnerCreateUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);

        Spinner activitySpinner = findViewById(R.id.activitySpinnerCreateUSer);

        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(this, R.array.activity_level, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        activitySpinner.setAdapter(activityAdapter);
    }
}
