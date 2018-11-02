package ng.apmis.apmismobile.ui.dashboard.buy.fundAccount;

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
import ng.apmis.apmismobile.data.database.fundAccount.Beneficiaries;
import ng.apmis.apmismobile.data.database.fundAccount.Fund;

/**
 * Created by Thadeus-APMIS on 10/23/2018.
 */

public class FundAccountFragment extends Fragment {

    RecyclerView recyclerView;
    FundAccountAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_buys, container, false);


        adapter = new FundAccountAdapter(getActivity(), generateSampleData());

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        return root;
    }

    ArrayList<Object> generateSampleData () {
        ArrayList<Object> allList = new ArrayList<>();

        allList.add(new Beneficiaries("Femi Alonge", R.drawable.apmis_profile));
        allList.add(new Fund("Thadeus Ajayi", "Laboratory", "NGN 30,000", "Tue July 8, 2018 2:30 AM"));
        allList.add(new Beneficiaries("Femi Alonge", R.drawable.apmis_profile));
        allList.add(new Fund("Thadeus Ajayi", "Laboratory", "NGN 30,000", "Tue July 8, 2018 2:30 AM"));
        allList.add(new Beneficiaries("Femi Alonge", R.drawable.apmis_profile));
        allList.add(new Fund("Thadeus Ajayi", "Laboratory", "NGN 30,000", "Tue July 8, 2018 2:30 AM"));
        allList.add(new Beneficiaries("Femi Alonge", R.drawable.apmis_profile));
        allList.add(new Fund("Thadeus Ajayi", "Laboratory", "NGN 30,000", "Tue July 8, 2018 2:30 AM"));

        allList.add(new Fund("Thadeus Ajayi", "Laboratory", "NGN 30,000", "Tue July 8, 2018 2:30 AM"));

        allList.add(new Fund("Thadeus Ajayi", "Laboratory", "NGN 30,000", "Tue July 8, 2018 2:30 AM"));

        allList.add(new Fund("Thadeus Ajayi", "Laboratory", "NGN 30,000", "Tue July 8, 2018 2:30 AM"));

        allList.add(new Fund("Thadeus Ajayi", "Laboratory", "NGN 30,000", "Tue July 8, 2018 2:30 AM"));

        return allList;
    }

}
