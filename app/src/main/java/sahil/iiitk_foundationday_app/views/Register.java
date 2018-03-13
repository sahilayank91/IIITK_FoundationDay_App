package sahil.iiitk_foundationday_app.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Patterns;

import java.util.Random;
import java.util.regex.Pattern;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.mail.GMailSender;
import sahil.iiitk_foundationday_app.model.User;

public class Register extends AppCompatActivity
{
    TextView reg_later;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mAuth = FirebaseAuth.getInstance();

        name = (EditText)findViewById(R.id.name_input);
        college = (EditText)findViewById(R.id.college_input);
        college_id = (EditText)findViewById(R.id.college_id_input);
        department = (EditText)findViewById(R.id.branch_input);
        phone = (EditText)findViewById(R.id.mobile_input);
        email = (EditText)findViewById(R.id.email_input);
        reg_later=(TextView)findViewById(R.id.reg_later);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("name") != null) bundle_name=extras.getString("name");
            if(extras.getString("email") != null){
                bundle_email=extras.getString("email");
                email.setText(bundle_email);
                email.setEnabled(false);
            }
            if(extras.getString("phone") != null){
                bundle_phone=extras.getString("phone");
                phone.setText(bundle_phone);
                phone.setEnabled(false);
            }
            name.setText(bundle_name);
        }
        reg_later.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SharedPreferences userdetails = getSharedPreferences("userInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor=userdetails.edit();
                        if (!bundle_email.isEmpty()){
                            editor.putString("email",bundle_email);
                        }
                        if(!bundle_name.isEmpty()){
                            editor.putString("name",bundle_name);
                        }
                        if (!bundle_phone.isEmpty()){
                            editor.putString("phone",bundle_phone);
                        }
                        editor.putString("status","true");
                        editor.apply();
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        final String[] arraySpinner = new String[] {
                "First", "Second", "Third", "Fourth","Fifth"
        };
        final Spinner s = (Spinner) findViewById(R.id.Year);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = adapterView.getSelectedItemPosition();
                year = arraySpinner[pos];
            }

            public void onNothingSelected(AdapterView<?> adapterView){
                TextView errorText = (TextView)s.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("Please enter the Year");//changes the selected item text to this
            }
        });
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

//this method was coded by tanuj
    public void getFFid(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FFID");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long uid=(long)dataSnapshot.getValue();
                //to generate random IDs so that user's can never guess what will be new FFID
                Random rand=new Random();
                int n=rand.nextInt(3)+1;
                uid=uid+n;
                dataSnapshot.getRef().setValue(uid);
                sendEmail(uid);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebase", "Failed to read value.", databaseError.toException());
            }

        });
    }

//this method was coded by tanuj
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

    //this method was coded by tanuj
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

    //this method was coded by tanuj
    public void regSecondStage(long id){
        user.setUser_id("FF"+id);
        DatabaseReference mRef = database.getReference().child("Users");
        mRef.push().setValue(user);
        Toast.makeText(getApplicationContext(),"Your FFID is : FF"+id,Toast.LENGTH_LONG).show();

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
        editor.putString("FFID", "FF"+id);
        editor.putString("status","true");
        editor.apply();
        Intent intent=new Intent(this,MainActivity.class);
        this.startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Intent i = new Intent(getApplicationContext(),Login_Screen.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

    }
}