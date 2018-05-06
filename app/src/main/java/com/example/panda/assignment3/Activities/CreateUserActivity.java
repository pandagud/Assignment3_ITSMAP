package com.example.panda.assignment3.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.panda.assignment3.DataBases.Database;
import com.example.panda.assignment3.DataBases.User;
import com.example.panda.assignment3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateUserActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etRepeatPassword, etBirthday, etHeight, etWeight;
    private RadioButton rbMan, rbWoman;
    private Button btCreateUserCancel, btCreateUserCreate;
    private Spinner activitySponnerCreateUser;
    private FirebaseAuth auth;
    private Database database;
    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);
        btCreateUserCreate = findViewById(R.id.btCreateUserCreate);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);
        auth = FirebaseAuth.getInstance();
        Spinner activitySpinner = findViewById(R.id.activitySpinnerCreateUSer);
        database = new Database();
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.activity_level, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        activitySpinner.setAdapter(activityAdapter);
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
                                    finish();
                                }
                            }
                        });

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
}
