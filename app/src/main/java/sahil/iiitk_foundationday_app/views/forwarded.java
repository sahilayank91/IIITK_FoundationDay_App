package sahil.iiitk_foundationday_app.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import sahil.iiitk_foundationday_app.R;

public class forwarded extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forwarded);
        TextView text=(TextView)findViewById(R.id.text);
        Bundle extras=getIntent().getExtras();
        if (extras!=null){
            String details="Name: " +extras.getString("name");
            details=details+"\nEmail: "+extras.getString("email");
            details=details+"\nPhone:"+extras.getString("phone");
            text.setText(details);
        }
    }

}
