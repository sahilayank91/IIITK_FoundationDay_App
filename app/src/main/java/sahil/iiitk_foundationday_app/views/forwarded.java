package sahil.iiitk_foundationday_app.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;


import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.mail.GMailSender;

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
        /*
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uid=(long)dataSnapshot.getValue();
                uid++;
                dataSnapshot.getRef().setValue(uid);
                Toast.makeText(getApplicationContext(),"entered",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }

        });
        */
        myRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Long value = (long)mutableData.getValue();
                value++;
                uid=value;
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                Log.e("firebase", "transaction:onComplete:" + databaseError);
            }
        });
        Toast.makeText(this,"Your FFID is : FF"+uid,Toast.LENGTH_SHORT).show();
    }
    //sending emails automatically
    public void sendEmail(View view){
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

}
