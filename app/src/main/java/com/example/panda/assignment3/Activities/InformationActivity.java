package com.example.panda.assignment3.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.panda.assignment3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InformationActivity extends AppCompatActivity {

    private EditText etInfoBirthday, etInfoHeight, etInfoWeight;
    private RadioButton rbInfoMan, rbInfoWoman;
    private Button btInfoCancel, btInfoCreate;
    private Spinner activitySponnerInfo;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Spinner activitySpinner = findViewById(R.id.activitySpinnerInfo);

        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(this, R.array.activity_level, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        activitySpinner.setAdapter(activityAdapter);
        btInfoCancel = findViewById(R.id.btInfoCancel);
        auth = FirebaseAuth.getInstance();
        btInfoCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(InformationActivity.this, LoginActivity.class));

            }
        });
    }
}
