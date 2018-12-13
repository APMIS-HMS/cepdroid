package ng.apmis.apmismobile.ui.dashboard.buy.payBills;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ng.apmis.apmismobile.R;

/**
 * Created by Thadeus-APMIS on 10/23/2018.
 */

public class PayBillsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pay_bills, container, false);
    }
}
