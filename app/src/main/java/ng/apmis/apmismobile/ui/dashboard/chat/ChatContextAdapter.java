package ng.apmis.apmismobile.ui.dashboard.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ng.apmis.apmismobile.R;

/**
 * Created by Thadeus-APMIS on 6/7/2018.
 */

public class ChatContextAdapter extends RecyclerView.Adapter<ChatContextAdapter.ChatContextViewHolder> {

    ArrayList<String> allChats = new ArrayList<>();
    Context mContext;

    ChatContextAdapter (Context context) {
        mContext = context;
    }

    public void setAllChats (ArrayList<String> allChats) {
        this.allChats = allChats;
        notifyDataSetChanged();
    }

    public void addChats (ArrayList<String> newMessages) {
        this.allChats.addAll(newMessages);
        notifyDataSetChanged();
    }


    @Override
    public ChatContextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_other, parent, false);

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatContextViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ChatContextViewHolder extends RecyclerView.ViewHolder {

        public ChatContextViewHolder(View itemView) {
            super(itemView);
        }
    }
}
