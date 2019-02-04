package ng.apmis.apmismobile.ui.dashboard.find.foundItems;


import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.database.MatrixCursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SearchTermItem;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoundItemsListFragment extends Fragment implements FoundItemsAdapter.OnViewClickedListener,
        FoundItemsAdapter.OnLoadMoreListener, FoundItemsAdapter.OnForceReloadListener {

    @BindView(R.id.search_shimmer)
    ShimmerFrameLayout searchShimmer;

    @BindView(R.id.search_recycler)
    RecyclerView searchRecycler;

    @BindView(R.id.find_bar)
    SearchView findSearchView;

    @BindView(R.id.find_spinner)
    Spinner findSpinner;

    @BindView(R.id.search_card)
    CardView searchCard;

    @BindView(R.id.empty_view)
    RelativeLayout searchEmptyView;

    @BindView(R.id.empty_text)
    TextView emptyTextView;

    String selectedSearchTerm;

    private SimpleCursorAdapter mAdapter;

    private List<String> searchTerms = new ArrayList<>();

    private List<String> terms = new ArrayList<>();

    private FoundItemsActivity hostActivity;

    private FoundItemsAdapter foundItemsAdapter;
    FoundItemsViewModel foundItemsViewModel;
    List<SearchTermItem> foundItems = new ArrayList<>();

    private int skipCount = 0;
    private int lastSkipCount;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onViewIdActionPerformed(String id, String name, String subName);
    }

    public FoundItemsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_items, container, false);
        ButterKnife.bind(this, view);

        Log.e("Find", "On create called again");

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

        hostActivity = (FoundItemsActivity) getActivity();

        selectedSearchTerm = hostActivity.getSearchTerm();

        searchTerms = Arrays.asList(hostActivity.getResources().getStringArray(R.array.filter));

        if (savedInstanceState != null) {
            skipCount = savedInstanceState.getInt("skipCount", 0);
            lastSkipCount = savedInstanceState.getInt("lastSkipCount", 0);
        }

        findSearchView.setQuery(hostActivity.getSearchQuery(), false);
        findSpinner.setSelection(searchTerms.indexOf(selectedSearchTerm));

        searchRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        final String[] from = new String[] {"title"};
        final int[] to = new int[] {R.id.item_search_name};
        mAdapter = new SimpleCursorAdapter(getContext(), R.layout.search_suggest_list_item,
                null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        // Get the SearchView and set the searchable configuration
        final SearchManager searchManager = (SearchManager) hostActivity.getSystemService(Context.SEARCH_SERVICE);
        findSearchView.setSuggestionsAdapter(mAdapter);
        findSearchView.setSearchableInfo(searchManager.getSearchableInfo(hostActivity.getComponentName()));

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

        //handles back stack pop call here
        if (foundItemsAdapter != null) {
            searchRecycler.setAdapter(foundItemsAdapter);
            searchShimmer.setVisibility(View.GONE);
            searchShimmer.stopShimmer();
        }

        if (foundItemsViewModel == null)
            initViewModel();

        findSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get the clinic id of the work by reducing position by 1
                selectedSearchTerm = searchTerms.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Getting selected (clicked) item suggestion
        findSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                mAdapter.getCursor().moveToPosition(position);
                String jg = mAdapter.getCursor().getString(1);

                resetSearch(jg);
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
            public boolean onQueryTextSubmit(String query) {
                resetSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                populateAdapter(newText);
                return true;
            }
        });

        return view;
    }

    private Observer<List<SearchTermItem>> objectsObserver;

    private void initViewModel(){
        FoundItemsViewModelFactory previousItemsViewModelFactory = InjectorUtils.provideFoundItemsViewModelFactory(getContext());
        foundItemsViewModel = ViewModelProviders.of(this, previousItemsViewModelFactory).get(FoundItemsViewModel.class);
        foundItemsViewModel.clearFoundItems();

        objectsObserver = objects -> {

            if (objects == null){
                if (skipCount > 0){
                    foundItemsAdapter.showNullLoader();

                } else {
                    //This check is in case of onBackPressed re-creations
                    if ((foundItemsAdapter != null && foundItemsAdapter.getItemCount() == 0) ||
                            foundItemsAdapter == null) {
                        searchEmptyView.setVisibility(View.VISIBLE);
                        emptyTextView.setText(String.format(getString(R.string.empty_search_text),
                                hostActivity.getSearchTerm(), hostActivity.getSearchQuery()));
                    }
                    searchShimmer.setVisibility(View.GONE);
                    searchShimmer.stopShimmer();
                    Snackbar.make(findSearchView, "Failed to load "
                            + hostActivity.formatToPlural(hostActivity.getSearchTerm()), Snackbar.LENGTH_LONG).show();
                }

                return;
            }

            if (objects.size() > 0) {

                if (foundItemsAdapter == null) {

                    foundItems = objects;
                    foundItemsAdapter = new FoundItemsAdapter(getContext());
                    foundItemsAdapter.instantiateOnViewClickedListener(this);
                    foundItemsAdapter.instantiateOnLoadMoreListener(this);
                    foundItemsAdapter.instantiateOnForceReloadListener(this);
                    searchRecycler.setAdapter(foundItemsAdapter);
                    foundItemsAdapter.createFoundItems(foundItems);
                    Log.e("Find", "Am I called");

                } else {

                    if (skipCount == 0) {
                        foundItemsAdapter.clear();
                        foundItems = objects;
                        foundItemsAdapter.createFoundItems(foundItems);
                        foundItemsAdapter.setLoaded();
                        Log.e("Find", "Have been called");

                    } else {
                        //Check if the skip count has been loaded before
                        //in case of doubled data calls
                        if (lastSkipCount < skipCount) {
                            foundItems = objects;
                            foundItemsAdapter.remove(foundItemsAdapter.getItemCount() - 1);
                            foundItemsAdapter.addFoundItems(foundItems);
                            foundItemsAdapter.setLoaded();

                            lastSkipCount = skipCount;
                            Log.e("Find", "loaded extra " + skipCount);
                        }
                    }
                }

                searchEmptyView.setVisibility(View.GONE);
                searchShimmer.setVisibility(View.GONE);
                searchShimmer.stopShimmer();

            } else { //if size is empty

                if (skipCount > 0){
                    foundItemsAdapter.remove(foundItemsAdapter.getItemCount() - 1);

                } else {
                    searchEmptyView.setVisibility(View.VISIBLE);
                    emptyTextView.setText(String.format(getString(R.string.empty_search_text),
                            hostActivity.getSearchTerm(), hostActivity.getSearchQuery()));
                    searchShimmer.setVisibility(View.GONE);
                    searchShimmer.stopShimmer();
                }
            }
        };

        //Observe the found items
        foundItemsViewModel.getFoundItems(hostActivity.getSearchTerm(),
                hostActivity.getSearchQuery()).observe(this, objectsObserver);
    }

    private void resetSearch(String searchQuery){
        skipCount = 0;
        lastSkipCount = 0;

        //Remove the keyboard
        findSearchView.clearFocus();

        hostActivity.setSearchQuery(searchQuery);
        hostActivity.setSearchTerm(selectedSearchTerm);

        searchEmptyView.setVisibility(View.GONE);

        if (foundItemsAdapter != null) {
            foundItems = new ArrayList<>();
            foundItemsAdapter.clear();
            foundItemsAdapter.setLoading();
        }

        searchShimmer.startShimmer();
        searchShimmer.setVisibility(View.VISIBLE);

        foundItemsViewModel.populateExtra(selectedSearchTerm,
                searchQuery, 0);

        hostActivity.setToolBarTitle(hostActivity.formatToPlural(selectedSearchTerm));
    }

    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "title" });
        for (int i=0; i<terms.size(); i++) {
            if (terms.get(i).toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[] {i, terms.get(i)});
        }
        mAdapter.changeCursor(c);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("skipCount", skipCount);
        outState.putInt("lastSkipCount", lastSkipCount);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewClicked(String id, String name, String subName) {
        mListener.onViewIdActionPerformed(id, name, subName);
    }

    @Override
    public void onLoadMore() {
        Log.e("Find", "Load more called with skip at "+skipCount);

        skipCount = skipCount + 20;

        foundItemsAdapter.add(null);

        foundItemsViewModel.populateExtra(hostActivity.getSearchTerm(),
                hostActivity.getSearchQuery(), skipCount);

    }

    @Override
    public void onResume() {
        super.onResume();
        hostActivity.setToolBarTitle(hostActivity.formatToPlural(hostActivity.getSearchTerm()));
    }

    @Override
    public void onForceReload() {
        Log.e("Find", "Force reload called with skip at "+skipCount);
        foundItemsAdapter.removeNullLoader();
        foundItemsViewModel.populateExtra(hostActivity.getSearchTerm(),
                hostActivity.getSearchQuery(), skipCount);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        Log.e("Find", "Am i Destroyed?");
        super.onDestroy();
    }
}
