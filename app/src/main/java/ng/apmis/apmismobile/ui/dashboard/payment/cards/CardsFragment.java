package ng.apmis.apmismobile.ui.dashboard.payment.cards;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.cardModel.Card;
import ng.apmis.apmismobile.data.database.personModel.Wallet;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardsFragment extends Fragment implements CardListAdapter.OnCardActionListener {

    @BindView(R.id.use_new_card)
    TextView fundWithCardButton;

    @BindView(R.id.balance_text)
    TextView balanceTextView;

    @BindView(R.id.cards_recycler)
    RecyclerView cardsRecycler;

    @BindView(R.id.amount_edit)
    EditText amountEditText;

    @BindView(R.id.fund_wallet_button)
    Button fundWalletButton;

    @BindView(R.id.empty_card_view)
    FrameLayout emptyCardView;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private CardListAdapter cardListAdapter;

    CardsViewModel cardsViewModel;
    Observer<Wallet> walletObserver = null;
    Observer<List<String>> verificationObserver = null;
    Observer<String> removalObserver = null;

    int lastRemovedCardPosition = -1;

    private SnapHelper snapHelper;

    private Card selectedCard;

    private int amountToPay = 0;

    private SharedPreferencesManager prefs;


    public CardsFragment() {
        // Required empty public constructor
    }

    private OnAddCardButtonClickedListener mListener;
    private OnPaymentCompletedListener payListener;

    public interface OnAddCardButtonClickedListener {
        void onAddCardButtonClicked();
    }

    public interface OnPaymentCompletedListener {
        void onPaymentComplete(int result);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cards, container, false);
        ButterKnife.bind(this, view);

        prefs = new SharedPreferencesManager(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        cardsRecycler.setLayoutManager(layoutManager);
        snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(cardsRecycler);

        //handles back stack pop call here
        if (cardListAdapter != null) {
            cardsRecycler.setAdapter(cardListAdapter);
            Log.e("Dash", "Card adapter is not null");
        } else {
            Log.e("Dash", "Card adapter is null");
        }

        fundWithCardButton.setOnClickListener(v -> mListener.onAddCardButtonClicked());
        emptyCardView.setOnClickListener(v -> mListener.onAddCardButtonClicked());

        amountEditText.addTextChangedListener(new TextWatcher() {

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
                        amountEditText.setText(previousTextInBox);
                        AppUtils.showShortToast(getContext(), "Invalid amount");
                        return;
                    }
                }

                formatButtonText(amountToPay, selectedCard);
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
                    amountEditText.setText(AppUtils.formatNumberWithCommas(amountToPay));

                //Move cursor to end of the word
                amountEditText.setSelection(amountEditText.getText().length());
            }
        });

        initViewModel();

        fundWalletButton.setOnClickListener(v -> {

            if (amountToPay >= 50 && selectedCard != null) {
                cardsViewModel.clearVerification();
                cardsViewModel.getPayData(null, amountToPay,
                        true, false, selectedCard.getCustomer().getEmail(),
                        selectedCard.getAuthorization().getAuthorizationCode())
                        .observe(CardsFragment.this, verificationObserver);
                showLoadingDialog();
            } else if (amountToPay < 50){
                amountEditText.setError("Please enter amount between ₦50 and ₦20,000,000");
            } else {
                AppUtils.showShortToast(getContext(), "Please select a card");
            }
        });

        return view;
    }

    @Override
    public void onCardSelected(Card card) {
        selectedCard = card;
        formatButtonText(amountToPay, selectedCard);
    }

    @Override
    public void onCardRemoved(Card card, int cardPosition) {
        if (lastRemovedCardPosition != -1){
            AppUtils.showShortToast(getContext(), "Still removing a card");
            return;
        }


        if (selectedCard != null && card.getAuthorization().getSignature()
                .equals(selectedCard.getAuthorization().getSignature())){
            //remove selected card status if selected card is the one to delete
            cardListAdapter.deselectCard();
            selectedCard = null;
            formatButtonText(amountToPay, selectedCard);
        }

        lastRemovedCardPosition = cardPosition;
        cardsViewModel.clearCardRemovalStatus();
        cardsViewModel.getRemovalStatus(card.getId(), wallet).observe(this, removalObserver);
        AppUtils.showShortSnackBar(balanceTextView, "Removing ****"+card.getAuthorization().getLast4());
    }

    private void formatButtonText(int amount, Card card){
        if (amount == 0 && card == null)
            fundWalletButton.setText("PAY");
        else if (amount == 0 && card != null)
            fundWalletButton.setText("PAY WITH ****" + card.getAuthorization().getLast4());
        else if (amount > 0 && card == null)
            fundWalletButton.setText("PAY ₦"+ AppUtils.formatNumberWithCommas(amount));
        else if (amount > 0 && card != null)
            fundWalletButton.setText("PAY ₦"+ AppUtils.formatNumberWithCommas(amount) +
                    " WITH **** " + card.getAuthorization().getLast4());
    }


    private Wallet wallet;

    private void initViewModel(){
        CardsViewModelFactory viewModelFactory = InjectorUtils.provideCardsViewModelFactory(getContext().getApplicationContext());
        cardsViewModel = ViewModelProviders.of(this, viewModelFactory).get(CardsViewModel.class);

        walletObserver = wallet -> {

            if (wallet == null) {
                return;
            }

            this.wallet = wallet;

            balanceTextView.setText(String.format("₦%s", AppUtils.formatNumberWithCommas(wallet.getBalance())));

            if (cardListAdapter == null) {

                if (wallet.getCards() != null) {
                    cardListAdapter = new CardListAdapter(getActivity(), wallet.getCards());
                    cardListAdapter.instantiateSelectionListener(this);
                    cardsRecycler.setAdapter(cardListAdapter);
                    cardsRecycler.setVisibility(View.VISIBLE);
                    fundWithCardButton.setVisibility(View.VISIBLE);
                    emptyCardView.setVisibility(View.GONE);
                }

            } else {
                cardListAdapter.clear();
                cardListAdapter.addAll(wallet.getCards());
                cardListAdapter.notifyDataSetChanged();
            }

            if (wallet.getCards() == null || wallet.getCards().size() == 0){
                cardsRecycler.setVisibility(View.GONE);
                fundWithCardButton.setVisibility(View.INVISIBLE);
                emptyCardView.setVisibility(View.VISIBLE);
            }
        };

        cardsViewModel.getWallet(prefs.getPersonId()).observe(this, walletObserver);

        verificationObserver = s -> {
            dismissLoadingDialog();

            if (s == null || !s.get(0).equals("success")){
                presentCompletionDialog(false);
                return;
            }

            //if payment hit the server and successfully returned a result...

            if (s.get(1).equals("true")) {
                presentCompletionDialog(true);
                AppUtils.showShortToast(getContext(), "SUCCESS");
            } else {
                AppUtils.showShortToast(getContext(), "Unable to charge this card, please use another");
            }
        };

        removalObserver = cardRemovalStatus -> {

            if (cardRemovalStatus != null && cardRemovalStatus.equals("success")){
                cardListAdapter.notifyItemRemoved(lastRemovedCardPosition);
                AppUtils.showShortSnackBar(balanceTextView, "Card successfully removed");
            } else {
                AppUtils.showShortSnackBar(balanceTextView, "Unable to remove card");
            }
            lastRemovedCardPosition = -1;
        };
    }

    private void presentCompletionDialog(boolean isSuccessful){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_fund_completed, null, false);

        TextView textView = view.findViewById(R.id.fund_status_text);
        ImageView imageIcon = view.findViewById(R.id.image_icon);

        if (isSuccessful){
            textView.setText(Html.fromHtml(
                    "Your wallet has successfully been funded with <b>&#8358;"
                            + AppUtils.formatNumberWithCommas(amountToPay) +"</b>"));
        } else {
            imageIcon.setImageResource(R.drawable.ic_error_red_24dp);
            textView.setText("Something went wrong with the payment verification, " +
                    "please contact us within 24hrs to resolve the issue");
        }

        Button button = view.findViewById(R.id.complete_button);
        button.setOnClickListener(v -> {
            if (isSuccessful){
                payListener.onPaymentComplete(RESULT_OK);
            } else {
                payListener.onPaymentComplete(RESULT_CANCELED);
            }
        });

        dialog.setView(view);
        dialog.show();

    }

    private void showLoadingDialog(){
        builder = new AlertDialog.Builder(getContext());
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(getContext());
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
        if (context instanceof OnAddCardButtonClickedListener) {
            mListener = (OnAddCardButtonClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddCardButtonClickedListener");
        }
        if (context instanceof OnPaymentCompletedListener) {
            payListener = (OnPaymentCompletedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPaymentCompletedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        payListener = null;
    }

}
