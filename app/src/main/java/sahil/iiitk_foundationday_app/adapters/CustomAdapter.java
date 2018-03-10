package sahil.iiitk_foundationday_app.adapters;
// Made by tanuj
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sahil.iiitk_foundationday_app.R;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private static String[] names,position,emails,facebookIDs,linkedinIDs;
    List<Integer> images;
    private static ImageView dp;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView,positionView;

        public ViewHolder(View v) {
            super(v);
            nameView = (TextView) v.findViewById(R.id.mem_name);
            positionView=(TextView)v.findViewById(R.id.mem_pos);
            ImageView mem_email=(ImageView) v.findViewById(R.id.mem_mail);
            dp=v.findViewById(R.id.mem_image);
            mem_email.setOnClickListener(
                    new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Log.d("card","Mail button: "+getAdapterPosition());
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto",emails[getAdapterPosition()], null));
                            v.getContext().startActivity(Intent.createChooser(emailIntent, "Send Email via"));
                        }
                    }
            );
            ImageView mem_fb=(ImageView)v.findViewById(R.id.mem_fb);
            mem_fb.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("card","Facebook button: "+getAdapterPosition());
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(facebookIDs[getAdapterPosition()]));
                            v.getContext().startActivity(i);
                        }
                    }
            );
            ImageView mem_linkedin=(ImageView)v.findViewById(R.id.mem_linkedin);
            mem_linkedin.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("card","Linked in button: "+getAdapterPosition());
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(linkedinIDs[getAdapterPosition()]));
                            v.getContext().startActivity(i);
                        }
                    }
            );
        }

        public TextView getNameView() {
            return nameView;
        }
        public TextView getPositionView(){
            return positionView;
        }
        public ImageView getDPView(){
            return dp;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param data1 String[] containing the data to populate views to be used by RecyclerView.
     */
    public CustomAdapter(String[] data1,String[] data2,String[] data3,String[] data4,String[] data5,List<Integer> data6) {
        names = data1;
        position=data2;
        emails=data3;
        facebookIDs=data4;
        linkedinIDs=data5;
        images=data6;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int pos) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getNameView().setText(names[pos]);
        viewHolder.getPositionView().setText(position[pos]);
        viewHolder.getDPView().setImageResource(images.get(pos));
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return names.length;
    }
}
