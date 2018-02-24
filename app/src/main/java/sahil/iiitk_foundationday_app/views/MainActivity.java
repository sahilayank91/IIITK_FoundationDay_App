package sahil.iiitk_foundationday_app.views;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.adapters.NotifAdapter;
import sahil.iiitk_foundationday_app.model.Notif;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ArrayList<String > titles;
    ImageView BackGround;
    private boolean backPressedToExitOnce = false;
    ArrayList<String> ar=new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager  mLayoutManager;
    RecyclerView.Adapter mAdapter;
    TextView nav_ffid;

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

        ////////////////////////////    PUSHER SHURU
        PusherOptions options = new PusherOptions();
        options.setCluster("APP_CLUSTER");

        Pusher pusher = new Pusher("APP_KEY", options);
        pusher.connect();
        Channel channel = pusher.subscribe("my-channel");
        channel.bind("my-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                System.out.println(data);
            }
        });

        //////////////////////////////////       PUSHER KHATAM
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.coll);
        collapsingToolbarLayout.setTitleEnabled(true);
        BackGround = (ImageView)findViewById(R.id.BG);
        titles = new ArrayList<>();
        {
            titles.add("About");
            titles.add("Events");
            titles.add("Schedule");
            titles.add("Sponsors");
            titles.add("Helpline");
            titles.add("Team");
        }
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
                BackGround.setImageResource(R.drawable.home_back);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /////////////////////////////////////////////////
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
        if (pref.getString("FFID","").equals("")){
            nav_ffid.setText("Register to get FFID");
        }else{
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
        adapter.addFrag(new SponsorsFragment(), "SPONSERS");
        adapter.addFrag(new HelplineFragment(), "HELPLINE");
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
            Intent intent=new Intent(this,QuizActivity.class);
            this.startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Flair-Fiesta 2k18");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Download Flair-Fiesta 2k18 from Play Store.");
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

    public void postNotification(View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Notif notification=new Notif();
        notification.setDetails("First Notification");
        notification.setNotif_id(1);
        notification.setTime("1:08 AM");
        notification.setWhich_club("Technical Club");
        DatabaseReference mRef = database.getReference().child("Notification");
        mRef.push().setValue(notification);
        Toast.makeText(this, "Notification push Successfull!", Toast.LENGTH_SHORT).show();
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
            //Get phone field and append to list
           // notifications.add((String) singleUser.get("details"));
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
        Collections.reverse(info);
        Collections.reverse(when);
        Collections.reverse(which_club);
        //show notifications in cards
        Log.e("notif",info.toString());
        Log.e("notif",when.toString());
        Log.e("notif",which_club.toString());

        //showing notifications in cards
        showNotifCard(info,when,which_club);

        //getting last seen notification from sharedpreferences
        SharedPreferences pref=getSharedPreferences("userInfo",MODE_PRIVATE);
        int n=pref.getInt("last_notif",0);
        SharedPreferences.Editor editor=pref.edit();
        editor.putInt("last_notif",max_notif_id);
        editor.apply();

        //displaying these notifications in user's notification panel
        for (int i=n;i<info.size();i++){
            alertUser(info.get(i),i);
        }
    }

    private void showNotifCard(ArrayList<String> info,ArrayList<String> when,ArrayList<String> which){
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NotifAdapter(info,when,which);
        mRecyclerView.setAdapter(mAdapter);
    }

    private  void alertUser(String a,int NOTIFICATION_ID){
//        //creating  a notification channel
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

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_round)
                        .setContentTitle("FlairFiesta 2k18")
                        .setTicker(a)
                       .setAutoCancel(true)
                        .setPriority(1000)
                        .setContentText(a);
        Intent intent=new Intent(this,Splash_Activity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }
}
