
package coin.got.com.westerosbudget.manager;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * RequesManager singleton class to get the request queue to be used by the network requests
 * Created by prateek on 11/8/15.
 */
public final class RequestManager {
    private static RequestManager Instance;

    private RequestQueue requestQueue;

    /**
     * @return the only instance of {@link RequestManager}.
     */
    public static synchronized RequestManager getInstance(Context context) {
        if (Instance == null) {
            Instance = new RequestManager(context);
        }

        return Instance;
    }

    private RequestManager(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    /**
     * @return the only instance of {@link RequestQueue}.
     */
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
