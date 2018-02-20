package sahil.iiitk_foundationday_app.views;
// Made by tanuj
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.adapters.ClubsAdapter;

public class ClubsFragment extends Fragment {

    String[] club_names,club_taglines;
    protected RecyclerView mRecyclerView;
    protected ClubsAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_clubs, container, false);

club_names=getResources().getStringArray(R.array.club_names);
club_taglines=getResources().getStringArray(R.array.club_taglines);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewClubs);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ClubsAdapter(getActivity(),club_names,club_taglines);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

}
