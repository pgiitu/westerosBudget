
package coin.got.com.westerosbudget.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

/**
 * Created by prateek on 11/8/15.
 */
public abstract class BaseFragment extends Fragment {
    protected FragmentInteractionListener interactionListener;

    protected ViewGroup root;

    public BaseFragment() {
        // Required empty public constructor
    }

    /**
     * An enumerator of IDs for Fragments.
     *
     * @author Prateek
     */
    public enum FragmentId {
        TRANSACTIONS_FRAGMENT,
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        try {
            interactionListener = (FragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    /**
     * @return the identifying enum for the current fragment.
     */
    public abstract FragmentId getFragmentId();

    /**
     * This interface must be implemented by activities that contain this fragment to allow an
     * interaction in this fragment to be communicated to the activity and potentially other
     * fragments contained in that activity.
     */
    public interface FragmentInteractionListener {
        public void sampleMethod();

    }
}
