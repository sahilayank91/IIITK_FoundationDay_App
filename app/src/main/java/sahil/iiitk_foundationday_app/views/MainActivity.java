package sahil.iiitk_foundationday_app.views;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.adapters.NotifAdapter;
import sahil.iiitk_foundationday_app.model.AdminIDs;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ArrayList<String> titles;
    ImageView BackGround;
    ArrayList<Integer> backgrounds;
    private boolean backPressedToExitOnce = false;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager  mLayoutManager;
    RecyclerView.Adapter mAdapter;
    TextView nav_ffid;
    String dialogue_entry;
    FirebaseDatabase db;
    DatabaseReference ref;
    AdminIDs admin=new AdminIDs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       //requesting location permission from user
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
        }

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.coll);
        collapsingToolbarLayout.setTitleEnabled(true);
        BackGround = (ImageView)findViewById(R.id.BG);
        titles = new ArrayList<>();
        {
            titles.add("About");
            titles.add("Events");
            titles.add("Schedule");
            //titles.add("Sponsors");
            titles.add("Contacts");
            titles.add("Team");
        }
        backgrounds=new ArrayList<>();
        backgrounds.add(R.drawable.home_back);
        backgrounds.add(R.drawable.home_back);
        //backgrounds.add(R.drawable.home_back);
        backgrounds.add(R.drawable.home_back);
        backgrounds.add(R.drawable.home_back);
        backgrounds.add(R.drawable.home_back);

        collapsingToolbarLayout.setTitle(titles.get(0));
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("page",titles.get(position));
                collapsingToolbarLayout.setTitle(titles.get(position));
                BackGround.setImageResource(backgrounds.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavigationView notif_nav=findViewById(R.id.nav_view_2);
        mRecyclerView = notif_nav.getHeaderView(0).findViewById(R.id.notif_recycler);

        //showing user's FFID if it exists
        nav_ffid=navigationView.getHeaderView(0).findViewById(R.id.nav_ffid);
        SharedPreferences pref=getSharedPreferences("userInfo",MODE_PRIVATE);
        if (!pref.getString("FFID","").equals("")){
            nav_ffid.setText("Your FFID is "+pref.getString("FFID",""));
        }

        //getting notifications
        getNotifications();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MainFragment2(), "ABOUT");
        adapter.addFrag(new ClubsFragment(), "EVENTS");
        adapter.addFrag(new ScheduleFragment(), "SCHEDULE");
        //adapter.addFrag(new SponsorsFragment(), "SPONSORS");
        adapter.addFrag(new HelplineFragment(), "CONTACTS");
        adapter.addFrag(new TeamFragment(), "TEAM");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.bell) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.END);
            return true;
        }
        if (id==R.id.logoutInside){
            SharedPreferences sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.clear();
            editor.apply();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            GoogleSignInClient client=GoogleSignIn.getClient(this,gso);
            client.signOut();
            Intent intent=new Intent(this,Login_Screen.class);
            this.startActivity(intent);
            Toast.makeText(this,"Logged out successfully!",Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)){
            drawer.closeDrawer(GravityCompat.END);
        }else{
            //double back press logic
            if (backPressedToExitOnce) {
                super.onBackPressed();
            } else {
                this.backPressedToExitOnce = true;
                Toast.makeText(getApplicationContext(),"Press again to exit",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backPressedToExitOnce = false;
                    }
                }, 2000);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_register) {
            SharedPreferences sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
            if (!sharedPreferences.getString("FFID","").isEmpty()){
                Toast.makeText(getApplicationContext(),"You are already registered!",Toast.LENGTH_SHORT).show();
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
                Intent intent=new Intent(this,Register.class);
                intent.putExtras(bundle);
                this.startActivity(intent);
                finish();
            }

        } else if (id == R.id.nav_reaches) {
            Intent intent=new Intent(this, MapActivity.class);
            this.startActivity(intent);

        } else if (id == R.id.nav_queries) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","tanujm242@gmail.com", null));
            this.startActivity(Intent.createChooser(emailIntent, "Send Email via"));

        } else if (id == R.id.nav_quiz) {
            //check if the user has an account in the app or not
            SharedPreferences sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
            if (sharedPreferences.getString("FFID","").isEmpty()){
                Toast.makeText(this,"You have register in the App to play game.",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent=new Intent(this,QuizActivity.class);
                this.startActivity(intent);
            }

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Flair-Fiesta 2k18");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Download Flair-Fiesta 2k18 from Play Store. https://play.google.com/store/apps/details?id=sahil.iiitk_foundationday_app ");
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (id==R.id.nav_fb){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.facebook.com/iiitkfd"));
            this.startActivity(i);

        }else if (id==R.id.nav_website){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://www.flairfiesta.com/"));
            this.startActivity(i);
        }
        else if (id==R.id.nav_admin){
            launchAdmin();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void getNotifications(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Notification");
        Query query= ref.orderByChild("notif_id");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                collectNotifications((Map<String,Object>) dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void collectNotifications(Map<String,Object> users) {
        //iterate through each notification
        HashMap<Long,String> details=new HashMap<Long, String>();
        HashMap<Long,String> times=new HashMap<Long, String>();
        HashMap<Long,String> club_names=new HashMap<Long, String>();
        List keys;
        ArrayList<String> info=new ArrayList<>();
        ArrayList<String> when=new ArrayList<>();
        ArrayList<String> which_club=new ArrayList<>();
        for (Map.Entry<String, Object> entry : users.entrySet()){
            //Get user map
            Map singleUser = (Map) entry.getValue();
            details.put((Long)singleUser.get("notif_id"),(String)singleUser.get("details"));
            times.put((Long)singleUser.get("notif_id"),(String)singleUser.get("time"));
            club_names.put((Long)singleUser.get("notif_id"),(String)singleUser.get("which_club"));
        }
        //sort the notifications
        keys = new ArrayList(details.keySet());
        Collections.sort(keys);
        int max_notif_id=keys.size();
        //Fill array which is sorted and ready
        for (int i=0;i<keys.size();i++){
            info.add(details.get(keys.get(i)));
            when.add(times.get(keys.get(i)));
            which_club.add(club_names.get(keys.get(i)));
        }
        //copy info list for use in alerting by notification
        ArrayList<String> copy=new ArrayList<>();
        for (int i=0;i<info.size();i++){
            String x=info.get(i);
            copy.add(x);
        }
        //reverse all lists so that newest notification comes on top
        Collections.reverse(info);
        Collections.reverse(when);
        Collections.reverse(which_club);
        //show notifications in cards
        Log.e("notif",info.toString());
        Log.e("notif",when.toString());
        Log.e("notif",which_club.toString());
        Log.e("notif",""+max_notif_id);
        //showing notifications in cards
        showNotifCard(info,when,which_club);

        //getting last seen notification from sharedpreferences
        SharedPreferences pref=getSharedPreferences("userInfo",MODE_PRIVATE);
        int n=pref.getInt("last_notif",0);
        SharedPreferences.Editor editor=pref.edit();
        editor.putInt("last_notif",max_notif_id);
        editor.apply();
        Log.e("notif",""+n+", "+max_notif_id);
        //displaying these notifications in user's notification panel
        Log.e("notif",copy.toString());

        for (int i=n;i<max_notif_id;i++){
            alertUser(copy.get(i),i);
        }
    }

    private void showNotifCard(ArrayList<String> info,ArrayList<String> when,ArrayList<String> which){
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NotifAdapter(info,when,which);
        mRecyclerView.setAdapter(mAdapter);
    }

    private  void alertUser(String a,int NOTIFICATION_ID){
        Log.e("notif","notifying: "+a);
//        //creating  a notification channel
        //todo setup notifications for newer versions of android >=26
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//// The id of the channel.
//        String id = "my_channel";
//// The user-visible name of the channel.
//        CharSequence name = "Flair-Fiesta";
//// The user-visible description of the channel.
//        String description ="Flair-Fiesta";
//        int importance = NotificationManager.IMPORTANCE_HIGH;
//        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
//// Configure the notification channel.
//        mChannel.setDescription(description);
//        mChannel.enableLights(true);
//// Sets the notification light color for notifications posted to this
//// channel, if the device supports this feature.
//        mChannel.setLightColor(Color.RED);
//        mChannel.enableVibration(true);
//        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//        mNotificationManager.createNotificationChannel(mChannel);

        if (Build.VERSION.SDK_INT<26){
            //notification for devices running SDK<26
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher_round)
                            .setContentTitle("FlairFiesta 2k18")
                            .setTicker(a)
                            .setAutoCancel(true)
                            .setPriority(1000)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setContentText(a);
            Intent intent=new Intent(this,Splash_Activity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.notify(NOTIFICATION_ID, builder.build());
        }

    }

    private void launchAdmin(){
        //launch a dialogue to verify Admin ID
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Admin ID");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
       // input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        //   Set up the buttons
        builder.setPositiveButton("GO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogue_entry = input.getText().toString();
                dialog.cancel();
               checkAdminID();
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

    private  void checkAdminID(){
        db=FirebaseDatabase.getInstance();
        ref=db.getReference("Admin");
        Query query=ref;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Log.e("adminpage","Data Snapshot exists!");
                    Map<String,Object> data=(Map<String,Object>) dataSnapshot.getValue();
                    getAdminIDs(data);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getAdminIDs(Map<String,Object> users){
        ArrayList<String> adminIDs=new ArrayList<>();
        ArrayList<String> adminNames=new ArrayList<>();
        for (Map.Entry<String, Object> entry : users.entrySet()){
            //Get admin user map
            Map singleUser = (Map) entry.getValue();
            adminIDs.add((String)singleUser.get("id"));
            adminNames.add((String)singleUser.get("name"));
        }

        Log.e("adminpage",adminIDs.toString());
        if (adminIDs.contains(dialogue_entry)){
            Log.e("adminpage","Admin ID confirmed!");
            int index=adminIDs.indexOf(dialogue_entry);
            String club_name=adminNames.get(index);
            Toast.makeText(getApplicationContext(),"Welcome Admin!",Toast.LENGTH_SHORT).show();
            //goto Admin page
            Intent intent=new Intent(getApplicationContext(),AdminPage.class);
            intent.putExtra("club_name",club_name);
            this.startActivity(intent);
        }else{
            Log.e("adminpage","Wrong Admin ID");
            Toast.makeText(this,"Wrong Admin ID!",Toast.LENGTH_SHORT).show();
        }
    }
//use when need to put more adminIDs
    private  void putAdminIDs(){
        db=FirebaseDatabase.getInstance();
        ref=db.getReference("Admin");
        admin.setName("Fine_arts_and_photography_club");
        admin.setId("abc");
        ref.push().setValue(admin);
        Log.e("adminpage","One Admin ID pushed!");
    }
}
