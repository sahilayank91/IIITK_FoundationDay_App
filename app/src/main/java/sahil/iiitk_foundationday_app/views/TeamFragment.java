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
    List<Integer> images=new ArrayList<>();
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
        images.add(R.drawable.sahil);
        images.add(R.drawable.tanuj);
        images.add(R.drawable.shashwat);
        images.add(R.drawable.vipasha);
        images.add(R.drawable.gaurav);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CustomAdapter(names,positions,emails,facebookIDs,linkedinIDs,images);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
