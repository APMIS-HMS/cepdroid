package ng.apmis.apmismobile.ui.dashboard.payment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

public class FundWalletActivity extends AppCompatActivity implements CardEntryFragment.OnPaymentCompletedListener,
        CardsFragment.OnAddCardButtonClickedListener {

    @BindView(R.id.general_toolbar)
    Toolbar generalToolbar;

    @BindView(R.id.action_bar_title)
    TextView toolbarTitle;

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    ActionBar actionBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_wallet);
        ButterKnife.bind(this);

        setSupportActionBar(generalToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        toolbarTitle.setText("Fund Account");

        CardsFragment cardsFragment = new CardsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, cardsFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }

    /**
     * Replace the current fragment with another
     * @param fragment Fragment used to replace
     */
    private void placeFragment(Fragment fragment) {
        //getSupportFragmentManager().popBackStack("current", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("current")
                .commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPaymentComplete(int result) {
        setResult(result);
        finish();
    }

    @Override
    public void onAddCardButtonClicked() {
        placeFragment(new CardEntryFragment());
    }
}
