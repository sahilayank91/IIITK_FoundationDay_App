package sahil.iiitk_foundationday_app;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sahil.iiitk_foundationday_app.model.User;

public class Register extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public FirebaseDatabase database;
    //public FirebaseUser fireuser;
    public SharedPreferences userdetails;
    public EditText name, college, college_id, department, phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        name = (EditText)findViewById(R.id.name_input);
        college = (EditText)findViewById(R.id.college_input);
        college_id = (EditText)findViewById(R.id.college_id_input);
        department = (EditText)findViewById(R.id.branch_input);
        phone = (EditText)findViewById(R.id.mobile_input);
        email = (EditText)findViewById(R.id.email_input);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name.setText(extras.getString("name"));
            email.setText(extras.getString("email"));
            phone.setText(""+extras.getInt("phone"));
        }
    }

    public void sendMessage(View view) {


        database = FirebaseDatabase.getInstance();
        //fireuser = mAuth.getCurrentUser();
        //String user_id = fireuser.getUid();
        User user = new User();
        user.setName(name.getText().toString());
        user.setDepartment(department.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setEmail(email.getText().toString());
        user.setCollege(college.getText().toString());
        user.setCollegeid(college_id.getText().toString());
        //user.setUser_id(Long.parseLong(user_id));
        DatabaseReference mRef = database.getReference().child("Users").child("1");
        mRef.setValue(user);

        userdetails = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=userdetails.edit();
        editor.putString("name",name.getText().toString());
        editor.putString("department",department.getText().toString());
        editor.putString("phone",phone.getText().toString());
        editor.putString("email",email.getText().toString());
        editor.putString("college",college.getText().toString());
        editor.putString("collegeid",college_id.getText().toString());
        editor.apply();

        //userdetails = getSharedPreferences("userInfo", MODE_PRIVATE);
        //String s = userdetails.getString("name", "Nodata");
        //Toast.makeText(this, "name:"+s, Toast.LENGTH_SHORT).show();
    }
}
