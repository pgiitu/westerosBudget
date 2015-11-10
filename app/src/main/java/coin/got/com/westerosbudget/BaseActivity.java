
package coin.got.com.westerosbudget;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;

import coin.got.com.westerosbudget.fragment.BaseFragment;
import coin.got.com.westerosbudget.manager.RequestManager;

/**
 * Created by prateek on 11/8/15.
 */
public class BaseActivity extends AppCompatActivity implements
        BaseFragment.FragmentInteractionListener {

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected Context context;
    protected RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        // Volley.
        requestQueue = RequestManager.getInstance(context).getRequestQueue();

    }

    @Override
    public void sampleMethod() {

    }
}
