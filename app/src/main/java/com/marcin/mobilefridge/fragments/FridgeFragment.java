package com.marcin.mobilefridge.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.marcin.mobilefridge.R;
import com.marcin.mobilefridge.model.Product;
import com.marcin.mobilefridge.services.FridgeService;
import com.marcin.mobilefridge.util.SharedPreferencesUtil;
import com.marcin.mobilefridge.view.ProductsAdapter;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import static com.marcin.mobilefridge.util.SharedPreferencesUtil.SHARED_PREFERENCES_FILE_PATH;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FridgeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FridgeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ArrayAdapter<Product> adapter;
    private FridgeService fridgeService;
    private View view;
    private View mProgressView;
    private ListView list;

    public FridgeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fridge, container, false);

        mProgressView = view.findViewById(R.id.fridge_progress);

        list = (ListView) view.findViewById(R.id.listOfProducts);
        SharedPreferencesUtil sharedPreferences = new SharedPreferencesUtil(
                this.getActivity().getSharedPreferences(SHARED_PREFERENCES_FILE_PATH, Context.MODE_PRIVATE));
        fridgeService = new FridgeService(sharedPreferences.restoreData(
                SharedPreferencesUtil.O_AUTH_KEY), sharedPreferences.restoreData
                (SharedPreferencesUtil.LOGIN_PREFERENCES_PATH));


        adapter = new ProductsAdapter(this.getContext(), R.layout.product, new ArrayList<Product>());
        showProgress(true);
        ProductListTask productListTask = new ProductListTask(this.getContext());
        productListTask.execute();

        list.setAdapter(adapter);

        return view;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            view.setVisibility(show ? View.GONE : View.VISIBLE);
            view.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            view.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class ProductListTask extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        private String errorMessage;
        private ArrayList<Product> productList;

        ProductListTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                productList = fridgeService.getProducts();
            } catch (SocketTimeoutException e) {
                errorMessage = getString(R.string.error_connection_failed);
                return false;
            } catch (IOException e) {
                errorMessage = getString(R.string.error_invalid_credentials);
                return false;
            } catch (Exception e) {
                errorMessage = getString(R.string.error_unexpected_error);
                return false;
            }
            fridgeService.getProductsImages(productList);
            return true;


        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            if (success) {
                try {
                    adapter.clear();
                    adapter.addAll(productList);
                    ((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                adapter = new ArrayAdapter<>(context, R.layout.product, new ArrayList<Product>());
            }
        }

        @Override
        protected void onCancelled() {
            adapter = new ArrayAdapter<>(context, R.layout.product, new ArrayList<Product>());
            showProgress(false);
        }
    }


}
