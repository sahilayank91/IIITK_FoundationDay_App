package sahil.iiitk_foundationday_app;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sahil.iiitk_foundationday_app.model.User;

public class MainActivity extends AppCompatActivity {


    public Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit = (Button)findViewById(R.id.register);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

    }
}