package ng.apmis.apmismobile.ui.dashboard.find.foundItems;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SearchTermItem;
import ng.apmis.apmismobile.ui.dashboard.profile.ProfileActivity;

public class FoundItemsAdapter extends RecyclerView.Adapter<FoundItemsAdapter.FoundItemViewHolder> {

    private Context context;
    private List<SearchTermItem> foundItems = null;

    private OnViewClickedListener mListener;

    public interface OnViewClickedListener {
        void onViewClicked(String id, String name);
    }

    public FoundItemsAdapter(Context context){
        this.context = context;
        foundItems = new ArrayList<>();
    }

    public void createFoundItems(List<SearchTermItem> items){
        this.foundItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public void instantiateOnViewClickedListener(OnViewClickedListener listener){
        this.mListener = listener;
    }

    public void clear(){
        foundItems.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoundItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_found_item, parent, false);

        return new FoundItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoundItemViewHolder holder, int position) {
        SearchTermItem foundItem = foundItems.get(position);

        holder.foundTitle.setText(foundItem.getTitle());
        holder.foundSubTitle.setText(foundItem.getSubTitle());

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Change fragments with the id
                mListener.onViewClicked(foundItem.getId(), foundItem.getTitle());
            }
        });

        if (foundItem.getType().equals("Hospital"))
            holder.foundImage.setImageResource(R.drawable.ic_default_hospital);
        else if (foundItem.getType().equals("Doctor"))
            holder.foundImage.setImageResource(R.drawable.ic_default_profile);
        else if (foundItem.getType().equals("Nurse"))
            holder.foundImage.setImageResource(R.drawable.ic_default_profile);

        Glide.with(holder.foundImage)
                .load(foundItem.getImageURL())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        try {
//                            holder.imageLoader.setVisibility(View.GONE);
//                        } catch (Exception ignored) {}
                        if (foundItem.getType().equals("Hospital"))
                            target.onLoadCleared(context.getResources().getDrawable(R.drawable.ic_default_hospital));
                        else if (foundItem.getType().equals("Doctor"))
                            target.onLoadCleared(context.getResources().getDrawable(R.drawable.ic_default_profile));
                        else if (foundItem.getType().equals("Nurse"))
                            target.onLoadCleared(context.getResources().getDrawable(R.drawable.ic_default_profile));
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //holder.imageLoader.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.foundImage);
    }

    @Override
    public int getItemCount() {
        return foundItems == null ? 0 : foundItems.size();
    }

    class FoundItemViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.found_title)
        TextView foundTitle;

        @BindView(R.id.found_sub_title)
        TextView foundSubTitle;

        @BindView(R.id.found_image)
        ImageView foundImage;

        @BindView(R.id.view_button)
        Button viewButton;

        public FoundItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
