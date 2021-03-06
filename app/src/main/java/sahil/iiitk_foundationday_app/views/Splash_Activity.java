package sahil.iiitk_foundationday_app.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import sahil.iiitk_foundationday_app.R;

public class Splash_Activity extends AppCompatActivity {
    // Splash Screen
    private TextView tv;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        SharedPreferences pref = getSharedPreferences("MyPref",MODE_PRIVATE);
//        SharedPreferences.Editor edit = pref.edit();
//        edit.putString("flag","1");
//        edit.apply();
        // 1 = first time
        // 0 = second

        //set GIF background
        LinearLayout back=findViewById(R.id.splash_back);
        try{
            GifDrawable image=new GifDrawable(getResources(),R.drawable.splash_back);
            back.setBackground(image);
        }catch (IOException e){

        }

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status));
        }
        tv = (TextView) findViewById(R.id.tv);
        iv = (ImageView) findViewById(R.id.iv);
        Typeface type = Typeface.createFromAsset(getAssets(), "font/Sofia-Regular.otf");
        tv.setTypeface(type);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        tv.startAnimation(myanim);
        iv.startAnimation(myanim);
        Thread loading = new Thread(){
            public void run()
            {
                try{
                    //todo add sleep
                    sleep(4000);
                    Intent i;
                       if (isLoggedIn()){
                           i = new Intent(getApplicationContext(),MainActivity.class);
                       }else{
                           i=new Intent(getApplicationContext(),Login_Screen.class);
                       }
                        startActivity(i);
                        finish();
                }
                catch (Exception ex)
                {
                    Log.e("thread",""+ex.getMessage());
                }
            }
        };
        loading.start();
    }
    public boolean isLoggedIn(){
        SharedPreferences sp=getSharedPreferences("userInfo",this.MODE_PRIVATE);
        if (sp.getString("status","false").equals("true"))
            return true;
        else return false;
    }
}
