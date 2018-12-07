package ng.apmis.apmismobile.ui.dashboard.find.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SearchTermItem;

public class SearchTermsItemAdapter extends RecyclerView.Adapter<SearchTermsItemAdapter.SearchTermsItemViewHolder> {

    private Context context;
    private List<SearchTermItem> searchTermsItems;

    private OnPreviousItemSelectedListener mListener;

    public interface OnPreviousItemSelectedListener{
        void onPreviousItemSelected(String itemId, String itemName, String itemType);
    }

    public SearchTermsItemAdapter(Context context, @NonNull List<SearchTermItem> searchTermsItems) {
        this.context = context;
        this.searchTermsItems = searchTermsItems;
    }

    @NonNull
    @Override
    public SearchTermsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_previous_item, parent, false);

        return new SearchTermsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTermsItemViewHolder holder, int position) {
        SearchTermItem item = searchTermsItems.get(position);

        holder.titleText.setText(item.getTitle());

        if (item.getType().equals("Hospital"))
            holder.itemImage.setImageResource(R.drawable.ic_default_hospital);
        else if (item.getType().equals("Doctor"))
            holder.itemImage.setImageResource(R.drawable.ic_default_profile);


        holder.subTitleText.setText(item.getSubTitle());
        Glide.with(context)
            .load(item.getImageURL())
            .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    try {
                        holder.imageLoader.setVisibility(View.GONE);
                    } catch (Exception ignored) {}

                    if (item.getType().equals("Hospital"))
                        target.onLoadCleared(context.getResources().getDrawable(R.drawable.ic_default_hospital));
                    else if (item.getType().equals("Doctor"))
                        target.onLoadCleared(context.getResources().getDrawable(R.drawable.ic_default_profile));
                    return true; //prevent onLoadFailed from being called on the target
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.imageLoader.setVisibility(View.GONE);
                    return false;
                }
            })
            .into(holder.itemImage);

        holder.previousItem.setOnClickListener(view -> {
            mListener.onPreviousItemSelected(item.getId(), item.getTitle(), item.getType());
        });
    }

    public void instantiatePreviousItemSelectedListener(OnPreviousItemSelectedListener listener){
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return searchTermsItems==null ? 0 : searchTermsItems.size();
    }

    class SearchTermsItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.previous_item_card)
        CardView previousItem;

        @BindView(R.id.item_image)
        ImageView itemImage;

        @BindView(R.id.image_loader)
        ProgressBar imageLoader;

        @BindView(R.id.title_text)
        TextView titleText;

        @BindView(R.id.subtitle_text)
        TextView subTitleText;

        SearchTermsItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
