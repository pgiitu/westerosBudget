
package coin.got.com.westerosbudget.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coin.got.com.westerosbudget.R;
import coin.got.com.westerosbudget.model.Transaction;

/**
 * Created by prateek on 11/8/15.
 */
public final class Utils {

    /**
     * @param currState
     * @return an array of valid transaction states from a given state
     */
    public static String[] getValidTransactionsStates(Transaction.TransactionState currState) {
        final List<String> stateList = new ArrayList<>();
        Transaction.TransactionState values[] = Transaction.TransactionState.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i] != currState) {
                stateList.add(values[i].toString().toLowerCase());
            }
        }
        return stateList.toArray(new String[stateList.size()]);
    }

    /**
     * @param state
     * @return the enum for the given string representation of the Transaction State
     */
    public static Transaction.TransactionState getTransactionState(String state) {
        return Transaction.TransactionState.valueOf(state.toUpperCase());
    }

    /**
     * @param context
     * @param transaction
     * @return the transaction amout as a string to be displayed
     */
    public static String getReadableAmount(Context context, Transaction transaction) {
        return context.getResources().getString(R.string.transaction_amount,
                Integer.toString(((int) transaction.getAmount())));
    }

    /**
     * @param context
     * @param transaction
     * @return the readable version of the date String
     */
    public static String getReadableDate(Context context, Transaction transaction) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date convertedDate = dateFormat.parse(transaction.getDate());
            return output.format(convertedDate);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
