package sahil.iiitk_foundationday_app.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import sahil.iiitk_foundationday_app.R;

public class DetailedEvent extends AppCompatActivity {

    TextView event_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_event);

        event_detail=(TextView)findViewById(R.id.event_detail);
        int club_number=getIntent().getIntExtra("club_number",0);
        int event_number=getIntent().getIntExtra("event_number",0);
        event_detail.setText("Club Number: "+club_number+"\nEvent number: "+event_number);
    }

}
