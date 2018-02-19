package sahil.iiitk_foundationday_app.adapters;
// Made by tanuj
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.views.ParentForFrag;

public class ClubsAdapter extends RecyclerView.Adapter<ClubsAdapter.ViewHolder> {
    private static final String TAG = "Clubsdapter";
    private static  Context con;

    private static String[] club_names,club_taglines;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView,tagView;

        public ViewHolder(View v) {
            super(v);
            nameView = (TextView) v.findViewById(R.id.club_name);
            tagView = (TextView) v.findViewById(R.id.club_tagline);
            final Intent intent=new Intent(con, ParentForFrag.class);
            v.setOnClickListener(
                    new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            intent.putExtra("club_number",getAdapterPosition());
                            con.startActivity(intent);
                        }
                    }
            );
        }

        public TextView getNameView() {
            return nameView;
        }
        public TextView getTagView(){
            return tagView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param data1 String[] containing the data to populate views to be used by RecyclerView.
     */
    public ClubsAdapter(Context context, String[] data1, String[] data2) {
        club_names = data1;
        club_taglines=data2;
        con=context;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.clubs_card, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int pos) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getNameView().setText(club_names[pos]);
        viewHolder.getTagView().setText(club_taglines[pos]);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return club_names.length;
    }
}

