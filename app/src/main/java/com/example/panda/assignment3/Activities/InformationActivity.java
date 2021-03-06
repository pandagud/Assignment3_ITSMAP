package com.example.panda.assignment3.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.panda.assignment3.DataBases.Database;
import com.example.panda.assignment3.Globals.ActivityParser;
import com.example.panda.assignment3.Globals.Global;
import com.example.panda.assignment3.Model.UserModel;
import com.example.panda.assignment3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class InformationActivity extends AppCompatActivity {

    private String LOG = "INFORMATIONACTIVITY";
    private EditText etInfoBirthday, etInfoHeight, etInfoWeight;
    private RadioButton rbInfoMan, rbInfoWoman;
    private Button btInfoCancel, btInfoCreate;
    private Spinner activitySponnerInfo;
    private FirebaseAuth auth;
    private UserModel currentUser;
    private Database database;
    private DatePickerDialog.OnDateSetListener mDatasetListener;
    private ActivityParser activityParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize objects
        setContentView(R.layout.activity_information);
        activityParser  = new ActivityParser();
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

        // initialize objects
        etInfoHeight=findViewById(R.id.etInfoHeight);
        etInfoWeight=findViewById(R.id.etWeight);
        rbInfoMan = findViewById(R.id.rbInfoMan);
        rbInfoWoman=findViewById(R.id.rbInfoWoman);


        auth = FirebaseAuth.getInstance();
        database = new Database();
        if(auth.getCurrentUser()!=null)
        {
            retriveUserModel(auth.getCurrentUser().getUid());
        }

        btInfoCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btInfoCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initialize objects
                final String height = etInfoHeight.getText().toString().trim();
                final String weight = etInfoWeight.getText().toString().trim();
                String birthday= etInfoBirthday.getText().toString().trim();
                String sex = "";

                // Checking Values
                if (TextUtils.isEmpty(height)) {
                    Toast.makeText(getApplicationContext(),R.string.EnterHeight , Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(weight)) {
                    Toast.makeText(getApplicationContext(), R.string.EnterWeight, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(birthday)) {
                    Toast.makeText(getApplicationContext(), R.string.EnterBirthday, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!rbInfoMan.isChecked()&&!rbInfoWoman.isChecked())
                {
                    Toast.makeText(getApplicationContext(), R.string.EnterGender, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(rbInfoMan.isChecked())
                {
                    sex=Global.MALE;
                }
                else if(rbInfoWoman.isChecked())
                {
                    sex=Global.FEMALE;
                }
                if(currentUser==null) {
                    // Creating userobj. and storing if user is logged in.
                    currentUser = new UserModel(sex, birthday.toString(), Double.parseDouble(height), Double.parseDouble(weight), activityParser.getActivityDouble(getBaseContext(),activitySponnerInfo.getSelectedItem().toString()));
                    if(auth.getCurrentUser()!=null)
                    database.saveDataForUser(currentUser, auth.getCurrentUser().getUid());
                }
                else{

                    currentUser.setSex(sex);
                    currentUser.setBirthday(birthday.toString());
                    currentUser.setHeight(Double.parseDouble(height));
                    currentUser.setWeight(Double.parseDouble(weight));
                    currentUser.setActivityLevel(activityParser.getActivityDouble(getBaseContext(),activitySponnerInfo.getSelectedItem().toString()));
                    if(auth.getCurrentUser()!=null)
                        database.saveDataForUser(currentUser, auth.getCurrentUser().getUid());

                }

                Intent i = new Intent(InformationActivity.this,CalcActivity.class);
                i.putExtra(Global.INTENT_CODE_TO_CALCACTIVITY,currentUser);
                startActivityForResult(i,Global.REQUESTCODEFROMINFORMATIONTOCALC);
            }
        });
    }
    @Override
    public void onBackPressed() {
        auth.signOut();
        finish();

    }
    public void updateUI(UserModel data)
    {
        etInfoBirthday.setText(data.getBirthday());
        etInfoHeight.setText(String.valueOf(data.getHeight()));
        etInfoWeight.setText(String.valueOf(data.getWeight()));
        setSpinText(activitySponnerInfo,String.valueOf(activityParser.getActivityString(this,data.getActivityLevel())));
        setSex(data.getSex());
        Log.d(LOG,"UI updated");
    }

    //https://stackoverflow.com/questions/13151699/set-text-on-spinner
    public void setSpinText(Spinner spin, String text)
    {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            String test = spin.getAdapter().getItem(i).toString();
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);
            }
        }

    }
    public void setSex(String text)
    {
        if(text==Global.MALE)
            rbInfoMan.setChecked(true);
        else if(text == Global.FEMALE)
        {
            rbInfoWoman.setChecked(true);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(Global.SAVEDINSTANCEUSERMODELOBJECT, currentUser);
        try
        {
            UserModel savedUM = new UserModel();
            savedUM.setBirthday(etInfoBirthday.getText().toString());
            savedUM.setHeight(Double.parseDouble(etInfoHeight.getText().toString()));
            savedUM.setWeight(Double.parseDouble(etInfoWeight.getText().toString()));
            if(rbInfoMan.isChecked() == true) {
                savedUM.setSex(Global.MALE);
            } else if(rbInfoWoman.isChecked() == true){
                savedUM.setSex(Global.FEMALE);
            }
            savedUM.setActivityLevel(activityParser.getActivityDouble(getBaseContext(),activitySponnerInfo.getSelectedItem().toString()));
            savedInstanceState.putSerializable(Global.SAVEDINSTANCE_INFOACTIVITY_USERMODEL,savedUM);
            if(currentUser!=null)
            database.saveDataForUser(currentUser, auth.getCurrentUser().getUid());
            Log.d(LOG,"Saved the instance");
        }
        catch (Exception e)
        {
            Log.d(LOG,"unable to setHeight and Weight"+ e.toString());
        }

    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        UserModel userModel = (UserModel) savedInstanceState.getSerializable(Global.SAVEDINSTANCE_INFOACTIVITY_USERMODEL);
        if(userModel!=null)
        {
            updateUI(userModel);
            database.setRetrivedata(userModel);
        }

        Log.d(LOG,"Restored the instance");
    }
    public void retriveUserModel(String ID)
    {
        final String _Id =ID;
        final FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference childRef = database.getReference(Global.USER_KEY+"/"+ID+"/"+Global.INFORMATION_KEY);
        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserModel value = dataSnapshot.getValue(UserModel.class);
                currentUser=value;
                try
                {
                    if(value.getSex()!=null)
                        updateUI(value);
                }
                catch (Exception e)
                {
                    Log.d(Global.DATABASELOG,"nothing stored in database");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Global.REQUESTCODEFROMINFORMATIONTOCALC) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle b = data.getExtras();
                if (b != null) {
                    currentUser = (UserModel) b.getSerializable(Global.INTENTFROMCALCACTIVITY);
                    updateUI(currentUser);
                }
            } else if (resultCode == 0) {
                Toast.makeText(getApplicationContext(), R.string.ProblemCalcData, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
