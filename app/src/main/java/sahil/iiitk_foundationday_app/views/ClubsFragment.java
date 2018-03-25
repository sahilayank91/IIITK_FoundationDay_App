package sahil.iiitk_foundationday_app.views;
// Made by tanuj
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.adapters.ClubsAdapter;

public class ClubsFragment extends Fragment {

    String[] club_names,club_taglines;
//    List<Integer> images=new ArrayList<>();
    List<String> images_url=new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected ClubsAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_clubs, container, false);
//        images.add(R.drawable.club_technical);
//        images.add(R.drawable.club_cultural);
//        images.add(R.drawable.club_literray);
//        images.add(R.drawable.club_photography);

        images_url.add("https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/clubs%2Fclub_technical.jpg?alt=media&token=28d8c109-1ce8-4902-9bd9-881b523dd3c1");
        images_url.add("https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/clubs%2Fclub_cultural.jpg?alt=media&token=1bd6ad1d-c39e-442a-9fc8-059fce02ca3b");
        images_url.add("https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/clubs%2Fclub_literray.jpeg?alt=media&token=1cde0dec-f58e-4509-b2cc-80962e4c6f33");
        images_url.add("https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/clubs%2Fclub_photography.jpg?alt=media&token=6a0a6b26-7f4b-4d59-a447-449ee3507c7e");


club_names=getResources().getStringArray(R.array.club_names);
club_taglines=getResources().getStringArray(R.array.club_taglines);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewClubs);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ClubsAdapter(getActivity(),club_names,club_taglines,images_url);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

}
