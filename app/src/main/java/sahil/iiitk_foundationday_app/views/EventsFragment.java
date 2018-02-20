package sahil.iiitk_foundationday_app.views;
// Made by tanuj
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.adapters.EventAdapter;

public class EventsFragment extends Fragment {

    Integer[] event_images={R.drawable.event1,R.drawable.event2,R.drawable.event1,R.drawable.event2,R.drawable.event1};
    protected RecyclerView mRecyclerView;
    protected EventAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_events, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewEvents);
        mLayoutManager =new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new EventAdapter(event_images);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

}
