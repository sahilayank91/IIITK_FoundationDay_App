package sahil.iiitk_foundationday_app.views;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.mail.GMailSender;

import sahil.iiitk_foundationday_app.R;

public class forwarded extends AppCompatActivity {

    long uid=0;
    String body="";
    String details="";
    String name="";
    String email="";
    String phone="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forwarded);
        TextView text=(TextView)findViewById(R.id.text);
        Bundle extras=getIntent().getExtras();
        if (extras!=null){
            if (extras.getString("name")!=null) name=extras.getString("name");
            if (extras.getString("phone")!=null) phone=extras.getString("phone");
            if (extras.getString("email")!=null) email=extras.getString("email");
            details="Name: "+name+"\nEmail: "+email+"\nPhone"+phone;
            text.setText(details);
        }
    }
    public void getFFid(View view){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FFID");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uid=(long)dataSnapshot.getValue();
                uid++;
                dataSnapshot.getRef().setValue(uid);
                sendEmail(uid);
                Toast.makeText(getApplicationContext(),"Your FFID is FF"+uid,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("firebase", "Failed to read value.", databaseError.toException());
            }
        });

    }
    //sending emails automatically
    public void sendEmail(long id){
        //use generated id here to give user his FFID
        body="Hi,\n"+name+" ( "+email+" )\n You have successfully registered for FlairFiesta 2k18, annual cultural-technical fest of" +
                " IIIT Kota.\nWe would be glad to see you on 23rd and 24th March,2018.";
        final String recepient;
        if (email.isEmpty()){
            Toast.makeText(this,"Email is not provided!",Toast.LENGTH_SHORT).show();
            return;
        }else{
            recepient=email;
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

    //this method checks if a registered account exists for the given email id in
    //firebase. Appropriate action to be done inside if-else statements to get required usage.
    //this will be used on the onCreate() method of login screen
    public void checkEmail(String a){
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference().child("Users");
        Query query=ref.orderByChild("email").equalTo(a);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //do appropriate action here when account with this email exists
                    Toast.makeText(getApplicationContext(),"Email exists",Toast.LENGTH_SHORT).show();
                }else{
                    //do appropriate action here when account with this email does not exist
                    Toast.makeText(getApplicationContext(),"Email does not exist!",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //this method checks if a registered account exists for the given phone number in
    //firebase. Appropriate action to be done inside if-else statements to get required usage.
    //this will be used on the onCreate() method of login screen
    public void checkPhone(String a){
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference().child("Users");
        Query query=ref.orderByChild("phone").equalTo(a);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //do appropriate action here when account with this phone number exists
                    Toast.makeText(getApplicationContext(),"Phone number exists",Toast.LENGTH_SHORT).show();
                }else{
                    //do appropriate action here when account with this phone number does not exist
                    Toast.makeText(getApplicationContext(),"Phone number does not exist!",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //this method checks if a registered account exists for the given user_id/FFID in
    //firebase. Appropriate action to be done inside if-else statements to get required usage.
    //this will be used on the onCreate() method of login screen
    public void checkFFID(String a){
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference().child("Users");
        Query query=ref.orderByChild("user_id").equalTo(a);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //do appropriate action here when account with this user_id/FFID number exists
                    Toast.makeText(getApplicationContext(),"FFID exists",Toast.LENGTH_SHORT).show();
                }else{
                    //do appropriate action here when account with this user_id/FFID does not exist
                    Toast.makeText(getApplicationContext(),"FFID does not exist!",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //this method checks shared preferences of the app to determine if the user is logged in in the app or not.
    //this will be used while displaying the splash screen on its onCreate() method
    public boolean isLoggedIn(){
        SharedPreferences sp=getSharedPreferences("userInfo",this.MODE_PRIVATE);
        if (sp.getString("status","false").equals("true"))
            return true;
        else return false;
    }


}
