package sahil.iiitk_foundationday_app.views;
// Made by Tanuj

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import sahil.iiitk_foundationday_app.R;

public class DetailedEvent extends AppCompatActivity {

    TextView event_detail, event_date_time, event_venue, event_last_date, event_contact, event_prize, event_type,event_regfee;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ArrayList<String> titles;
    ImageView event_picture;
    FloatingActionButton fab;
    HashMap<String, Integer> map = new HashMap<>();
    HashMap<String, Integer> imageMap = new HashMap<>();
    int club_number;
    int event_number;
    int i,min,max;
    FirebaseDatabase db;
    String[] event_data,clubs_array;

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

        //setting image addresses in imageMap
        //todo update all images address ,, currently all are same
        imageMap.put("image00", R.drawable.detail_technical);
        imageMap.put("image01", R.drawable.detail_technical);
        imageMap.put("image02", R.drawable.detail_technical);
        imageMap.put("image03", R.drawable.detail_technical);
        imageMap.put("image04", R.drawable.detail_technical);
        imageMap.put("image05", R.drawable.detail_technical);

        imageMap.put("image10", R.drawable.detail_cultural);
        imageMap.put("image11", R.drawable.detail_cultural);
        imageMap.put("image12", R.drawable.detail_cultural);
        imageMap.put("image13", R.drawable.detail_cultural);
        imageMap.put("image14", R.drawable.detail_cultural);
        imageMap.put("image15", R.drawable.detail_cultural);

        imageMap.put("image20", R.drawable.detail_literary);
        imageMap.put("image21", R.drawable.detail_literary);
        imageMap.put("image22", R.drawable.detail_literary);
        imageMap.put("image23", R.drawable.detail_literary);
        imageMap.put("image24", R.drawable.detail_literary);

        imageMap.put("image30", R.drawable.detail_photography);
        imageMap.put("image31", R.drawable.detail_photography);
        imageMap.put("image32", R.drawable.detail_photography);
        imageMap.put("image33", R.drawable.detail_photography);
        imageMap.put("image34", R.drawable.detail_photography);
        imageMap.put("image35", R.drawable.detail_photography);

        fab = findViewById(R.id.event_fab);
        event_detail = findViewById(R.id.event_more_details);
        event_type = findViewById(R.id.event_type);
        event_date_time = findViewById(R.id.event_date_time);
        event_last_date = findViewById(R.id.event_last_date);
        event_venue = findViewById(R.id.event_venue);
        event_contact = findViewById(R.id.event_contact_details);
        event_prize = findViewById(R.id.event_prize);
        event_regfee=findViewById(R.id.event_regfee);

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
        event_regfee.setText("Registration Fee: \u20B9 "+event_data[10]+"/-");
        event_last_date.setText("Last date to pay registration fee: " + event_data[4]);
        event_contact.setText("Contact: " + event_data[5]);
        event_prize.setText("Prizes worth \u20B9 " + event_data[6] + "/-");
        event_detail.setText(event_data[7]);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if user has registered in the app or not
                SharedPreferences sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
                if (!sharedPreferences.getString("FFID","").isEmpty()){
                    //open registration page
                    Intent intent=new Intent(getApplicationContext(),EventRegActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("club_number",club_number);
                    bundle.putString("event_name",event_data[0]);
                    bundle.putInt("min",min);
                    bundle.putInt("max",max);
                    intent.putExtras(bundle);
                    DetailedEvent.this.startActivity(intent);
                }else{
                    Bundle bundle=new Bundle();
                    if (!sharedPreferences.getString("name","").isEmpty()){
                        bundle.putString("name",sharedPreferences.getString("name",""));
                    }
                    if (!sharedPreferences.getString("email","").isEmpty()){
                        bundle.putString("email",sharedPreferences.getString("email",""));
                    }
                    if (!sharedPreferences.getString("phone","").isEmpty()){
                        bundle.putString("phone",sharedPreferences.getString("phone",""));
                    }
                    Intent intent=new Intent(getApplicationContext(),Register.class);
                    intent.putExtras(bundle);
                    getApplicationContext().startActivity(intent);
                }
            }
        });
    }

}
