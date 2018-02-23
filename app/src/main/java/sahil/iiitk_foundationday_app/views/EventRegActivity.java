package sahil.iiitk_foundationday_app.views;

import android.graphics.Color;
import android.graphics.PorterDuff;
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
import java.util.List;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.model.EventReg;

public class EventRegActivity extends AppCompatActivity
{
    LinearLayout lv1,lv2;
    Spinner s1;
    int min,max,club_number,check_number;
    String event_name;
    EditText ed,edd;
    Button btnl;
    List<EditText> allEds = new ArrayList<>();
    List<Integer> lst = new ArrayList<>();
    List<String> IDs=new ArrayList<>();
    EventReg registration=new EventReg();
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_reg);

        db=FirebaseDatabase.getInstance();
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
                if (edd.getText().toString().equals("")){
                    edd.setError("Empty");
                    flag=1;
                }
                if(flag==0)
                {
                    //todo proceed to firebase
                    checkFFID(IDs.get(0));
                }else{
                    IDs.clear();
                }

            }
        });
    }

    public void checkFFID(final String a){
        DatabaseReference databaseReference=db.getReference("Users");
        Query query=databaseReference.orderByChild("user_id").equalTo(a);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    Toast.makeText(getApplicationContext(),"Member with FFID: "+a+" does not exist!",Toast.LENGTH_SHORT).show();
                }
                else{
                    check_number++;
                    if (check_number<IDs.size()){
                        checkFFID(IDs.get(check_number));
                    }else if (check_number==IDs.size()){
                        reg_lastStage();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void reg_secondStage(){
            //todo checking if given users are in other teams or not remaining
    }

    public void reg_lastStage(){
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
        registration.setFFIDs(IDs);
        //get current time
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        registration.setReg_time(formattedDate);
        registration.setTeam_name(edd.getText().toString());
        ref.push().setValue(registration);
    }
}

