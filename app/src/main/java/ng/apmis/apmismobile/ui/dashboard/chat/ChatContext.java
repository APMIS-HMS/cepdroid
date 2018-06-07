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

import java.util.ArrayList;

import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.chatModel.Chats;

/**
 * Created by Thadeus-APMIS on 6/7/2018.
 */

public class ChatContext extends Fragment {

    ChatContextAdapter chatContextAdapter;

    ArrayList<Chats> existingChats = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.chat_layout, container, false);

        chatContextAdapter = new ChatContextAdapter(getActivity());

        existingChats.add(new Chats("akjsljfalsdjf;laksdfl;ajskljdf;alsjd;flakjsd;fjas;ldjf;lasjkflkjasdljfa;sjdflajksdfljasl;jkdfasjl;kdfjas;lkjdflajksd;fjasljfdals;jf;ajsdfjas;jd;fjasdjf;asdf", "me", R.drawable.ic_buy));
        existingChats.add(new Chats("This is Ikeja", "Other", R.drawable.ic_diagnostic_report));
        existingChats.add(new Chats("This is Agege", "me", R.drawable.ic_pay_bills));
        existingChats.add(new Chats("This is London", "Other", R.drawable.ic_find));
        existingChats.add(new Chats("This is Lagos", "me", R.drawable.ic_buy));
        existingChats.add(new Chats("This is Fausets", "Other", R.drawable.ic_buy));

        RecyclerView chatMessageRecycler = rootView.findViewById(R.id.chat_recycler);

        chatMessageRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        chatMessageRecycler.setAdapter(chatContextAdapter);

        chatContextAdapter.setAllChats(existingChats);

        return rootView;
    }
}
