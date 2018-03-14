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
    int club_number;
    Bundle bundle;

//square images for every event of every club
    Integer[] event_images,
            a={R.drawable.sherlocked,R.drawable.enigma,R.drawable.codecanon,R.drawable.warmanga,R.drawable.connectthedots,R.drawable.cryptic},
            b={R.drawable.melomania,R.drawable.dhun,R.drawable.flyingsolo,R.drawable.algorhythm,R.drawable.connecttoculture,R.drawable.informals},
            c={R.drawable.spellbee,R.drawable.talesmith,R.drawable.madads,R.drawable.yatharth,R.drawable.tellatale},
            d={R.drawable.halloween,R.drawable.tattoo,R.drawable.memeslay,R.drawable.campus,R.drawable.vine,R.drawable.origami};
    protected RecyclerView mRecyclerView;
    protected EventAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bundle=getArguments();
        club_number=bundle.getInt("club_number");

        switch (club_number){
            case 0: event_images=a;
            break;
            case 1:event_images=b;
            break;
            case 2: event_images=c;
            break;
            case 3: event_images=d;
            break;
            default: event_images=a;
        }

        View view= inflater.inflate(R.layout.fragment_events, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewEvents);
        mLayoutManager =new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new EventAdapter(getActivity(),club_number,event_images);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

}
