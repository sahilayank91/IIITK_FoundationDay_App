package sahil.iiitk_foundationday_app.views;
// Made by Tanuj
import android.app.Activity;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.model.Notif;

public class AdminPage extends Activity {

    String club_name,dialogue_entry;
    TextView admin_club_name;
    HashMap<String,Integer> map=new HashMap<>();
    ArrayList<String> event_names=new ArrayList<String>();
    Spinner event_spinner;
    int club_number,i;
    ListView list;
    FirebaseDatabase db;
    HashMap<Long,ArrayList<String>> ffids=new HashMap<>();
    ArrayList<String> team_contact_email=new ArrayList<>();
    ArrayList<String> team_contact_phone=new ArrayList<>();
    ArrayList<String> team_time=new ArrayList<>();
    ArrayList<String> team_names=new ArrayList<>();
    FloatingActionButton admin_fab;

    //add number of events of each here
    //todo check once at last time
    int[] num={6,6,5,6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        club_name=getIntent().getStringExtra("club_name");
        admin_club_name=findViewById(R.id.admin_club_name);
        String s=club_name.replace('_',' ');
        admin_club_name.setText(s);
        event_spinner=findViewById(R.id.admin_spinner);
        list=findViewById(R.id.admin_registrations);
        admin_fab=findViewById(R.id.admin_fab);

        //data for events of every club
        //todo check once at final time
        map.put("event00", R.array.event00);
        map.put("event01", R.array.event01);
        map.put("event02", R.array.event02);
        map.put("event03", R.array.event03);
        map.put("event04", R.array.event04);
        map.put("event05", R.array.event05);
        map.put("event10", R.array.event10);
        map.put("event11", R.array.event11);
        map.put("event12", R.array.event12);
        map.put("event13", R.array.event13);
        map.put("event14", R.array.event14);
        map.put("event15", R.array.event15);
        map.put("event20", R.array.event20);
        map.put("event21", R.array.event21);
        map.put("event22", R.array.event22);
        map.put("event23", R.array.event23);
        map.put("event24", R.array.event24);
        map.put("event30", R.array.event30);
        map.put("event31", R.array.event31);
        map.put("event32", R.array.event32);
        map.put("event33", R.array.event33);
        map.put("event34", R.array.event34);
        map.put("event35", R.array.event35);

        //determine club number
        if (club_name.equals("Technical_club")) club_number=0;
        else if (club_name.equals("Cultural_club")) club_number=1;
        else if (club_name.equals("Literary_and_dramatics_club")) club_number=2;
        else if (club_name.equals("Fine_arts_and_photography_club")) club_number=3;

        //get all event  names
        i=0;
        while (i<num[club_number]){
            int a = map.get("event"+club_number+""+i);
           String[] data =getResources().getStringArray(a);
           event_names.add(data[0]);
           i++;
        }

        Log.e("adminpage",club_name);
        Log.e("adminpage",event_names.toString());
        //show event names in spinner

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,event_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        event_spinner.setAdapter(adapter);

        //listen for item clicks on spinner
        event_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getRegisteredTeams(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //listen for item clicks on list
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(AdminPage.this);
                builder.setTitle(team_names.get(position));
                String details="";
                ArrayList<Long> keys=new ArrayList<>(ffids.keySet());
                Collections.sort(keys);
                ArrayList<String> members=ffids.get(keys.get(position));
                Log.e("adminpage",members.toString());
                for (int i=0;i<members.size();i++){
                    details+=""+(i+1)+". "+members.get(i)+"\n";
                }
                details+="\n@ "+team_time.get(position)
                +"\nContact: "+team_contact_phone.get(position)+"\n"+team_contact_email.get(position)+"\n";
                builder.setMessage(details);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     dialog.cancel();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

        admin_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminPage.this);
                builder.setTitle("Enter content of notification-");

                // Set up the input
                final EditText input = new EditText(AdminPage.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                // input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                builder.setView(input);

                //   Set up the buttons
                builder.setPositiveButton("GO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogue_entry = input.getText().toString();
                        if (dialogue_entry.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Empty Message!",Toast.LENGTH_SHORT).show();
                        }else{
                            dialog.cancel();
                            //post notification
                            generateNotifID();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private void generateNotifID(){
            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("last_notif");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long id=(long)dataSnapshot.getValue();
                    id=id+1;
                    dataSnapshot.getRef().setValue(id);
                    postNotification(id);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("firebase", "Failed to read value.", databaseError.toException());
                }

            });
    }

    public void postNotification(long notif_id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Notif notification=new Notif();
        notification.setDetails(dialogue_entry);
        notification.setNotif_id(notif_id);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM HH:mm");
        String formattedDate = df.format(c.getTime());
        notification.setTime(formattedDate);
        String a=club_name.replace('_',' ');
        notification.setWhich_club(a);
        DatabaseReference mRef = database.getReference().child("Notification");
        mRef.push().setValue(notification);
        Toast.makeText(this, "Notification push Successfull!", Toast.LENGTH_LONG).show();
    }

    private void getRegisteredTeams(int position){
        db=FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference("Registrations");
        ref=ref.child(club_name);
        ref=ref.child(event_names.get(position));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Log.e("registration","Registrations found!");
                    collectRegistrations((Map<String,Object>) dataSnapshot.getValue());
                }else{
                    Log.e("registration","No registrations found for this event!");
                    team_names.clear();
                    ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,team_names);
                    list.setAdapter(adapter);
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
        long i=0;
        team_names.clear();
        team_contact_phone.clear();
        team_time.clear();
        team_contact_email.clear();
        for (Map.Entry<String, Object> entry : users.entrySet()){
            //Get registration map
            Map singleUser = (Map) entry.getValue();
            ffids.put(i, (ArrayList<String>) singleUser.get("ffids"));
            team_names.add((String)singleUser.get("team_name"));
            team_time.add((String) singleUser.get("reg_time"));
            team_contact_email.add((String) singleUser.get("email"));
            team_contact_phone.add((String) singleUser.get("phone"));
            i++;
        }
        Log.e("adminpage",team_names.toString());
        Log.e("adminpage",ffids.toString());
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,team_names);
        list.setAdapter(adapter);
    }
}
