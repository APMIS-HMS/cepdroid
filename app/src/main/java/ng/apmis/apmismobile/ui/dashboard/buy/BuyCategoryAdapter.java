package ng.apmis.apmismobile.ui.dashboard.buy;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ng.apmis.apmismobile.ui.dashboard.buy.fundAccount.FundAccountFragment;
import ng.apmis.apmismobile.ui.dashboard.buy.makePurchase.MakePurchaseFragment;
import ng.apmis.apmismobile.ui.dashboard.buy.payBills.PayBillsFragment;

/**
 * Created by Thadeus-APMIS on 10/23/2018.
 */

public class BuyCategoryAdapter extends FragmentPagerAdapter {

    private Context context;
    private String[] pageTitles = new String[]{"Fund Account", "Pay Bills", "Make Purchase"};

    public BuyCategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FundAccountFragment();
            case 1:
                return new PayBillsFragment();
            case 2:
                return new MakePurchaseFragment();
            default:
                return new FundAccountFragment();
        }

    }

    @Override
    public int getCount() {
        return pageTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return pageTitles[0];
            case 1:
                return pageTitles[1];
            case 2:
                return pageTitles[2];
            default:
                return pageTitles[0];
        }
    }
}
