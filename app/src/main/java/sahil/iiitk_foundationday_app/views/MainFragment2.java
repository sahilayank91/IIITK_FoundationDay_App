package sahil.iiitk_foundationday_app.views;
//Made by Tanuj
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sahil.iiitk_foundationday_app.R;

public class MainFragment2 extends Fragment {

//it is for about screen or first page in view pager
    TextView about;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main2, container, false);
        about=v.findViewById(R.id.about_data);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "font/Sofia-Regular.otf");
        about.setTypeface(type);
        return v;
    }
}
