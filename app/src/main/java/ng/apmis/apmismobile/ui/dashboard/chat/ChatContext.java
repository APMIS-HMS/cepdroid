package ng.apmis.apmismobile.ui.dashboard.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ng.apmis.apmismobile.R;

/**
 * Created by Thadeus-APMIS on 6/7/2018.
 */

public class ChatContext extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.chat_layout, container, false);

        RecyclerView chatMessages = rootView.findViewById(R.id.chat_recycler);

        return rootView;
    }
}
