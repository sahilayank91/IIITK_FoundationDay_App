package sahil.iiitk_foundationday_app.views;
// Made by tanuj
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import sahil.iiitk_foundationday_app.R;

public class ScheduleFragment extends Fragment {

    ImageView schedule1,schedule2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_schedule, container, false);
        schedule1=view.findViewById(R.id.schedule_image1);
        schedule2=view.findViewById(R.id.schedule_image2);
        try{
            schedule1.setImageResource(R.drawable.schedule1);
        }catch (OutOfMemoryError e){
            Log.e("image","ImageError: "+e.getMessage());
        }
        try{
            schedule2.setImageResource(R.drawable.schedule2);
        }catch (OutOfMemoryError e){
            Log.e("image","ImageError: "+e.getMessage());
        }
        return view;
    }

}
