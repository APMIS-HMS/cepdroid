package ng.apmis.apmismobile.ui.dashboard.find.foundItems;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
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

public class FoundItemsActivity extends AppCompatActivity implements FoundItemsListFragment.OnFragmentInteractionListener,
        FoundHospitalDetailFragment.OnFragmentInteractionListener {

    @BindView(R.id.general_toolbar)
    Toolbar generalToolbar;

    @BindView(R.id.action_bar_title)
    TextView toolbarTitle;

    private String searchQuery;

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    private String searchTerm;

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

        //If an intent
        if (intent.hasExtra("itemId")){
            String id = intent.getStringExtra("itemId");
            String name = intent.getStringExtra("itemName");
            String type = intent.getStringExtra("itemType");

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, FoundHospitalDetailFragment.newInstance(id, name, null))
                    //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            return;
        }



        searchTerm = intent.getStringExtra("SearchTerm");

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchQuery = intent.getStringExtra(SearchManager.QUERY);
            Log.e("Search query", searchQuery);

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // Handle a suggestions click (because the suggestions all use ACTION_VIEW)
            searchQuery = intent.getStringExtra("title");
        }

        //Set toolbar title to search term item
        toolbarTitle.setText(formatToPlural(searchTerm));

        foundItemsListFragment = new FoundItemsListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, foundItemsListFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
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

    public String formatToPlural(String singular){
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
    public void onViewIdActionPerformed(String id, String name, String subName) {
        if (searchTerm.equals("Hospital")){
            placeFragment(FoundHospitalDetailFragment.newInstance(id, name, subName));
        }
    }

    /**
     * Replace the current fragment with another
     * @param fragment Fragment used to replace
     */
    private void placeFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack("current", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_slide_in, R.anim.fragment_slide_out,
                        R.anim.fragment_pop_slide_in, R.anim.fragment_pop_slide_out)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("current")
                .commit();
    }

    @Override
    public void onPayClicked(String facilityId) {

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                super.finishAfterTransition();
            } else {
                super.finish();
            }
        else
            super.onBackPressed();
    }
}
