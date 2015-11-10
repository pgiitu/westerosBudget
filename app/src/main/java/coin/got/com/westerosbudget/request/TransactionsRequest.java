
package coin.got.com.westerosbudget.request;

import coin.got.com.westerosbudget.Configurations;
import coin.got.com.westerosbudget.model.Transaction;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by prateek on 11/8/15.
 */

/**
 * Class to fetch all the transactions from the server
 */
public class TransactionsRequest extends BaseRequest<ArrayList<Transaction>> {

    private static final String TAG = TransactionsRequest.class.getSimpleName();
    Context context;

    public TransactionsRequest(Context ctx, Response.Listener<ArrayList<Transaction>> listener,
            Response.ErrorListener errorListener) {

        super(ctx, Method.GET, Configurations.getTransactionsRequestUrl(), listener, errorListener);
        context = ctx;
        requestQueue.add(this);
    }

    @Override
    protected ArrayList<Transaction> getResult(JsonObject reply, Map responseHeader) {
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
}
