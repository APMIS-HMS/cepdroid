package ng.apmis.apmismobile.ui.dashboard.payment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

public class FundWalletActivity extends AppCompatActivity {

    @BindView(R.id.general_toolbar)
    Toolbar generalToolbar;

    @BindView(R.id.action_bar_title)
    TextView toolbarTitle;

    ActionBar actionBar;

    @BindView(R.id.amount_edit)
    EditText amountEdit;

    @BindView(R.id.email_edit)
    EditText emailEdit;

    @BindView(R.id.card_edit)
    EditText cardEdit;

    @BindView(R.id.expiration_edit)
    EditText expirationEdit;

    @BindView(R.id.cvv_edit)
    EditText cvvEdit;

    @BindView(R.id.save_card_radio)
    RadioButton saveCardRadio;

    @BindView(R.id.pay_button)
    Button payButton;


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

        //Quickly fetch the email and pre-load the email edit text if one is available
        LiveData<PersonEntry> entryLiveData = InjectorUtils.provideRepository(this).getUserData();
        Observer<PersonEntry> personEntryObserver = personEntry -> {
            if (personEntry != null){
                if (personEntry.getEmail() != null)
                    emailEdit.setText(personEntry.getEmail());
            }
        };
        entryLiveData.observe(this, personEntryObserver);

        initViewModel();


        payButton.setOnClickListener(v -> validateCardForm());
    }

    FundWalletViewModel fundWalletViewModel;
    Observer<String> verificationObserver;

    private void initViewModel(){
        FundWalletViewModelFactory viewModelFactory = InjectorUtils.provideFundWalletViewModelFactory(this);
        fundWalletViewModel = ViewModelProviders.of(this, viewModelFactory).get(FundWalletViewModel.class);

        verificationObserver = s -> {
            if (s != null) {
                AppUtils.showShortToast(this, s.toUpperCase());
                dismissDialog();

                if (s.equals("success")){
                    setResult(RESULT_OK);
                } else {
                    setResult(RESULT_CANCELED);
                }

                finish();
            }
        };

    }

    private void validateCardForm(){
        //validate amount field
        String amount = amountEdit.getText().toString().trim();

        if (TextUtils.isEmpty(amount)){
            amountEdit.setError("Please enter an amount to pay");
            return;
        }


        //Validate email field
        String email = emailEdit.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            emailEdit.setError("Please enter an email address");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdit.setError("Please enter a valid email address");
            return;
        }


        //validate card num field
        String cardNum = cardEdit.getText().toString().trim();

        if (TextUtils.isEmpty(cardNum)){
            cardEdit.setError("Empty card number");
            return;
        }

        String expiryString = expirationEdit.getText().toString().trim();
        if (TextUtils.isEmpty(expiryString)){
            expirationEdit.setError("Enter your expiration details");
            return;
        }

        if (expiryString.length() != 5){
            expirationEdit.setError("Please enter proper expiration details");
            return;
        }

        String[] monthYear = expiryString.split("/");
        String month = monthYear[0];
        String year = monthYear[1];

        int monthInt = Integer.parseInt(month);

        if (monthInt > 12 || monthInt < 1){
            expirationEdit.setError("Please enter valid month");
            return;
        }

        int yearInt = Integer.parseInt(year);

        if (yearInt < 0 || yearInt > 99){
            expirationEdit.setError("Please enter valid year");
            return;
        }

        String cvv = cvvEdit.getText().toString().trim();
        if (TextUtils.isEmpty(cvv)){
            cvvEdit.setError("Please enter cvv");
            return;
        }

        if (cvv.length() != 3){
            cvvEdit.setError("Please enter proper cvv");
            return;
        }

        Card card = new Card(cardNum, monthInt, yearInt, cvv);
        if (card.isValid()) {
            // charge card
            chargeCard(card, Integer.parseInt(amount), email);
            showDialog();
        } else {
            if (!card.validNumber())
                cardEdit.setError("Please enter valid card number");
            if (!card.validCVC())
                cvvEdit.setError("Please enter valid cvv");
            if (!card.validExpiryDate())
                expirationEdit.setError("Please enter a valid expiration date");
        }

    }

    private void chargeCard(Card card, int amount, String email){

        //create a Charge object
        Charge charge = new Charge();
        charge.setCard(card); //sets the card to charge
        charge.setEmail(email);
        charge.setAmount(amount*100);//kobo
        charge.addParameter("Platform", "Android APMIS Mobile");

        PaystackSdk.chargeCard(FundWalletActivity.this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                // This is called only after transaction is deemed successful.
                // Retrieve the transaction, and send its reference to your server
                // for verification.
                dismissDialog();
                AppUtils.showShortToast(FundWalletActivity.this, transaction.getReference());
                Log.e("Pay", transaction.getReference());

                fundWalletViewModel.clearVerification();
                showDialog();
                fundWalletViewModel.getPayData(transaction.getReference(), amount).observe(FundWalletActivity.this, verificationObserver);
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                // This is called only before requesting OTP.
                // Save reference so you may send to server. If
                // error occurs with OTP, you should still verify on server.
                AppUtils.showShortToast(FundWalletActivity.this, "Please Wait...");
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                //handle error here

                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error
//                FundWalletActivity.this.transaction = transaction;
//                Log.e("Pay On Error block", "Error");
//                if (error instanceof ExpiredAccessCodeException) {
//                    //FundWalletActivity.this.startAFreshCharge();
//                    //FundWalletActivity.this.chargeCard(card, amount);
//                    Log.e("Pay On expired", "Error");
//                    return;
//                }
//
                dismissDialog();
                if (transaction.getReference() != null) {
                    AppUtils.showShortToast(FundWalletActivity.this, transaction.getReference() + " concluded with error: " + error.getMessage());
                    //Log.e("Pay", String.format("%s  concluded with error: %s %s", transaction.getReference(), error.getClass().getSimpleName(), error.getMessage()));
                    //new verifyOnServer().execute(transaction.getReference());
                } else {
                    AppUtils.showShortToast(FundWalletActivity.this, error.getMessage());
                    //Log.e("Pay null", String.format("Error: %s %s", error.getClass().getSimpleName(), error.getMessage()));
                }
            }

        });
    }

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private void showDialog(){
        builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_full_screen_loader, null, false);
        dialog.setView(view);
        dialog.show();
    }

    private void dismissDialog(){
        if (dialog != null) {
            dialog.cancel();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
