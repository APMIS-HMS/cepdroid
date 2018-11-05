package ng.apmis.apmismobile.ui.dashboard.find.foundItems;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SearchTermItem;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

public class FoundItemsActivity extends AppCompatActivity implements FoundItemsListFragment.OnFragmentInteractionListener {

    @BindView(R.id.general_toolbar)
    Toolbar generalToolbar;

    @BindView(R.id.action_bar_title)
    TextView toolbarTitle;

    String searchQuery;
    String searchExtra;

    FoundItemsListFragment foundItemsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_items);
        ButterKnife.bind(this);

        setSupportActionBar(generalToolbar);
        ActionBar actionBar = getSupportActionBar();
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
    public void onIdActionPerformed(String id) {
        AppUtils.showShortToast(this, "Clicked "+id);
    }

    /**
     * Replace the current fragment with another
     * @param fragment Fragment used to replace
     */
    private void placeFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack("current", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("current")
                .commit();
    }
}
