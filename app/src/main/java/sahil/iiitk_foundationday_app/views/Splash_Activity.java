package sahil.iiitk_foundationday_app.views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sahil.iiitk_foundationday_app.R;

public class Splash_Activity extends AppCompatActivity
{
    // Splash Screen
    private TextView tv;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);

        // hide Action Bar
        ActionBar action = getSupportActionBar();
        action.hide();
        action.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
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
                   // sleep(4000);
                    Intent i = new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    finish();
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        };
        loading.start();
    }
}
