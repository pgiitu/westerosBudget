
package coin.got.com.westerosbudget.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import coin.got.com.westerosbudget.R;
import coin.got.com.westerosbudget.adapter.TransactionsAdapter;
import coin.got.com.westerosbudget.manager.TransactionsManager;
import coin.got.com.westerosbudget.model.Transaction;
import coin.got.com.westerosbudget.utils.Utils;

/**
 * Transaction fragment containing a recyler view to show transactions and way to filter the
 * Recharge and Taxi transactions.
 */
public class TransactionsFragment extends BaseFragment {

    private static int POLLING_INTERVAL = 60000; // 1 min

    // enum fo the filtering of transactions
    public enum FILTER {
        ALL, TAXI, RECHARGE
    }

    RecyclerView recyclerView;

    TransactionsManager transactionManager;
    TransactionsAdapter adapter;

    boolean firstTimeFetch;
    private FILTER currentFilter = FILTER.ALL;
    private Dialog stateDialog;
    boolean transactionUpdateGoingOn = false;

    // different Views
    private TextView emptyTransactionsView;
    private View progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private View filterLayout;
    private TextView allView;
    private TextView taxiView;
    private TextView rechargeView;

    // using a handler to poll the server when the app is in foreground
    private Handler handler;
    private Runnable checkServerUpdates = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getActivity(), "Checking for server updates", Toast.LENGTH_SHORT).show();
            transactionManager.requestTransactions();
            handler.postDelayed(checkServerUpdates, POLLING_INTERVAL);
        }
    };

    /**
     * listener called whenever we contact the server for data.
     */
    TransactionsManager.OnDataChangedListener dataChangedListener =
            new TransactionsManager.OnDataChangedListener() {

                @Override
                public void onDataChanged(boolean dataChanged) {
                    if (dataChanged) {
                        if (!firstTimeFetch) {
                            Toast.makeText(getActivity(), "Got new data", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        showHideViews();
                    }
                    if (firstTimeFetch) {
                        firstTimeFetch = false;
                    }
                    refreshData();

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onDataError(VolleyError error) {
                    if (firstTimeFetch) {
                        firstTimeFetch = false;
                    }
                    refreshData();
                }
            };

    /**
     * click listener for when the user clicks on any of the filteroptions
     */
    View.OnClickListener filterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int darkColor = ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark);
            int lightColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
            switch (v.getId()) {
                case R.id.taxi:
                    taxiView.setBackgroundColor(darkColor);
                    rechargeView.setBackgroundColor(lightColor);
                    allView.setBackgroundColor(lightColor);
                    setCurrentFilter(FILTER.TAXI);
                    break;
                case R.id.recharge:
                    rechargeView.setBackgroundColor(darkColor);
                    taxiView.setBackgroundColor(lightColor);
                    allView.setBackgroundColor(lightColor);
                    setCurrentFilter(FILTER.RECHARGE);
                    break;
                default:
                case R.id.all:
                    allView.setBackgroundColor(darkColor);
                    taxiView.setBackgroundColor(lightColor);
                    rechargeView.setBackgroundColor(lightColor);
                    setCurrentFilter(FILTER.ALL);
                    break;
            }
        }
    };

    /**
     * listener for whenever the transactions status of any transaction changes
     */
    public TransactionsManager.onTransactionStatusChangeListener statusChangeListener =
            new TransactionsManager.onTransactionStatusChangeListener() {
                @Override
                public void onResponse(boolean success) {
                    if (success) {
                        Toast.makeText(getActivity(),
                                getString(R.string.successful_transaction_update),
                                Toast.LENGTH_SHORT).show();
                        refreshData();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.error_transaction_status),
                                Toast.LENGTH_SHORT).show();
                    }
                    transactionUpdateGoingOn = false;
                }
            };

    public TransactionsFragment() {
    }

    @Override
    public FragmentId getFragmentId() {
        return FragmentId.TRANSACTIONS_FRAGMENT;
    }

    @Override
    public View
            onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            root = (ViewGroup) inflater.inflate(R.layout.fragment_transactions, container, false);
            filterLayout = root.findViewById(R.id.filter_options);
            allView = (TextView) root.findViewById(R.id.all);
            taxiView = (TextView) root.findViewById(R.id.taxi);
            rechargeView = (TextView) root.findViewById(R.id.recharge);
            progressBar = root.findViewById(R.id.loading);
            emptyTransactionsView = (TextView) root.findViewById(R.id.empty_results);
            swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh);

            transactionManager = TransactionsManager.getInstance(getActivity());
            transactionManager.requestTransactions();

            allView.setOnClickListener(filterClickListener);
            rechargeView.setOnClickListener(filterClickListener);
            taxiView.setOnClickListener(filterClickListener);

            progressBar.setVisibility(View.VISIBLE);
            emptyTransactionsView.setVisibility(View.GONE);

            swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    // This method performs the actual data-refresh operation.
                    // The method calls setRefreshing(false) when it's finished.
                    handler.removeCallbacks(checkServerUpdates);
                    handler.postDelayed(checkServerUpdates, 0);
                }
            });

            setupTransactionsView();
            firstTimeFetch = true;
            handler = new Handler();

        }
        return root;
    }

    /**
     * Set up the Transactions recyclerView
     */
    public void setupTransactionsView() {
        recyclerView = (RecyclerView) root.findViewById(R.id.transactions_rv);
        adapter = new TransactionsAdapter(getActivity(), transactionManager.getTransactions());
        adapter.setTransactionStatusClickListener(new TransactionsAdapter.onTransactionStatusClicked() {
            @Override
            public void onStatusClicked(final Transaction transaction) {
                if (stateDialog != null && stateDialog.isShowing()) return;

                final String[] stateNames =
                        Utils.getValidTransactionsStates(transaction.getState());

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getActivity().getResources().getString(
                        R.string.change_status_title));
                builder.setItems(stateNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (transactionUpdateGoingOn) {
                            // if the transaction update is currently going on ignore the click
                            // TODO may be we can communicate it to the user by showing a loading
                            // spinner while we are doing the update
                            return;
                        }
                        transactionUpdateGoingOn = true;
                        transactionManager.updateTransactionsState(transaction.getId(),
                                Utils.getTransactionState(stateNames[which]), statusChangeListener);
                        stateDialog.dismiss();
                    }
                });
                stateDialog = builder.create();
                stateDialog.show();

            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        transactionManager.addOnDataChangedListener(dataChangedListener);
        handler.postDelayed(checkServerUpdates, POLLING_INTERVAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        transactionManager.removeOnDataChangedListener(dataChangedListener);
        handler.removeCallbacks(checkServerUpdates);
    }

    /**
     * function to change the filter and update the data
     * 
     * @param filter
     */
    private void setCurrentFilter(FILTER filter) {
        if (filter == currentFilter) {
            return;
        }
        currentFilter = filter;
        refreshData();
    }

    /**
     * function to show and hide views whenever we receive data
     */
    private void showHideViews() {
        progressBar.setVisibility(View.GONE);
        if (transactionManager.getTransactions().size() == 0) {
            emptyTransactionsView.setText(getResources().getString(R.string.empty_transactions));
            emptyTransactionsView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            filterLayout.setVisibility(View.GONE);
        } else {
            emptyTransactionsView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            filterLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * refresh the recycler view data
     */
    private void refreshData() {
        showHideViews();
        if (currentFilter == FILTER.ALL) {
            adapter.setItems(transactionManager.getTransactions());
        } else if (currentFilter == FILTER.RECHARGE) {
            adapter.setItems(transactionManager
                    .getTransactions(Transaction.TransactionCategory.RECHARGE));
            if (adapter.getItemCount() == 0) {
                emptyTransactionsView.setText(getResources().getString(
                        R.string.empty_recharge_transactions));
                emptyTransactionsView.setVisibility(View.VISIBLE);
            } else {
                emptyTransactionsView.setVisibility(View.GONE);
            }
        } else {
            adapter.setItems(transactionManager
                    .getTransactions(Transaction.TransactionCategory.TAXI));
            if (adapter.getItemCount() == 0) {
                emptyTransactionsView.setText(getResources().getString(
                        R.string.empty_taxi_transactions));
                emptyTransactionsView.setVisibility(View.VISIBLE);
            } else {
                emptyTransactionsView.setVisibility(View.GONE);
            }
        }
    }
}
