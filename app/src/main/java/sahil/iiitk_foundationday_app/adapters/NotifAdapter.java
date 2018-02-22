package sahil.iiitk_foundationday_app.adapters;
// Made by tanuj
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sahil.iiitk_foundationday_app.R;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.ViewHolder> {
    private static final String TAG = "NotifAdapter";
    private static List<String> info,when,which;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView club_name,notif_time,notif_details;

        public ViewHolder(View v) {
            super(v);
            club_name = (TextView) v.findViewById(R.id.notif_club_name);
            notif_time = (TextView) v.findViewById(R.id.notif_time);
            notif_details=(TextView) v.findViewById(R.id.notif_info);
        }

        public TextView getClubNameView() {
            return club_name;
        }
        public TextView getTimeView(){
            return notif_time;
        }
        public TextView getInfoView(){
            return notif_details;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param data1 String[] containing the data to populate views to be used by RecyclerView.
     */
    public NotifAdapter(ArrayList<String> data1,ArrayList<String> data2,ArrayList<String> data3){
        info=data1;
        when=data2;
        which=data3;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notif_card, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int pos) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getClubNameView().setText(which.get(pos));
        viewHolder.getTimeView().setText("@ "+when.get(pos));
        viewHolder.getInfoView().setText(info.get(pos));
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return info.size();
    }
}

