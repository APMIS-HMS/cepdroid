package ng.apmis.apmismobile.ui.dashboard.find;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.ui.dashboard.find.adapters.SearchTermsRowAdapter;
import ng.apmis.apmismobile.ui.dashboard.find.foundItems.FoundItemsActivity;
import ng.apmis.apmismobile.utilities.Constants;
import ng.apmis.apmismobile.utilities.InjectorUtils;

public class FindFragment extends Fragment implements SearchTermsRowAdapter.OnViewAllTermsClickedListener{

    @BindView(R.id.search_term_recycler)
    RecyclerView searchTermRecycler;

    @BindView(R.id.find_spinner)
    Spinner findSpinner;

    @BindView(R.id.find_bar)
    SearchView findSearchView;

    @BindView(R.id.search_card)
    CardView searchCard;

    private SimpleCursorAdapter mAdapter;

    private SharedPreferencesManager prefs;

    private PreviousItemsViewModel previousItemsViewModel;

    private SearchTermsRowAdapter searchTermsRowAdapter;

    List<String> searchTerms = new ArrayList<>();

    private List<String> terms = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new SharedPreferencesManager(getContext());

        terms.add("Light hospital");
        terms.add("Mighty hospital");
        terms.add("Lagos hospital");
        terms.add("Delta hospital");
        terms.add("Mofe's hospital");
        terms.add("Apmis hospital");
        terms.add("Tomal hospital");
        terms.add("Compty hospital");
        terms.add("Voice of man hospital");
        terms.add("Great hospital");
        terms.add("Hoff hospital");
        terms.add("Koline hospital");
        terms.add("Nuance hospital");
        terms.add("Boys hospital");
        terms.add("Zed hospital");
        terms.add("Opium hospital");
        terms.add("Vape society hospital");
        terms.add("Popcorn lung hospital");
        terms.add("Oncology hospital");
        terms.add("Oncology II hospital");
        terms.add("Oncology III hospital");
        terms.add("Oncology IV hospital");
        terms.add("Oncology V hospital");


        View rootView = inflater.inflate(R.layout.fragment_find, container, false);
        ButterKnife.bind(this, rootView);

        searchTermRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));

        searchTerms = Arrays.asList(getActivity().getResources().getStringArray(R.array.filter));

        searchTermsRowAdapter = new SearchTermsRowAdapter(getContext(), searchTerms);
        searchTermsRowAdapter.instantiateOnViewAllClickedListener(this);

        searchTermRecycler.setAdapter(searchTermsRowAdapter);

        final String[] from = new String[] {"title"};
        final int[] to = new int[] {R.id.item_search_name};
        mAdapter = new SimpleCursorAdapter(getContext(), R.layout.search_suggest_list_item,
                null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        // Get the SearchView and set the searchable configuration
        final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        findSearchView.setSuggestionsAdapter(mAdapter);
        findSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        final SearchView.SearchAutoComplete searchAutoComplete = findSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        final View dropDownAnchor = findSearchView.findViewById(searchAutoComplete.getDropDownAnchor());
        if (dropDownAnchor != null) {
            dropDownAnchor.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {

                // calculate width of DropdownView
                int point[] = new int[2];
                //Get the search view's x and y coordinates
                findSearchView.getLocationOnScreen(point);
                dropDownAnchor.setX(findSearchView.getX());

                // x coordinate of DropDownView
                int dropDownPadding = point[0] + searchAutoComplete.getDropDownHorizontalOffset();

                Rect screenSize = new Rect();
                getActivity().getWindowManager().getDefaultDisplay().getRectSize(screenSize);
                // screen width
                int screenWidth = screenSize.width();

                // set DropDownView width
                searchAutoComplete.setDropDownWidth(screenWidth - dropDownPadding * 2);
            });
        }

        // Getting selected (clicked) item suggestion
        findSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                // Your code here
                Intent i = new Intent(getActivity(), FoundItemsActivity.class);
                i.putExtra("SearchTerm", searchTerms.get(findSpinner.getSelectedItemPosition()));
                i.setAction(Intent.ACTION_VIEW);

                mAdapter.getCursor().moveToPosition(position);
                String jg = mAdapter.getCursor().getString(1);

                i.putExtra("title", jg);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Apply activity transition
                    startActivity(i, ActivityOptions.makeSceneTransitionAnimation(
                            getActivity(), searchCard, "search_card").toBundle());
                } else {
                    startActivity(i);
                }
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                return true;
            }
        });

        findSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return true;
            }
        });

        findSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get the clinic id of the work by reducing position by 1
                ((DashboardActivity) getActivity()).findSearchTerm = searchTerms.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Getting selected (clicked) item suggestion
//        findSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
//            @Override
//            public boolean onSuggestionClick(int position) {
//                // Your code here
//                Intent i = new Intent(MainActivity.this, SearchPollActivity.class);
//                i.setAction(Intent.ACTION_VIEW);
//
//                mAdapter.getCursor().moveToPosition(position);
//                String jg = mAdapter.getCursor().getString(1);
//
//                i.putExtra("title", jg);
//                startActivity(i);
//                return true;
//            }
//
//            @Override
//            public boolean onSuggestionSelect(int position) {
//                // Your code here
//                return true;
//            }
//        });

        initViewModel();

        return rootView;
    }

    private void initViewModel() {
        PreviousItemsViewModelFactory previousItemsViewModelFactory = InjectorUtils.providePreviousItemsViewModelFactory(getContext().getApplicationContext());
        previousItemsViewModel = ViewModelProviders.of(this, previousItemsViewModelFactory).get(PreviousItemsViewModel.class);

        final Observer<List<Appointment>> appointmentObserver = appointments -> {

            if (appointments != null && appointments.size() > 0) {
                searchTermsRowAdapter.notifySubLists(appointments);
            } else {
                searchTermsRowAdapter.notifySubLists(new ArrayList<>());
            }
        };

        //Observe the Appointments
        previousItemsViewModel.getPersonAppointments(prefs.getPersonId()).observe(this, appointmentObserver);
    }

    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "title" });
        for (int i=0; i<terms.size(); i++) {
            if (terms.get(i).toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[] {i, terms.get(i)});
        }
        mAdapter.changeCursor(c);
        Log.e("Query", query);
    }

    @Override
    public void onPause() {
        //reset selection on pause
        findSearchView.setQuery("", false);
        findSpinner.setSelection(0);
        super.onPause();
    }

    @Override
    public void onResume() {
        if (getActivity() != null) {
            ((DashboardActivity)getActivity()).setToolBarTitleAndBottomNavVisibility(Constants.SEARCH, true);
            ((DashboardActivity)getActivity()).mBottomNav.getMenu().findItem(R.id.find_menu).setChecked(true);
        }
        super.onResume();
    }

    @Override
    public void onViewAllTermsClicked(String searchTerm) {
        Intent i = new Intent(getActivity(), FoundItemsActivity.class);
        i.putExtra("SearchTerm", searchTerm);
        i.setAction(Intent.ACTION_VIEW);

        i.putExtra("title", "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
            startActivity(i, ActivityOptions.makeSceneTransitionAnimation(
                    getActivity(), searchCard, "search_card").toBundle());
        } else {
            startActivity(i);
        }
    }
}
