package sahil.iiitk_foundationday_app.adapters;
// Made by Tanuj
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import sahil.iiitk_foundationday_app.R;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class Sponsor_adapter extends RecyclerView.Adapter<Sponsor_adapter.ViewHolder> {
    private static final String TAG = "Sponsor_adapter";

    private static String[] sponsor_names;
    private static Integer[] sponsor_images;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            nameView = (TextView) v.findViewById(R.id.sponsor_name);
            imageView=(ImageView) v.findViewById(R.id.sponsor_image);
            nameView.setOnClickListener(
                    new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Log.d("card","Name button: "+getAdapterPosition());
                        }
                    }
            );
        }

        public TextView getNameView() {
            return nameView;
        }
        public ImageView getImageView(){
            return imageView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param data1 String[] containing the data to populate views to be used by RecyclerView.
     */
    public Sponsor_adapter(String[] data1,Integer[] data2) {
        sponsor_names = data1;
        sponsor_images=data2;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sponsor_card, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int pos) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getNameView().setText(sponsor_names[pos]);
        try{
            viewHolder.getImageView().setImageResource(sponsor_images[pos]);
        }catch (OutOfMemoryError e){
            Log.e("image","ImageError: "+e.getMessage());
        }
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return sponsor_names.length;
    }
}
