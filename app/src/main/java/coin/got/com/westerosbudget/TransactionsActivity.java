
package coin.got.com.westerosbudget;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import coin.got.com.westerosbudget.fragment.BaseFragment;
import coin.got.com.westerosbudget.fragment.TransactionsFragment;

/**
 * Main Activity to be shown in the application
 */
public class TransactionsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(BaseFragment.FragmentId.TRANSACTIONS_FRAGMENT.name()) == null) {
            final TransactionsFragment transactionsFragment = new TransactionsFragment();
            final FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.fragment_frame, transactionsFragment,
                    BaseFragment.FragmentId.TRANSACTIONS_FRAGMENT.name()).addToBackStack(
                    transactionsFragment.getFragmentId().name());
            transaction.commit();
        }

    }

}
