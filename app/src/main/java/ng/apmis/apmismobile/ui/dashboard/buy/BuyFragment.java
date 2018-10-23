package ng.apmis.apmismobile.ui.dashboard.buy;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.ui.dashboard.ModuleListModel;

public class BuyFragment extends android.support.v4.app.Fragment {


    private static final String CLASSNAME = BuyFragment.class.getSimpleName();
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    List<ModuleListModel> optionItems = new ArrayList<>();

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buy, container, false);
        ButterKnife.bind(this, rootView);

        /*
        * Set elevation on AppbarLayout
        * */
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            ViewCompat.setElevation(appBarLayout, 8);
        });

        ViewPager viewPager = rootView.findViewById(R.id.view_pager);
        BuyCategoryAdapter adapter = new BuyCategoryAdapter(getActivity(), getChildFragmentManager());

        TabLayout tabLayout = rootView.findViewById(R.id.tabview);
        tabLayout.setupWithViewPager(viewPager);
       /* tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (getString(R.string.pregJournal).equals(tab.getText()))
                    ((DashboardActivity)getActivity()).fabVisibility(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (getString(R.string.pregJournal).equals(tab.getText()))
                    ((DashboardActivity)getActivity()).fabVisibility(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
       tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager.setAdapter(adapter);



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
