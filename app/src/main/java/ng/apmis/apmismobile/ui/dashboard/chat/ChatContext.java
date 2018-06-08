package ng.apmis.apmismobile.ui.dashboard.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.chatModel.Chats;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;

/**
 * Created by Thadeus-APMIS on 6/7/2018.
 */

public class ChatContext extends Fragment {

    ChatContextAdapter chatContextAdapter;

    ArrayList<Chats> existingChats = new ArrayList<>();

    private static String CLASSNAME = "APMIS CEP";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.chat_layout, container, false);

        chatContextAdapter = new ChatContextAdapter(getActivity());

        existingChats.add(new Chats("Hi Thadeus.\nWhat do you want today?", "Other", R.drawable.ic_buy, new Date().getTime()));
        existingChats.add(new Chats("I feel feverish and have a runny nose.\nWhat could be wrong with me?", "me", R.drawable.ic_diagnostic_report, new Date().getTime()));
        existingChats.add(new Chats("Sorry about that...", "Other", R.drawable.ic_find, new Date().getTime()));
        existingChats.add(new Chats("Are your visions blurry?", "Other", R.drawable.ic_buy, new Date().getTime()));
        existingChats.add(new Chats("Yes", "me", R.drawable.ic_buy, new Date().getTime()));

        RecyclerView chatMessageRecycler = rootView.findViewById(R.id.chat_recycler);

        EditText chatMessageEditText = rootView.findViewById(R.id.add_chat);

        ImageView sendBtn = rootView.findViewById(R.id.send_chat);

        chatMessageRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        chatMessageRecycler.setAdapter(chatContextAdapter);

        chatContextAdapter.setAllChats(existingChats);

        chatMessageRecycler.smoothScrollToPosition(existingChats.size());

        sendBtn.setOnClickListener((view) -> {
            Chats oneChat = new Chats(chatMessageEditText.getText().toString(),"me", R.drawable.ic_buy, new Date().getTime());
            ArrayList<Chats> chats = new ArrayList<>();
            chats.add(oneChat);
            chatContextAdapter.addChats(chats);
            chatMessageRecycler.smoothScrollToPosition(chatContextAdapter.getItemCount());
            chatMessageEditText.setText("");
        });

        return rootView;
    }

    @Override
    public void onResume() {
        if (getActivity() != null) {
            ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME, false);
        }
        super.onResume();
    }
}
