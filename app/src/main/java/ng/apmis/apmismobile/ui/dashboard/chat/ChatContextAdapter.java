package ng.apmis.apmismobile.ui.dashboard.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.chatModel.Chats;

/**
 * Created by Thadeus-APMIS on 6/7/2018.
 */

public class ChatContextAdapter extends RecyclerView.Adapter<ChatContextAdapter.ChatContextViewHolder> {

    ArrayList<Chats> allChats = new ArrayList<>();
    Context mContext;

    ChatContextAdapter (Context context) {
        mContext = context;
    }

    public void setAllChats (ArrayList<Chats> allChats) {
        this.allChats = allChats;
        notifyDataSetChanged();
    }

    public void addChats (ArrayList<Chats> newMessages) {
        this.allChats.addAll(newMessages);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ChatContextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_owner, parent, false);
                return new ChatContextViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_other, parent, false);
                return new ChatContextViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (allChats.get(position).getUserName().equals("me")) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatContextViewHolder holder, int position) {
        Chats currentChat = allChats.get(position);
        if (currentChat != null) {
            holder.chatText.setText(currentChat.getChatMessage());
            holder.userImage.setImageResource(currentChat.getUserImage());
        }
    }

    @Override
    public int getItemCount() {
        return allChats.size();
    }

    class ChatContextViewHolder extends RecyclerView.ViewHolder {

        TextView chatText;
        ImageView userImage;

        ChatContextViewHolder(View itemView) {
            super(itemView);
            chatText = (TextView) itemView.findViewById(R.id.message_text);
            userImage = (ImageView) itemView.findViewById(R.id.user_image);

        }
    }
}
