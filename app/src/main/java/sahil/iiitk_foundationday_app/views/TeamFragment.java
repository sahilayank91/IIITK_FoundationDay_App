package sahil.iiitk_foundationday_app.views;
// Made by tanuj
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.adapters.CustomAdapter;

public class TeamFragment extends Fragment {

    protected String[] names,emails,facebookIDs,linkedinIDs;
    protected String[] positions;
//    List<Integer> images=new ArrayList<>();
    List<String> images_url = new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_team, container, false);

        positions=getResources().getStringArray(R.array.positions);
        names=getResources().getStringArray(R.array.names);
        emails=getResources().getStringArray(R.array.emails);
        facebookIDs=getResources().getStringArray(R.array.facebook);
        linkedinIDs=getResources().getStringArray(R.array.linkedin);

        // names of display pictures of every team member
//        images.add(R.drawable.sahil);
//        images.add(R.drawable.tanuj);
//        images.add(R.drawable.shashwat);
//        images.add(R.drawable.vipasha);
//        images.add(R.drawable.gaurav);

        images_url.add("https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/sahil.jpg?alt=media&token=918ba31c-c63c-4422-ab19-73f261dfa4ec");
        images_url.add("https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/tanuj.jpg?alt=media&token=384b51a7-c665-4ad3-96ad-cd580069bfaf");
        images_url.add("https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/shashwat.jpg?alt=media&token=4f879755-6fdf-4320-a1cc-a7c34fd67e40");
        images_url.add("https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/vipasha.jpg?alt=media&token=f8434327-cc88-4f8e-a6b2-004a0f15ad94");
        images_url.add("https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/gaurav.png?alt=media&token=59437c37-11c0-4c38-b5e0-b1ec1157fe62");


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CustomAdapter(names,positions,emails,facebookIDs,linkedinIDs,images_url,getContext());
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
