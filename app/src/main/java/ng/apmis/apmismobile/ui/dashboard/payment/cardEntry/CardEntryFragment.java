package ng.apmis.apmismobile.ui.dashboard.payment.cardEntry;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardEntryFragment extends Fragment {

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

    @BindView(R.id.save_card_switch)
    Switch saveCardSwitch;

    @BindView(R.id.pay_button)
    Button payButton;

    @BindView(R.id.paystack_text)
    TextView paystackTextView;

    private int amountToPay;
    private int amountPaid;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private Context mContext;

    //4084084084084081
    //408

    private OnPaymentCompletedListener mListener;

    public interface OnPaymentCompletedListener {
        void onPaymentComplete(int result);
    }

    public CardEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_entry, container, false);
        ButterKnife.bind(this, view);

        mContext = getContext();

        //Quickly fetch the email and pre-load the email edit text if one is available
        LiveData<PersonEntry> entryLiveData = InjectorUtils.provideRepository(mContext).getUserData();
        Observer<PersonEntry> personEntryObserver = personEntry -> {
            if (personEntry != null){
                if (personEntry.getEmail() != null)
                    emailEdit.setText(personEntry.getEmail());
            }
        };
        entryLiveData.observe(this, personEntryObserver);


        amountEdit.addTextChangedListener(new TextWatcher() {

            String previousTextInBox;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //keep reference to previous text
                previousTextInBox = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                //if text entered (or removed) leaves the box empty...
                if (TextUtils.isEmpty(s))
                    amountToPay = 0;

                else {//otherwise, try checking the value entered
                    try {
                        amountToPay = Integer.parseInt(s.toString().replaceAll(",", ""));
                        Integer.parseInt(amountToPay+"00");//Add two extra zeros for kobo conversion
                    } catch (Exception e){
                        amountEdit.setText(previousTextInBox);
                        AppUtils.showShortToast(getContext(), "Invalid amount");
                        return;
                    }
                }

                formatButtonText(amountToPay);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Do nothing if value entered is invalid
                try {
                    int errCatch = Integer.parseInt(s.toString().replaceAll(",", ""));
                    Integer.parseInt(errCatch+"00");//Add two extra zeros for kobo conversion
                } catch (Exception e){
                    return;
                }

                if (TextUtils.isEmpty(previousTextInBox) || !previousTextInBox.equals(s.toString()))
                    amountEdit.setText(AppUtils.formatNumberWithCommas(amountToPay));

                //Move cursor to end of the word
                amountEdit.setSelection(amountEdit.getText().length());
            }
        });

        initViewModel();

        payButton.setOnClickListener(v -> validateCardForm());

        paystackTextView.setOnClickListener(v -> {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://paystack.com"));
            startActivity(i);
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setTranslationZ(getView(), 10f);
    }

    private void formatButtonText(int amount){
        if (amount == 0)
            payButton.setText("PAY");
        else if (amount > 0)
            payButton.setText("PAY ₦"+ AppUtils.formatNumberWithCommas(amount));
    }

    CardEntryViewModel cardEntryViewModel;
    Observer<List<String>> verificationObserver;

    private void initViewModel(){
        CardEntryViewModelFactory viewModelFactory = InjectorUtils.provideCardEntryViewModelFactory(mContext);
        cardEntryViewModel = ViewModelProviders.of(this, viewModelFactory).get(CardEntryViewModel.class);

        verificationObserver = s -> {
            if (s != null) {
                AppUtils.showShortToast(mContext, s.get(0).toUpperCase());
                dismissLoadingDialog();

                presentCompletionDialog(s.get(0).equals("success"));

                //Monitor if card was able to save
                if (saveCardSwitch.isChecked()){
                    if (s.get(1).equals("false"))
                        AppUtils.showShortToast(getContext(), "Unable to save card details");
                }

            } else {
                dismissLoadingDialog();
                presentCompletionDialog(false);
            }
        };

    }

    private void presentCompletionDialog(boolean isSuccessful){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_fund_completed, null, false);

        TextView textView = view.findViewById(R.id.fund_status_text);
        ImageView imageIcon = view.findViewById(R.id.image_icon);

        if (isSuccessful){
            textView.setText(Html.fromHtml(
                    "Your wallet has successfully been funded with <b>&#8358;"
                            + AppUtils.formatNumberWithCommas(amountPaid) +"</b>"));
        } else {
            imageIcon.setImageResource(R.drawable.ic_error_red_24dp);
            textView.setText("Something went wrong with the payment verification, " +
                    "please contact us within 24hrs to resolve the issue");
        }

        Button button = view.findViewById(R.id.complete_button);
        button.setOnClickListener(v -> {
            if (isSuccessful){
                mListener.onPaymentComplete(RESULT_OK);
            } else {
                mListener.onPaymentComplete(RESULT_CANCELED);
            }
        });

        dialog.setView(view);
        dialog.show();

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
            chargeCard(card, Integer.parseInt(amount.replaceAll(",", "")), email);
            showLoadingDialog();
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

        PaystackSdk.chargeCard(getActivity(), charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                // This is called only after transaction is deemed successful.
                // Retrieve the transaction, and send its reference to your server
                // for verification.
                AppUtils.showShortToast(mContext, "Please Wait...");
                Log.e("Pay", transaction.getReference());

                cardEntryViewModel.clearVerification();

                amountPaid = amount;
                cardEntryViewModel.getPayData(transaction.getReference(), amount, false, saveCardSwitch.isChecked(), null, null).observe(CardEntryFragment.this, verificationObserver);
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                // This is called only before requesting OTP.
                // Save reference so you may send to server. If
                // error occurs with OTP, you should still verify on server.
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
                dismissLoadingDialog();
                if (transaction.getReference() != null) {
                    AppUtils.showShortToast(mContext, transaction.getReference() + " concluded with error: " + error.getMessage());
                    //Log.e("Pay", String.format("%s  concluded with error: %s %s", transaction.getReference(), error.getClass().getSimpleName(), error.getMessage()));
                    //new verifyOnServer().execute(transaction.getReference());
                } else {
                    AppUtils.showShortToast(mContext, error.getMessage());
                    //Log.e("Pay null", String.format("Error: %s %s", error.getClass().getSimpleName(), error.getMessage()));
                }
            }

        });
    }

    private void showLoadingDialog(){
        builder = new AlertDialog.Builder(mContext);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_full_screen_loader, null, false);
        dialog.setView(view);
        dialog.show();
    }

    private void dismissLoadingDialog(){
        if (dialog != null) {
            dialog.cancel();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPaymentCompletedListener) {
            mListener = (OnPaymentCompletedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPaymentCompletedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
