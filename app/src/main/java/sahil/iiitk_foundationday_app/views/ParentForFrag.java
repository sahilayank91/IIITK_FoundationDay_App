package sahil.iiitk_foundationday_app.views;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sahil.iiitk_foundationday_app.R;

public class ParentForFrag extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_for_frag);
        //show fragment
        Fragment fragment=new TeamFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content,fragment).commit();
    }
}
