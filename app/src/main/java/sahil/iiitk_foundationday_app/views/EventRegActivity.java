package sahil.iiitk_foundationday_app.views;
// Made by Tanuj
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.mail.GMailSender;
import sahil.iiitk_foundationday_app.model.EventReg;

public class EventRegActivity extends AppCompatActivity
{
    LinearLayout lv1,lv2;
    ScrollView back;
    Spinner s1;
    int min,max,club_number,check_number;
    String event_name,body;
    EditText ed,edd;
    Button btnl;
    List<EditText> allEds = new ArrayList<>();
    List<Integer> lst = new ArrayList<>();
    List<String> IDs=new ArrayList<>();
    EventReg registration=new EventReg();
    FirebaseDatabase db;
    SharedPreferences savedData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_reg);

        back=findViewById(R.id.reg_back);
        try{
            back.setBackgroundResource(R.drawable.th);
        }catch (OutOfMemoryError e){
            Log.e("image","ImageError: "+e.getMessage());
        }

        db=FirebaseDatabase.getInstance();
        savedData=getSharedPreferences("userInfo",MODE_PRIVATE);

        //get values passed by intent
        Bundle bundle=getIntent().getExtras();
        event_name=bundle.getString("event_name");
        club_number=bundle.getInt("club_number");
        min=bundle.getInt("min");
        max=bundle.getInt("max");

        try {
            for (int f = 0; f <= (max - min); f++) {
                lst.add(f + min);
            }
            s1 = findViewById(R.id.s1);
            lv1 = findViewById(R.id.lv1);
            lv2 =  findViewById(R.id.lv2);
            btnl = findViewById(R.id.go);
            ArrayAdapter<Integer> aa1 = new ArrayAdapter<>(this, R.layout.spinner_register, lst);
            s1.setAdapter(aa1);
            aa1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            s1.getBackground().setColorFilter(Color.parseColor("#cccccc"), PorterDuff.Mode.SRC_ATOP);
            int id=90;
            if(min>=2)
            {
                edd = new EditText(getApplicationContext());
                edd.setId(id);
                edd.getBackground().setColorFilter(getResources().getColor(R.color.hh), PorterDuff.Mode.SRC_ATOP);
                edd.setHint("Team Name");
                edd.setHintTextColor(getResources().getColor(R.color.hh));
                edd.setTextColor(getResources().getColor(R.color.tt));
                edd.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,
                        AbsListView.LayoutParams.WRAP_CONTENT));
                lv2.addView(edd);
            }
            s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    lv1.removeAllViews();
                    allEds.clear();
                    for(int t=0;t<(i+min);t++)
                    {
                        ed = new EditText(getApplicationContext());
                        allEds.add(ed);
                        ed.setId(t+1);
                        ed.getBackground().setColorFilter(getResources().getColor(R.color.hh), PorterDuff.Mode.SRC_ATOP);
                        ed.setHint("Member "+(t+1)+" FFID");
                        ed.setHintTextColor(getResources().getColor(R.color.hh));
                        ed.setTextColor(getResources().getColor(R.color.tt));
                        ed.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,
                                AbsListView.LayoutParams.WRAP_CONTENT));
                        lv1.addView(ed);
                    }
                    allEds.get(0).setText(""+savedData.getString("FFID",""));
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),"Problem",Toast.LENGTH_SHORT).show();
        }
        btnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IDs.clear();
                check_number=0;
                int flag=0;
                String[] strings = new String[allEds.size()];
                for(int i=0; i < allEds.size(); i++) {
                    strings[i] = allEds.get(i).getText().toString();
                    if (strings[i].equals("")) {
                        allEds.get(i).setError("Enter FFID");
                        flag=1;
                    } else
                    {
                        IDs.add(strings[i]);
                    }
                }
                if (min>=2){
                    if (edd.getText().toString().equals("")){
                        edd.setError("Empty");
                        flag=1;
                    }
                }
                if(flag==0)
                {
                    //check if entered FFIDs have the registered user's FFID or not
                    //so that user can't register for other people unless he is in the team
                    if (IDs.contains(savedData.getString("FFID",""))){
                        Log.e("registration","Going to check FFIDs");
                        String college_name=savedData.getString("college","");
                        if (college_name.equals("IIIT KOTA") || college_name.equals("MNIT JAIPUR")){
                            checkFFID(IDs.get(0));
                        }else{
                            DatabaseReference databaseReference=db.getReference("Users");
                            Query query=databaseReference.orderByChild("user_id").equalTo(IDs.get(0));
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        if (max==1){
                                            i.setData(Uri.parse("https://www.townscript.com/e/solo-event-flairfiesta-iiitk-334121/"));
                                        }else{
                                            i.setData(Uri.parse("https://www.townscript.com/e/team-event-flairfiesta-iiitk-334121/"));
                                        }
                                        EventRegActivity.this.startActivity(i);
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Provided FFID does not exist!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                        }
                    }else{
                        Log.e("registration","User's FFID is not present in the list!");
                        Toast.makeText(getApplicationContext(),"You can't register for others unless you have a team!",Toast.LENGTH_LONG).show();
                    }

                }else{
                    IDs.clear();
                }

            }
        });
    }
        //registration ,validation and confirmation work done by
        //Tanuj
    public void checkFFID(final String a){
        DatabaseReference databaseReference=db.getReference("Users");
        Query query=databaseReference.orderByChild("user_id").equalTo(a);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    allEds.get(check_number).setError("This FFID does not exist!");
                    Toast.makeText(getApplicationContext(),"Member with FFID: "+a+" does not exist!",Toast.LENGTH_SHORT).show();
                }
                else{
                    check_number++;
                    if (check_number<IDs.size()){
                        checkFFID(IDs.get(check_number));
                    }else if (check_number==IDs.size()){
                        check_number=0;
                        reg_secondStage();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void reg_secondStage(){
        Log.e("registration","Second stage called!");
        DatabaseReference ref=db.getReference("Registrations");
        switch (club_number){
            case 0: ref=ref.child("Technical_club");
                break;
            case 1: ref=ref.child("Cultural_club");
                break;
            case 2: ref=ref.child("Literary_and_dramatics_club");
                break;
            case 3: ref=ref.child("Fine_arts_and_photography_club");
                break;
        }
        ref=ref.child(event_name);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                Log.e("registration","Snapshot exists!");
                collectRegistrations((Map<String,Object>) dataSnapshot.getValue());
            }else{
                Log.e("registration","Snapshot does not exists!");
                reg_lastStage();
            }

        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}

    private void collectRegistrations(Map<String,Object> users) {
        //iterate through each registration
        Log.e("registration","Collecting all registrations!");
        boolean goodToGo=true;
        HashMap<Long,ArrayList<String>> ffids=new HashMap<Long, ArrayList<String>>();
        long i=0;
        for (Map.Entry<String, Object> entry : users.entrySet()){
            //Get registration map
            Map singleUser = (Map) entry.getValue();
            ffids.put(i, (ArrayList<String>) singleUser.get("ffids"));
            i++;
            }
            ArrayList<Long> keys= new ArrayList<>(ffids.keySet());
        for (int j=0;j<ffids.keySet().size();j++){
            Log.e("registration","outer call no. "+j);
            ArrayList<String> single_reg=ffids.get(keys.get(j));
            Log.e("registration",single_reg.toString());
            Log.e("registration",IDs.toString());
            for (String a : IDs){
                Log.e("registration","inner call no. "+j);
                if (single_reg.contains(a)){
                    goodToGo=false;
                    Log.e("registration","Found match for FFID!");
                    break;
                }
            }
        }
        if (goodToGo){
            reg_lastStage();
        }else{
            Toast.makeText(getApplicationContext(),"One or more of these FFIDs are already registered for this event!",Toast.LENGTH_LONG).show();
        }
    }

    public void reg_lastStage(){
        Log.e("registration","Last stage called!");
        DatabaseReference ref=db.getReference("Registrations");
        switch (club_number){
            case 0: ref=ref.child("Technical_club");
                break;
            case 1: ref=ref.child("Cultural_club");
                break;
            case 2: ref=ref.child("Literary_and_dramatics_club");
                break;
            case 3: ref=ref.child("Fine_arts_and_photography_club");
                break;
        }
        ref=ref.child(event_name);
        registration.setFFIDs(IDs);
        //get current time
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        registration.setReg_time(formattedDate);
        String team_name;
        if (min>=2){
            team_name=edd.getText().toString();
        }
        else{
            team_name=savedData.getString("name","");
        }
        registration.setTeam_name(team_name);
        registration.setEmail(savedData.getString("email",""));
        registration.setPhone(savedData.getString("phone",""));
        ref.push().setValue(registration);
        Toast.makeText(this,"Your Team is registered for "+event_name,Toast.LENGTH_LONG).show();
        //send confirmation mail
        sendEmail(IDs,savedData.getString("name",""),savedData.getString("email",""),event_name,team_name);

        //go back to home activity
        Intent intent=new Intent(this,MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    //sending emails automatically
    public void sendEmail(List<String> ffids, String name, String email, final String event_name,final String team_name){
        body="Hi,\n"+name+" ( "+email+" )\nYour registration for "+event_name+" in Flair Fiesta 2k18 is now confirmed."
                + "\nTeam Name: "+team_name
                +"\nYour team members are - ";
        for (int i=0;i<ffids.size();i++){
            body=body+"\n"+(i+1)+". "+ffids.get(i);
        }
        body=body+"\n\nWe at Organzing Team of Flair Fiesta 2k18 will be glad to have you with us on 23Mar 18 and 24Mar 18."
        +"\n\nThanks and Regards"
        +"\nAdmin";
        final String recepient=email;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("tanujm242@gmail.com", "geniusmehta");
                    sender.sendMail("Your Registration for "+event_name+" in Flair Fiesta 2k18 is Confirmed.",
                            body,
                            "tanujm242@gmail.com",
                            recepient);
                    Log.e("registration","Sending confirmation email to: "+recepient);
                } catch (Exception e) {
                    Log.e("registration", e.getMessage(), e);
                }
            }
        }).start();

    }

}

