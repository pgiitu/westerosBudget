
package coin.got.com.westerosbudget.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import coin.got.com.westerosbudget.R;
import coin.got.com.westerosbudget.model.Transaction;
import coin.got.com.westerosbudget.utils.Utils;

/**
 * Adapter for the Transactions List Created by prateek on 11/8/15.
 */
public class TransactionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Transaction> transactions = new ArrayList<>();
    private onTransactionStatusClicked transactionStatusClicked;

    public TransactionsAdapter(Context ctx, List<Transaction> transactions) {
        this.transactions = transactions;
        this.context = ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent,
                        false);
        // Set the view to the ViewHolder
        final TransactionHolder holder = new TransactionHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TransactionHolder transactionHolder = (TransactionHolder) holder;
        final Transaction t = transactions.get(position);
        transactionHolder.transactionId.setText(context.getResources().getString(
                R.string.transaction_id, t.getId()));
        transactionHolder.amount.setText(Utils.getReadableAmount(context, t));
        transactionHolder.date.setText(Utils.getReadableDate(context, t));
        transactionHolder.desc.setText(t.getDescription());
        transactionHolder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transactionStatusClicked != null) {
                    transactionStatusClicked.onStatusClicked(t);
                }
            }
        });
        transactionHolder.category
                .setImageResource(t.getCategory() == Transaction.TransactionCategory.RECHARGE
                        ? R.drawable.ic_phone_android_black_18dp
                        : R.drawable.ic_directions_car_black_18dp);
        paintTransactionStatus(transactionHolder, t);

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void setItems(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    /**
     * Click listener for the Transaction status
     * 
     * @param listener
     */
    public void setTransactionStatusClickListener(onTransactionStatusClicked listener) {
        transactionStatusClicked = listener;
    }

    /**
     * Paint the transaction status for the recycler view item
     * 
     * @param holder
     * @param transaction
     */
    private void paintTransactionStatus(TransactionHolder holder, Transaction transaction) {
        GradientDrawable bgShape = (GradientDrawable) holder.status.getBackground();
        switch (transaction.getState()) {
            case FRAUD:
                bgShape.setColor(ContextCompat.getColor(context, R.color.fraud));
                holder.status.setText(context.getResources().getString(R.string.fraud));
                break;
            case UNVERIFIED:
                bgShape.setColor(ContextCompat.getColor(context, R.color.unverfied_payment));
                holder.status.setText(context.getResources().getString(R.string.unverified));
                break;
            case VERIFIED:
                bgShape.setColor(ContextCompat.getColor(context, R.color.verified_payment));
                holder.status.setText(context.getResources().getString(R.string.verified));
                break;
        }
    }

    /**
     * Viewholder class for the Transaction Item
     */
    private class TransactionHolder extends RecyclerView.ViewHolder {
        public View root;
        public TextView transactionId;
        public TextView desc;
        public TextView date;
        public TextView amount;
        public TextView status;
        public ImageView category;

        public TransactionHolder(View itemView) {
            super(itemView);
            root = itemView;
            transactionId = (TextView) root.findViewById(R.id.transaction_id);
            desc = (TextView) root.findViewById(R.id.desc);
            date = (TextView) root.findViewById(R.id.date);
            amount = (TextView) root.findViewById(R.id.amount);
            status = (TextView) root.findViewById(R.id.status);
            category = (ImageView) root.findViewById(R.id.category);
        }
    }

    /**
     * Listener for the transaction status click
     */
    public interface onTransactionStatusClicked {
        public void onStatusClicked(Transaction transaction);
    }

}
