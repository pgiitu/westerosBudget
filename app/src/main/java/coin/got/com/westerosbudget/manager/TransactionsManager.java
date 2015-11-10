
package coin.got.com.westerosbudget.manager;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import coin.got.com.westerosbudget.model.Transaction;
import coin.got.com.westerosbudget.request.TransactionsRequest;
import coin.got.com.westerosbudget.request.UpdateTransactionStatus;

/**
 * Transaction Manager to manage the list of transactions to be displayed in the app Created by
 * prateek on 11/8/15.
 */
public class TransactionsManager {
    private static TransactionsManager Instance;
    private final Context context;
    private List<Transaction> transactions = new ArrayList<>();

    private final List<OnDataChangedListener> dataChangedListeners = new ArrayList<>();

    public static TransactionsManager getInstance(Context context) {
        if (Instance == null) {
            Instance = new TransactionsManager(context);
        }
        return Instance;
    }

    private TransactionsManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public void requestTransactions() {
        TransactionsRequest request =
                new TransactionsRequest(context, new Response.Listener<ArrayList<Transaction>>() {

                    @Override
                    public void onResponse(ArrayList<Transaction> response) {
                        // Check if the number of transactions changed. Assuming transactions cannot
                        // be modified from any other place and we will only add new transactions
                        if (response.size() != transactions.size()) {
                            transactions = response;
                            notifyListeners(true);
                        } else {
                            notifyListeners(false);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notifyListenerError(error);
                    }
                });
    }

    /**
     * @return all transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * @param category
     * @return the given category of transactions
     */
    public List<Transaction> getTransactions(Transaction.TransactionCategory category) {
        List<Transaction> list = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getCategory() == category) {
                list.add(transaction);
            }
        }
        return list;
    }

    /**
     * function to update the status of a transaction
     * 
     * @param id
     * @param newState
     * @param listener
     */
    public void updateTransactionsState(String id, Transaction.TransactionState newState,
            final onTransactionStatusChangeListener listener) {
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();
        for (Transaction transaction : transactions) {
            Transaction t = new Transaction(transaction);
            if (transaction.getId() == id) {
                t.setState(newState);
                jsonArray.add(gson.toJsonTree(t, Transaction.class));
            } else {
                jsonArray.add(gson.toJsonTree(t, Transaction.class));
            }
        }
        new UpdateTransactionStatus(context, jsonArray,
                new Response.Listener<ArrayList<Transaction>>() {

                    @Override
                    public void onResponse(ArrayList<Transaction> response) {
                        transactions = response;
                        if (listener != null) {
                            listener.onResponse(true);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (listener != null) {
                            listener.onResponse(false);
                        }
                    }
                });
    }

    /**
     * registering for a listener for datachange
     * 
     * @param listener
     */
    public void addOnDataChangedListener(OnDataChangedListener listener) {
        dataChangedListeners.add(listener);
    }

    /**
     * removing the data change listener
     * 
     * @param listener
     */
    public void removeOnDataChangedListener(OnDataChangedListener listener) {
        dataChangedListeners.remove(listener);
    }

    /**
     * Notify the listeners for data change
     * 
     * @param dataChanged
     */
    private void notifyListeners(boolean dataChanged) {
        for (OnDataChangedListener listener : dataChangedListeners) {
            listener.onDataChanged(dataChanged);
        }
    }

    /**
     * Notify the listeners for the error
     * 
     * @param error
     */
    private void notifyListenerError(VolleyError error) {
        for (OnDataChangedListener listener : dataChangedListeners) {
            listener.onDataError(error);
        }
    }

    /**
     * Interface for the data change methods
     */
    public interface OnDataChangedListener {
        public void onDataChanged(boolean dataChanged);

        public void onDataError(VolleyError error);
    }

    /**
     * Interface for the Trsnaction status change listener
     */
    public interface onTransactionStatusChangeListener {
        public void onResponse(boolean success);
    }

}
