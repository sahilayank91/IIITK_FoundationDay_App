package sahil.iiitk_foundationday_app;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Patterns;


import java.util.regex.Pattern;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sahil.iiitk_foundationday_app.mail.GMailSender;
import sahil.iiitk_foundationday_app.model.User;

public class Register extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public FirebaseDatabase database;
    public SharedPreferences userdetails;
    public EditText name, college, college_id, department, phone, email;
    public RadioGroup radioGroup, mocGroup;
    public RadioButton radioButton, mocButton;
    public int selectedId, mocID;
    public String regphone = "^[6789]\\d{9}$";
    public String gender = "Female", body = "", bundle_name = "", bundle_email = "", bundle_phone = "", year = "", mos = "Hosteller";
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Registration Form");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();

        name = (EditText)findViewById(R.id.name_input);
        college = (EditText)findViewById(R.id.college_input);
        college_id = (EditText)findViewById(R.id.college_id_input);
        department = (EditText)findViewById(R.id.branch_input);
        phone = (EditText)findViewById(R.id.mobile_input);
        email = (EditText)findViewById(R.id.email_input);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("name") != null) bundle_name=extras.getString("name");
            if(extras.getString("email") != null) bundle_email=extras.getString("email");
            if(extras.getString("phone") != null) bundle_phone=extras.getString("phone");

            name.setText(bundle_name);
            email.setText(bundle_email);
            phone.setText(bundle_phone);
        }

        String[] arraySpinner = new String[] {
                "1", "2", "3", "4"
        };
        Spinner s = (Spinner) findViewById(R.id.Year);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        year = s.getSelectedItem().toString();
    }


    public void onRadioButtonClicked(View view){
        radioGroup = (RadioGroup) findViewById(R.id.rad);
        selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioButton = (RadioButton) findViewById(selectedId);
        gender = radioButton.getText().toString();
    }

    public void onMocButtonClicked(View view){
        mocGroup = (RadioGroup)findViewById(R.id.moc);
        mocID = mocGroup.getCheckedRadioButtonId();

        mocButton = (RadioButton)findViewById(mocID);
        mos = mocButton.getText().toString();
    }

    public void getFFid(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FFID");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long uid=(long)dataSnapshot.getValue();
                uid++;
                dataSnapshot.getRef().setValue(uid);
                Toast.makeText(getApplicationContext(),"Your FFID is : "+uid,Toast.LENGTH_SHORT).show();
                sendEmail(uid);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebase", "Failed to read value.", databaseError.toException());
            }

        });
    }

    //sending emails automatically
    public void sendEmail(long ffid){
        regSecondStage(ffid);
        body="Hi,\n"+name.getText().toString()+" ( "+email.getText().toString()+" )\nYour registration ID for Flair Fiesta 2k18 is FF"+ffid+" . \nYou have successfully registered for Flair Fiesta 2k18, annual cultural-technical fest of" +
                " IIIT Kota.\nWe would be glad to see you on 23rd and 24th March,2018.\n\nRegards\nAdmin";
        final String recepient;
        if (email.getText().toString().isEmpty()){
            Toast.makeText(this,"Email is not provided!",Toast.LENGTH_SHORT).show();
            return;
        }else{
            recepient=email.getText().toString();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("tanujm242@gmail.com", "geniusmehta");
                    sender.sendMail("Your Registration for FlairFiesta 2k18 is Confirmed.",
                            body,
                            "tanujm242@gmail.com",
                            recepient);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        }).start();

    }

    public boolean validateInputs(){
        int flag = 0;
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError("Enter correct Email Address");
            flag = 1;
        }
        if((!phone.getText().toString().matches(regphone)) || (phone.getText().toString().length() == 0)){
            phone.setError("Enter correct 10 digit Mobile Number");
            flag = 1;
        }

        if( name.getText().toString().length() == 0 ){
            name.setError( "Name is required!" );
            flag = 1;
        }

        if( college.getText().toString().length() == 0 ) {
            college.setError("College name is required!");
            flag = 1;
        }

        if( college_id.getText().toString().length() == 0 ) {
            college_id.setError("College ID is required!");
            flag = 1;
        }

        if( department.getText().toString().length() == 0 ) {
            department.setError("Department is required!");
            flag = 1;
        }

        if(selectedId < 0){
            radioButton.setError("Gender is required!");
            flag = 1;
        }

        if(year.length() == 0){
            Toast.makeText(this, "Enter correct year", Toast.LENGTH_SHORT).show();
            flag = 1;
        }

        if(mocID < 0){
            mocButton.setError("Mode of Stay is required!");
            flag = 1;
        }

        if(flag == 0)
            return true;
        else
            return false;
    }

    public void sendMessage(View view) {
        database = FirebaseDatabase.getInstance();
        boolean value = validateInputs();
        if(value){
            user.setName(name.getText().toString());
            user.setDepartment(department.getText().toString());
            user.setPhone(phone.getText().toString());
            user.setEmail(email.getText().toString());
            user.setCollege(college.getText().toString());
            user.setCollegeid(college_id.getText().toString());
            user.setGender(gender);
            user.setYear(year);
            user.setMos(mos);
            getFFid();
        }
        else{
            Toast.makeText(this, "Enter correct details", Toast.LENGTH_SHORT).show();
        }



    }
    public void regSecondStage(long id){
        user.setUser_id(id);
        DatabaseReference mRef = database.getReference().child("Users");
        mRef.push().setValue(user);
        Toast.makeText(this, "Registration Successfull", Toast.LENGTH_SHORT).show();

        userdetails = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=userdetails.edit();
        editor.putString("name",name.getText().toString());
        editor.putString("department",department.getText().toString());
        editor.putString("phone",phone.getText().toString());
        editor.putString("email",email.getText().toString());
        editor.putString("college",college.getText().toString());
        editor.putString("collegeid",college_id.getText().toString());
        editor.putString("gender", gender);
        editor.putString("Year", year);
        editor.putString("MOS", mos);
        editor.putString("FFID", ""+id);
        editor.apply();
    }
}
