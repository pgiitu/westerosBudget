
package coin.got.com.westerosbudget.request;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import coin.got.com.westerosbudget.manager.RequestManager;

/**
 * Created by prateek on 11/8/15.
 */

public abstract class BaseRequest<T> extends Request<T> {

    private static final String TAG = BaseRequest.class.getSimpleName();
    protected final Context context;
    protected final RequestQueue requestQueue;
    private final Response.Listener<T> listener;
    private Map<String, String> params;

    public BaseRequest(Context ctx, int method, String url, Response.Listener listener,
            Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.context = ctx.getApplicationContext();
        this.requestQueue = RequestManager.getInstance(context).getRequestQueue();
        this.listener = listener;
        this.params = new HashMap<>();
        setRetryPolicy(getMyOwnDefaultRetryPolicy());
    }

    // public BaseRequest(Context ctx, int method, String url, String mParams,
    // Response.Listener listener, Response.ErrorListener errorListener) {
    // super(method, url, errorListener);
    // this.context = ctx.getApplicationContext();
    // this.requestQueue = RequestManager.getInstance(context).getRequestQueue();
    // this.listener = listener;
    // this.mParams = mParams;
    // setRetryPolicy(getMyOwnDefaultRetryPolicy());
    // }

    private RetryPolicy getMyOwnDefaultRetryPolicy() {
        RetryPolicy retrP =
                new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return retrP;
    }

    @Override
    protected final void deliverResponse(T response) {
        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    protected final Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String reply = new String(response.data);

            Log.d(TAG, "statusCode=" + response.statusCode + " :JSON= " + reply);

            JsonObject jobject = new JsonParser().parse(reply).getAsJsonObject();
            return Response.success(getResult(jobject, response.headers),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse network response.");
            return Response.error(new ParseError(e));
        }
    }

    /**
     * function which a subclass can override to set any custom parameters to the builder.
     */
    protected void setCustomBuilderParams(Map<String, String> params) {
        // nothing
    }

    /**
     * @return the parsed result.
     */
    protected abstract T getResult(JsonObject reply, Map<String, String> responseHeader);

}
