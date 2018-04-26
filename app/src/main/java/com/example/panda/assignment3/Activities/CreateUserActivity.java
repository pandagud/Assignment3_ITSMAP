package com.example.panda.assignment3.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.panda.assignment3.R;

public class CreateUserActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etRepeatPassword, etBirthday, etHeight, etWeight;
    private RadioButton rbMan, rbWoman;
    private Button btCreateUserCancel, btCreateUserCreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);

    }
}
