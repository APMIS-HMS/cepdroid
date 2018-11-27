package ng.apmis.apmismobile.ui.dashboard;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.utilities.Constants;

import static android.widget.RelativeLayout.BELOW;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.search_box)
    TextInputEditText searchBox;

    @BindView(R.id.first_row)
    TableRow firstRow;

    @BindView(R.id.second_row)
    TableRow secondRow;

    @BindView(R.id.first)
    CardView firstCard;

    @BindView(R.id.second)
    CardView secondCard;

    @BindView(R.id.third)
    CardView thirdCard;

    @BindView(R.id.fourth)
    CardView fourthCard;

    @BindView(R.id.quick_links_group)
    RelativeLayout quickLinksLayout;

    @BindView(R.id.open_close_reminders)
    Button openCloseReminders;

    @BindView(R.id.reminder_recycler)
    RecyclerView reminderRecycler;

    boolean areRemindersShowing;

    OnQuickLinkListener mListener;

    public void initializeQuickLinkListener (OnQuickLinkListener listener) {
        this.mListener = listener;
    }

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, rootView);

        areRemindersShowing = false;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        firstRow.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, screenWidth/2));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, screenWidth/2);
        params.addRule(BELOW, R.id.first_row);
        secondRow.setLayoutParams(params);

        AnimatorSet out1 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.out);
        AnimatorSet out2 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.out);
        AnimatorSet out3 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.out);
        AnimatorSet out4 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.out);
        AnimatorSet out5 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.out);

        out1.setTarget(firstCard);
        out2.setTarget(secondCard);
        out3.setTarget(thirdCard);
        out4.setTarget(fourthCard);
        out5.setTarget(openCloseReminders);

        out1.start();
        out2.start();
        out3.start();
        out4.start();
        out5.start();

        AnimatorSet set1 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.bounce_in);
        set1.setTarget(firstCard);
        set1.setStartDelay(20);
        set1.start();

        AnimatorSet set2 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.bounce_in);
        set2.setTarget(secondCard);
        set2.setStartDelay(120);
        set2.start();

        AnimatorSet set3 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.bounce_in);
        set3.setTarget(thirdCard);
        set3.setStartDelay(220);
        set3.start();

        AnimatorSet set4 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.bounce_in);
        set4.setTarget(fourthCard);
        set4.setStartDelay(320);
        set4.start();


        AnimatorSet floatIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.fade_in_float);
        floatIn.setTarget(openCloseReminders);
        floatIn.setStartDelay(1000);
        floatIn.start();

        AnimatorSet outRecycler = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.out);
        outRecycler.setTarget(reminderRecycler);
        outRecycler.start();

        ReminderAdapter adapter = new ReminderAdapter(getContext());
        reminderRecycler.setAdapter(adapter);
        reminderRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        openCloseReminders.setOnClickListener(v -> {

            if (areRemindersShowing) {
                openCloseReminders.setText("View Reminders");
                areRemindersShowing = false;

                openCloseReminders.setCompoundDrawablesWithIntrinsicBounds(
                        null, null,
                        getContext().getResources().getDrawable(R.drawable.ic_expand_more_black), null);

                AnimatorSet showQuickLinks = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                        R.animator.fade_zoom_in);
                showQuickLinks.setTarget(quickLinksLayout);
                showQuickLinks.setStartDelay(200);
                showQuickLinks.start();

                AnimatorSet removeRecycler = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                        R.animator.top_to_bottom);
                removeRecycler.setTarget(reminderRecycler);
                removeRecycler.start();


            } else {
                openCloseReminders.setText("Close Reminders");
                areRemindersShowing = true;

                openCloseReminders.setCompoundDrawablesWithIntrinsicBounds(
                        null, null,
                        getContext().getResources().getDrawable(R.drawable.ic_close_black), null);

                AnimatorSet removeQuickLinks = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                        R.animator.fade_zoom_out);
                removeQuickLinks.setTarget(quickLinksLayout);
                removeQuickLinks.start();

                AnimatorSet showRecycler = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                        R.animator.bottom_to_top);
                showRecycler.setTarget(reminderRecycler);
                showRecycler.start();
            }


        });

        rootView.findViewById(R.id.appointment).setOnClickListener(this);
        rootView.findViewById(R.id.vitals).setOnClickListener(this);
        rootView.findViewById(R.id.location).setOnClickListener(this);
        rootView.findViewById(R.id.help).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appointment:
                mListener.onQuickLinkClicked(Constants.APPOINTMENTS);
                break;
            case R.id.vitals:
                mListener.onQuickLinkClicked(Constants.VITALS);
                break;
            case R.id.location:
                mListener.onQuickLinkClicked(Constants.LOCATION);
                break;
            case R.id.help:
                mListener.onQuickLinkClicked(Constants.HELP);
                break;
            default:
                break;
        }
    }

    public interface OnQuickLinkListener {
        void onQuickLinkClicked(String link);
    }


    @Override
    public void onResume() {
        if (getActivity() != null) {
            ((DashboardActivity)getActivity()).setToolBarTitleAndBottomNavVisibility(Constants.WELCOME, true);
            ((DashboardActivity)getActivity()).mBottomNav.getMenu().findItem(R.id.home_menu).setChecked(true);
        }
        super.onResume();
    }
}
