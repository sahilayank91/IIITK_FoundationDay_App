package sahil.iiitk_foundationday_app.adapters;
// Made by tanuj
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.views.DetailedEvent;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private static final String TAG = "EventAdapter";
    private static Context con;
    private static int club_number;

    private static Integer[] event_images;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            imageView=(ImageView) v.findViewById(R.id.event_image);
            final Intent intent=new Intent(con, DetailedEvent.class);
            v.setOnClickListener(
                    new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            intent.putExtra("club_number",club_number);
                            intent.putExtra("event_number",getAdapterPosition());
                            con.startActivity(intent);
                        }
                    }
            );
        }

        public ImageView getImageView(){
            return imageView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param data String[] containing the data to populate views to be used by RecyclerView.
     */
    public EventAdapter(Context context,int club_num,Integer[] data) {
        event_images=data;
        con=context;
        club_number=club_num;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.events_card, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int pos) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getImageView().setImageResource(event_images[pos]);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (event_images ==null)
            return 0;
        else
        return event_images.length;
    }
}
