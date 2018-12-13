package ng.apmis.apmismobile.ui.dashboard.find.foundItems;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class FoundItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<SearchTermItem> foundItems = null;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_LOADER = 1;

    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private OnViewClickedListener mListener;
    private OnForceReloadListener forceReloadListener;

    public interface OnViewClickedListener {
        void onViewClicked(String id, String name, String subName);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnForceReloadListener{
        void onForceReload();
    }

    public FoundItemsAdapter(Context context){
        this.context = context;
        foundItems = new ArrayList<>();
    }

    public void createFoundItems(List<SearchTermItem> items){
        this.foundItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public void addFoundItems(List<SearchTermItem> items){
        this.foundItems.addAll(items);
        notifyDataSetChanged();
    }

    public void instantiateOnViewClickedListener(OnViewClickedListener listener){
        this.mListener = listener;
    }

    public void instantiateOnForceReloadListener(OnForceReloadListener listener){
        this.forceReloadListener = listener;
    }

    public void add(SearchTermItem item){
        this.foundItems.add(item);
        notifyItemInserted(getItemCount()-1);
    }

    public void remove(int position){
        this.foundItems.remove(position);
        notifyItemRemoved(getItemCount());
    }

    public void clear(){
        foundItems.clear();
        notifyDataSetChanged();
    }

    private int infiniteScrollStatus;

    public void showNullLoader(){
        infiniteScrollStatus = 1;
        notifyItemChanged(getItemCount()-1);
    }

    public void removeNullLoader(){
        infiniteScrollStatus = 0;
        notifyItemChanged(getItemCount()-1);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if(!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)){
                        if(onLoadMoreListener != null){
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;

        switch (viewType){
            case TYPE_ITEM:
                view = inflater.inflate(R.layout.layout_found_item, parent, false);
                return new FoundItemViewHolder(view);
            case TYPE_LOADER:
                view = inflater.inflate(R.layout.list_progress_item, parent, false);
                return new ProgressViewHolder(view);
            default:
                view = inflater.inflate(R.layout.layout_found_item, parent, false);
                return new FoundItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchTermItem foundItem = foundItems.get(position);

        if (holder instanceof FoundItemViewHolder) {

            ((FoundItemViewHolder)holder).foundTitle.setText(foundItem.getTitle());
            ((FoundItemViewHolder)holder).foundSubTitle.setText(foundItem.getSubTitle());

            ((FoundItemViewHolder)holder).viewButton.setOnClickListener(v -> {
                //Change fragments with the id
                mListener.onViewClicked(foundItem.getId(), foundItem.getTitle(), foundItem.getSubTitle());
            });

            switch (foundItem.getType()) {
                case "Hospital":
                    ((FoundItemViewHolder) holder).foundImage.setImageResource(R.drawable.ic_default_hospital);
                    break;
                case "Doctor":
                    ((FoundItemViewHolder) holder).foundImage.setImageResource(R.drawable.ic_default_profile);
                    break;
                case "Nurse":
                    ((FoundItemViewHolder) holder).foundImage.setImageResource(R.drawable.ic_default_profile);
                    break;
            }

            Glide.with(((FoundItemViewHolder)holder).foundImage)
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
                    }).into(((FoundItemViewHolder)holder).foundImage);

        } else if (holder instanceof ProgressViewHolder){

            if (infiniteScrollStatus == 0) {
                ((ProgressViewHolder) holder).loader.setIndeterminate(true);
                ((ProgressViewHolder) holder).loader.setVisibility(View.VISIBLE);
                ((ProgressViewHolder) holder).refresh.setVisibility(View.GONE);

            } else {
                ((ProgressViewHolder) holder).loader.setVisibility(View.INVISIBLE);
                ((ProgressViewHolder) holder).refresh.setVisibility(View.VISIBLE);

                ((ProgressViewHolder) holder).refresh.setOnClickListener(v -> {
                    forceReloadListener.onForceReload();
                    ((ProgressViewHolder) holder).refresh.setVisibility(View.GONE);
                    ((ProgressViewHolder) holder).loader.setIndeterminate(true);
                    ((ProgressViewHolder) holder).loader.setVisibility(View.VISIBLE);
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return foundItems == null ? 0 : foundItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return foundItems.get(position) != null ? TYPE_ITEM : TYPE_LOADER;
    }

    public void instantiateOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    public void setLoading() {
        loading = true;
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

    class ProgressViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.progress_loader)
        ProgressBar loader;

        @BindView(R.id.refresh_button)
        ImageView refresh;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
