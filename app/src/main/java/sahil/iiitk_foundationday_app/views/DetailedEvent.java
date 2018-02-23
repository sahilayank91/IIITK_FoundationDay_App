package sahil.iiitk_foundationday_app.views;
// Made by Tanuj

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.List;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.model.EventReg;

public class DetailedEvent extends AppCompatActivity {

    TextView event_detail, event_date_time, event_venue, event_last_date, event_contact, event_prize, event_type;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ArrayList<String> titles;
    ImageView event_picture;
    FloatingActionButton fab;
    HashMap<String, Integer> map = new HashMap<>();
    HashMap<String, Integer> imageMap = new HashMap<>();
    int club_number;
    int event_number;
    EventReg registration=new EventReg();
    int i,check_number=0,min,max;
    FirebaseDatabase db;
    List<String> names=new ArrayList<>();;
    String[] event_data,clubs_array;
    String club_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_event);
        db=FirebaseDatabase.getInstance();
        //setting the views
        Toolbar toolbar = findViewById(R.id.event_toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = findViewById(R.id.event_collapsing);
        collapsingToolbarLayout.setTitleEnabled(true);
        event_picture = findViewById(R.id.event_picture);

        //getting chosen club and its event
        club_number = getIntent().getIntExtra("club_number", 0);
        event_number = getIntent().getIntExtra("event_number", 0);

        //setting address values in hash-map
        map.put("event00", R.array.event00);
        //todo add all events for full working

        //setting image addresses in imageMap
        imageMap.put("image00", R.drawable.home_back);

        //todo add all images address

        fab = findViewById(R.id.event_fab);
        event_detail = findViewById(R.id.event_more_details);
        event_type = findViewById(R.id.event_type);
        event_date_time = findViewById(R.id.event_date_time);
        event_last_date = findViewById(R.id.event_last_date);
        event_venue = findViewById(R.id.event_venue);
        event_contact = findViewById(R.id.event_contact_details);
        event_prize = findViewById(R.id.event_prize);

        //fetching stored event data from xml file according to Map
        clubs_array=getResources().getStringArray(R.array.club_names);
        event_data = getResources().getStringArray(map.get("event" + club_number + "" + event_number));
        min=Integer.parseInt(event_data[8]);
        max=Integer.parseInt(event_data[9]);

        //display data in fields
        event_picture.setImageResource(imageMap.get("image" + club_number + "" + event_number));
        collapsingToolbarLayout.setTitle(event_data[0]);
        event_date_time.setText(event_data[1]);
        event_type.setText(event_data[2]);
        event_venue.setText(event_data[3]);
        event_last_date.setText("Last date to pay registration fee: " + event_data[4]);
        event_contact.setText("Contact: " + event_data[5]);
        event_prize.setText("Prizes worth \u20B9 " + event_data[6] + "/-");
        event_detail.setText(event_data[7]);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open registration page
                Intent intent=new Intent(getApplicationContext(),EventRegActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("club_number",club_number);
                bundle.putString("event_name",event_data[0]);
                bundle.putInt("min",min);
                bundle.putInt("max",max);
                intent.putExtras(bundle);
                getApplicationContext().startActivity(intent);
            }
        });
    }

}
