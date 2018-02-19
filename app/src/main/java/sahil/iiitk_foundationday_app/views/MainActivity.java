package sahil.iiitk_foundationday_app.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import java.util.ArrayList;
import java.util.List;

import sahil.iiitk_foundationday_app.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ArrayList<String > titles;
    ImageView BackGround;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // getSupportActionBar().hide();

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        BackGround = (ImageView)findViewById(R.id.BG);
       // setSupportActionBar(toolbar);
        //   for(int i=0; i<6; i++)
        titles = new ArrayList<>();
        {
            titles.add("vip");
            titles.add("kit");
            titles.add("jam");
            titles.add("lit");
            titles.add("as");
            titles.add("hell");
        }
        // mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        // mViewPager.setAdapter(mSectionsPagerAdapter);
        setupViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // change your title
                // inflate menu
                // customize your toolbar
                Log.e("lala",titles.get(position));
                ///Toast.makeText(this,"fd",Toast.LENGTH_SHORT);
                // TextView title_change = (TextView) findViewById(R.id.title);
                //title_change.0(titles.get((position)));
                // getSupportActionBar().setTitle(titles.get(position));
                // setSupportActionBar(toolbar);
                collapsingToolbarLayout.setTitleEnabled(false);
                //toolbar.setTitle("kkljkljlk");

                toolbar.setTitle(titles.get(position));
                // getSupportActionBar().setTitle(titles.get(position));
                // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                BackGround.setImageResource(R.drawable.ic_menu_share);
                //
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                // Log.e("lala","happens2");
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
    }
    // @Override
   /* public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }*/

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

        else {


        }

        return super.onOptionsItemSelected(item);
    }



   /* public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            int temp = 0;
           if(z==1){
               rootView = inflater.inflate( R.layout.fragment_main, container, false);
                         }
            else {
               rootView = inflater.inflate( R.layout.fragment_main2, container, false);
           }

         ////   TextView textView = (TextView) rootView.findViewById(R.id.section_label);
           // textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
           // textView.setText( "djfdk" );
            return rootView;
        }
    } */

    /*  public class SectionsPagerAdapter extends FragmentPagerAdapter {

          public SectionsPagerAdapter(FragmentManager fm) {
              super(fm);
          }

          @Override
          public Fragment getItem(int position) {
              return PlaceholderFragment.newInstance(position + 1);
          }

          @Override
          public int getCount() {
              return 6;
          }

          @Override
          public CharSequence getPageTitle(int position) {
              switch (position) {
                  case 0:
                      return "SECTION 1";
                  case 1:
                      return "SECTION 2";
                  case 2:
                      return "SECTION 3";
                  case 3:
                      return "SECTION 1";
                  case 4:
                      return "SECTION 2";
                  case 5:
                      return "SECTION 3";
              }
              return null;
          }
      }
  */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Log.e("triple dot added","now done");
        getMenuInflater().inflate(R.menu.main, menu);

        //Toast.makeText(this,"triple dots aakldjf",Toast.LENGTH_LONG).show();
        return true;
    }
}
