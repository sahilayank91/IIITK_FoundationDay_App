package sahil.iiitk_foundationday_app.views;
//Made by Tanuj
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sahil.iiitk_foundationday_app.R;

public class MainFragment2 extends Fragment {

//it is for about screen or first page in view pager
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main2, container, false);
        return v;
    }
}
