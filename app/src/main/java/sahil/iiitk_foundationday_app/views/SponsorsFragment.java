package sahil.iiitk_foundationday_app.views;
// Made by tanuj
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.adapters.Sponsor_adapter;

public class SponsorsFragment extends Fragment {

    String[] sponsor_names;
    Integer[] sponsor_images={R.drawable.sponsor,R.drawable.sponsor,R.drawable.sponsor,R.drawable.sponsor,R.drawable.sponsor};
    protected RecyclerView mRecyclerView;
    protected Sponsor_adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_sponsors, container, false);

         sponsor_names=getResources().getStringArray(R.array.sponsor_names);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSponsors);
        mLayoutManager =new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new Sponsor_adapter(sponsor_names,sponsor_images);
        mRecyclerView.setAdapter(mAdapter);

     return  view;
    }

}
