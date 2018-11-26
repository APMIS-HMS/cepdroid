package ng.apmis.apmismobile.ui.dashboard.payment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardsFragment extends Fragment {

    @BindView(R.id.fund_with_card_button)
    Button fundWithCardButton;


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

        fundWithCardButton.setOnClickListener(v -> mListener.onAddCardButtonClicked());

        return view;
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
