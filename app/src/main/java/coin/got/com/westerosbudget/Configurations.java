
package coin.got.com.westerosbudget;

/**
 * Created by prateek on 11/8/15.
 */
public final class Configurations {

    private static final String TAG = Configurations.class.getSimpleName();

    /**
     * @return the transaction request url. Hard coding it for now
     */
    public static String getTransactionsRequestUrl() {
//        String url = "https://jsonblob.com/api/jsonBlob/563eae56e4b01190df3efe11";
        String url = "https://jsonblob.com/api/jsonBlob/563e061ce4b01190df3ef64a";
        return url;
    }

}
