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
//    Integer[] event_images,
//            a={R.drawable.sherlocked,R.drawable.enigma,R.drawable.codecanon,R.drawable.warmanga,R.drawable.connectthedots,R.drawable.cryptic},
//            b={R.drawable.melomania,R.drawable.dhun,R.drawable.flyingsolo,R.drawable.algorhythm,R.drawable.connecttoculture,R.drawable.informals},
//            c={R.drawable.spellbee,R.drawable.talesmith,R.drawable.madads,R.drawable.yatharth,R.drawable.tellatale},
//            d={R.drawable.halloween,R.drawable.tattoo,R.drawable.memeslay,R.drawable.campus,R.drawable.vine,R.drawable.origami};
    String[] event_images_url,
            a={"https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/technical%2Fsherlocked.jpg?alt=media&token=2c401628-e4ba-4886-b667-1778c6706bef",
                   "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/technical%2Fenigma.jpg?alt=media&token=94e3b4b5-07c7-41cf-b938-3a8a7707f6ed",
                   "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/technical%2Fcodecanon.jpg?alt=media&token=dfac1e85-8589-413f-a33d-8759b87eb065",
                    "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/technical%2Fwarmanga.jpg?alt=media&token=20109566-9787-4f43-bf95-8fdfd3dc9e20",
                    "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/technical%2Fconnectthedots.png?alt=media&token=1e7ec1ea-4c24-43f3-9318-71f9e67321be",
                   "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/technical%2Fcryptic.png?alt=media&token=8bf06dbb-536d-4502-9993-7b26acc7e2c7"},
            b={"https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/cultural%2Fmelomania.jpg?alt=media&token=e76d7f40-c842-48a1-9759-9eeaf9303dcc",
                    "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/cultural%2Fdhun.jpg?alt=media&token=f8f762e9-b7b9-411c-86c3-f4cb3c34c141",
                   "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/cultural%2Fflyingsolo.jpg?alt=media&token=03299ba4-fb5f-4bd3-bd1e-e41591964e4a",
                   "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/cultural%2Falgorhythm.jpg?alt=media&token=a7158ae1-8639-494d-bec5-68deb2443eeb",
                    "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/cultural%2Fconnecttoculture.jpg?alt=media&token=731c1d95-8fe2-4609-9f14-5da1399c3bd5",
                   "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/cultural%2Finformals.jpg?alt=media&token=bb46246e-ee7d-494c-8aef-5b7701a8f655"},
            c={"https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/literary%2Fspellbee.jpg?alt=media&token=43342ced-68dc-4919-a79a-6afc6ddb4aac",
                   "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/literary%2Ftalesmith.jpg?alt=media&token=80895400-7bf5-4854-8285-7cd97baf757b",
                  "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/literary%2Fmadads.jpg?alt=media&token=f2cd882b-2f3f-4ed0-9f3d-b18c2a3017bc",
                   "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/literary%2Fyatharth.jpg?alt=media&token=f52b9abd-9b94-42c2-a1ba-75e901fadecb",
                   "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/literary%2Ftellatale.jpg?alt=media&token=3eefe0c2-843c-4ca0-a023-f7fbd7123f45"},
            d={"https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/photography%2Fhalloween.jpg?alt=media&token=62de4933-1501-448d-971b-1eb078650a93"
                    ,"https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/photography%2Ftattoo.jpg?alt=media&token=3e57bc1b-9497-4b62-bbf6-33fe7409224d",
                    "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/photography%2Fmemeslay.jpg?alt=media&token=ecc3a70e-4b16-4886-a6e6-7c28749ca377",
                   "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/photography%2Fcampus.jpg?alt=media&token=72c97a6d-317d-4bd5-94cd-d93bf12e6d10",
                   "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/photography%2Fvine.jpg?alt=media&token=35e624f5-4a5b-493f-93e7-1cb0ee467747",
                   "https://firebasestorage.googleapis.com/v0/b/iiitkfoundationdayapp.appspot.com/o/photography%2Forigami.jpg?alt=media&token=ece351c5-a0dc-4fe8-be67-8a5364a3495e"};
    protected RecyclerView mRecyclerView;
    protected EventAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bundle=getArguments();
        club_number=bundle.getInt("club_number");

//        switch (club_number){
//            case 0: event_images=a;
//            break;
//            case 1:event_images=b;
//            break;
//            case 2: event_images=c;
//            break;
//            case 3: event_images=d;
//            break;
//            default: event_images=a;
//        }

        switch (club_number){
            case 0: event_images_url=a;
                break;
            case 1:event_images_url=b;
                break;
            case 2: event_images_url=c;
                break;
            case 3: event_images_url=d;
                break;
            default: event_images_url=a;
        }


        View view= inflater.inflate(R.layout.fragment_events, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewEvents);
        mLayoutManager =new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new EventAdapter(getActivity(),club_number,event_images_url);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

}
