package ng.apmis.apmismobile.ui.dashboard.find.foundItems;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.ui.dashboard.find.foundItems.foundHospital.FoundHospitalDetailFragment;
import ng.apmis.apmismobile.utilities.AppUtils;

public class FoundItemsActivity extends AppCompatActivity implements FoundItemsListFragment.OnFragmentInteractionListener,
        FoundHospitalDetailFragment.OnFragmentInteractionListener {

    @BindView(R.id.general_toolbar)
    Toolbar generalToolbar;

    @BindView(R.id.action_bar_title)
    TextView toolbarTitle;

    String searchQuery;
    String searchExtra;

    ActionBar actionBar;

    FoundItemsListFragment foundItemsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_items);
        ButterKnife.bind(this);

        setSupportActionBar(generalToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        searchExtra = intent.getStringExtra("SearchTerm");

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchQuery = intent.getStringExtra(SearchManager.QUERY);
            Log.e("Search query", searchQuery);

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // Handle a suggestions click (because the suggestions all use ACTION_VIEW)
            searchQuery = intent.getStringExtra("title");
        }

        //Set toolbar title to search term item
        toolbarTitle.setText(formatToPlural(searchExtra).toUpperCase());

        foundItemsListFragment = new FoundItemsListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, foundItemsListFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }


    public void setToolBarTitle(String title) {
        toolbarTitle.setText(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private String formatToPlural(String singular){
        if (singular.endsWith("y")) {
            StringBuffer buffer = new StringBuffer(singular);
            buffer.deleteCharAt(singular.length() - 1);
            buffer.append("ies");
            singular = buffer.toString();
        } else if (singular.endsWith("s")){

        } else {
            singular = singular+"s";
        }

        return singular;
    }

    @Override
    public void onViewIdActionPerformed(String id, String name) {
        if (searchExtra.equals("Hospital")){
            placeFragment(FoundHospitalDetailFragment.newInstance(id, name));
        }
    }

    /**
     * Replace the current fragment with another
     * @param fragment Fragment used to replace
     */
    private void placeFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack("current", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                //.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("current")
                .commit();
    }

    @Override
    public void onPayClicked(String facilityId) {

    }
}
