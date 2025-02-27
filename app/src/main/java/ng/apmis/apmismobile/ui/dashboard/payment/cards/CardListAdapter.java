package ng.apmis.apmismobile.ui.dashboard.payment.cards;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.cardModel.Card;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {

    private Context mContext;
    private List<Card> cards;

    private Card selectedCard;

    public OnCardSelectedListener mListener;

    public interface OnCardSelectedListener {
        void onCardSelected(Card card);
    }

    public CardListAdapter(Context context, List<Card> cards){
        this.mContext = context;
        this.cards = new ArrayList<>(cards);
    }

    public void addAll(List<Card> cards){
        this.cards.addAll(cards);
        notifyDataSetChanged();
    }

    public void clear(){
        cards.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_card_item, parent, false);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {

        Card card = cards.get(position);

        if (selectedCard != null) {
            if (card.getAuthorization().getSignature().equals(selectedCard.getAuthorization().getSignature()))
                holder.cardBackground.setSelected(true);
            else
                holder.cardBackground.setSelected(false);
        }

        holder.lastFourText.setText(card.getAuthorization().getLast4());

        if (card.getAuthorization().getCardType().contains("visa")){
            holder.cardTypeImage.setImageResource(R.drawable.ic_visa);

        } else if (card.getAuthorization().getCardType().contains("verve")){
            holder.cardTypeImage.setImageResource(R.drawable.ic_verve);

        } else if (card.getAuthorization().getCardType().contains("mastercard")){
            holder.cardTypeImage.setImageResource(R.drawable.ic_mastercard);

        } else {
            holder.cardTypeImage.setImageResource(R.drawable.ic_paystack);
        }

        String expiryYear = card.getAuthorization().getExpYear();

        if (expiryYear.length() > 2)
            expiryYear = expiryYear.substring(2, 4);
        holder.expiryText.setText(String.format("%s/%s", card.getAuthorization().getExpMonth(), expiryYear));


        holder.cardBackground.setOnClickListener(v -> {
            selectedCard = card;
            mListener.onCardSelected(selectedCard);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cards == null ? 0 : cards.size();
    }

    public void instantiateSelectionListener(OnCardSelectedListener listener){
        this.mListener = listener;
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_background)
        RelativeLayout cardBackground;

        @BindView(R.id.card_type_image)
        ImageView cardTypeImage;

        @BindView(R.id.last_four_text)
        TextView lastFourText;

        @BindView(R.id.card_holder_text)
        TextView cardHolderText;

        @BindView(R.id.expiry_text)
        TextView expiryText;

        public CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
