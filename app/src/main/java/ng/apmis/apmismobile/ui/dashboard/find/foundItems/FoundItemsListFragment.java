package ng.apmis.apmismobile.ui.dashboard.find.foundItems;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SearchTermItem;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoundItemsListFragment extends Fragment implements FoundItemsAdapter.OnViewClickedListener {

    @BindView(R.id.search_shimmer)
    ShimmerFrameLayout searchShimmer;

    @BindView(R.id.search_recycler)
    RecyclerView searchRecycler;

    private FoundItemsActivity hostActivity;

    private FoundItemsAdapter foundItemsAdapter;
    FoundItemsViewModel foundItemsViewModel;
    List<SearchTermItem> foundItems = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener{
        void onViewIdActionPerformed(String id, String name);
    }

    public FoundItemsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_items, container, false);
        ButterKnife.bind(this, view);

        hostActivity = (FoundItemsActivity) getActivity();

        searchRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //handles back stack pop call here
        if (foundItemsAdapter != null) {
            searchRecycler.setAdapter(foundItemsAdapter);
            searchShimmer.setVisibility(View.GONE);
            searchShimmer.stopShimmer();
        }

        initViewModel();

        return view;
    }


    private void initViewModel(){
        FoundItemsViewModelFactory previousItemsViewModelFactory = InjectorUtils.provideFoundItemsViewModelFactory(getContext());
        foundItemsViewModel = ViewModelProviders.of(this, previousItemsViewModelFactory).get(FoundItemsViewModel.class);
        foundItemsViewModel.clearFoundItems();

        final Observer<List<SearchTermItem>> objectsObserver = objects -> {

            if (objects != null && objects.size() > 0) {

                if (foundItemsAdapter == null) {
                    searchShimmer.setVisibility(View.GONE);
                    searchShimmer.stopShimmer();

                    foundItems = objects;
                    foundItemsAdapter = new FoundItemsAdapter(getContext());
                    foundItemsAdapter.instantiateOnViewClickedListener(this);
                    searchRecycler.setAdapter(foundItemsAdapter);
                    foundItemsAdapter.createFoundItems(foundItems);
                    foundItemsAdapter.notifyDataSetChanged();
                    Log.e("Find", "Am I called");
                } else {
                    foundItemsAdapter.clear();
                    foundItems = objects;
                    foundItemsAdapter.createFoundItems(foundItems);
                    foundItemsAdapter.notifyDataSetChanged();
                    Log.e("Find", "Have been called");
                }
            }
        };

        //Observe the found items]
        foundItemsViewModel.getFoundItems(hostActivity.searchExtra,
                hostActivity.searchQuery).observe(this, objectsObserver);
    }

    @Override
    public void onViewClicked(String id, String name) {
        mListener.onViewIdActionPerformed(id, name);
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
}
