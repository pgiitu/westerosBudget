
package coin.got.com.westerosbudget.request;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import coin.got.com.westerosbudget.Configurations;
import coin.got.com.westerosbudget.model.Transaction;

/**
 * Created by prateek on 11/8/15.
 */

/**
 * Class to overwrite the transactions blob
 */
public class UpdateTransactionStatus extends BaseRequest<ArrayList<Transaction>> {

    private static final String TAG = UpdateTransactionStatus.class.getSimpleName();
    Context context;
    JsonArray json;

    public UpdateTransactionStatus(Context ctx, JsonArray object,
            Response.Listener<ArrayList<Transaction>> listener, Response.ErrorListener errorListener) {

        super(ctx, Method.PUT, Configurations.getTransactionsRequestUrl(), listener, errorListener);

        context = ctx;
        json = object;
        requestQueue.add(this);
    }

    @Override
    protected ArrayList<Transaction>
            getResult(JsonObject reply, Map<String, String> responseHeader) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            Gson gson = new Gson();

            if (reply.has("expenses")) {
                JsonArray userArray = reply.getAsJsonArray("expenses");
                if (!userArray.isJsonNull()) {
                    for (JsonElement user : userArray) {
                        Transaction usr =
                                gson.fromJson(user.getAsJsonObject().toString(), Transaction.class);

                        transactions.add(usr);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            JsonObject obj = new JsonObject();
            obj.add("expenses", json);
            return obj.toString().getBytes(getParamsEncoding());
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, e.getMessage().toString());
        }
        return null;
    }

}
