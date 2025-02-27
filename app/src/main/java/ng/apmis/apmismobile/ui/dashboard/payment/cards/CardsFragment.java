package ng.apmis.apmismobile.ui.dashboard.payment.cards;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.cardModel.Card;
import ng.apmis.apmismobile.data.database.personModel.Wallet;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardsFragment extends Fragment implements CardListAdapter.OnCardSelectedListener {

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

    private CardListAdapter cardListAdapter;

    CardsViewModel cardsViewModel;
    Observer<Wallet> walletObserver = null;

    private SnapHelper snapHelper;

    private Card selectedCard;

    private int amountToPay = 0;

    private SharedPreferencesManager prefs;


    public CardsFragment() {
        // Required empty public constructor
    }

    private OnAddCardButtonClickedListener mListener;

    public interface OnAddCardButtonClickedListener {
        void onAddCardButtonClicked();
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

        if (cardListAdapter != null) {
            cardsRecycler.setAdapter(cardListAdapter);
        }

        initViewModel();

        return view;
    }

    @Override
    public void onCardSelected(Card card) {
        selectedCard = card;
        formatButtonText(amountToPay, selectedCard);
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



    private void initViewModel(){
        CardsViewModelFactory viewModelFactory = InjectorUtils.provideCardsViewModelFactory(getContext().getApplicationContext());
        cardsViewModel = ViewModelProviders.of(this, viewModelFactory).get(CardsViewModel.class);

        walletObserver = wallet -> {

            if (wallet == null) {
                return;
            }

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

                if (wallet.getCards() == null || wallet.getCards().size() == 0){
                    cardsRecycler.setVisibility(View.GONE);
                    fundWithCardButton.setVisibility(View.INVISIBLE);
                    emptyCardView.setVisibility(View.VISIBLE);
                }

            } else {
                cardListAdapter.clear();
                cardListAdapter.addAll(wallet.getCards());
                cardListAdapter.notifyDataSetChanged();
            }
        };

        cardsViewModel.getWallet(prefs.getPersonId()).observe(this, walletObserver);

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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
