package com.marcin.mobilefridge.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.marcin.mobilefridge.R;
import com.marcin.mobilefridge.model.Recipe;
import com.marcin.mobilefridge.services.RecipeService;
import com.marcin.mobilefridge.util.SharedPreferencesUtil;
import com.marcin.mobilefridge.view.RecipeAdapter;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import static com.marcin.mobilefridge.util.SharedPreferencesUtil.SHARED_PREFERENCES_FILE_PATH;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RecipesFragment extends Fragment {

    private ArrayAdapter<Recipe> adapter;

    private OnFragmentInteractionListener mListener;
    private View view;
    private RecipeService recipeService;
    private ListView list;

    public RecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recipes, container, false);
        list = (ListView) view.findViewById(R.id.listOfRecipes);
        SharedPreferencesUtil sharedPreferences = new SharedPreferencesUtil(
                this.getActivity().getSharedPreferences(SHARED_PREFERENCES_FILE_PATH, Context.MODE_PRIVATE));
        recipeService = new RecipeService(sharedPreferences.restoreData(
                SharedPreferencesUtil.O_AUTH_KEY), sharedPreferences.restoreData
                (SharedPreferencesUtil.LOGIN_PREFERENCES_PATH));

        adapter = new RecipeAdapter(this.getContext(), R.layout.recipe, new ArrayList<Recipe>());
        list.setAdapter(adapter);
        RecipeListTask recipeListTask = new RecipeListTask(this.getContext());
        recipeListTask.execute();
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

    private class RecipeListTask extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        private String errorMessage;
        private ArrayList<Recipe> recipesList;

        RecipeListTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                recipesList = recipeService.getRecipes();
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
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            showProgress(false);
            if (success) {
                try {
                    adapter.clear();
                    adapter.addAll(recipesList);
                    ((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                adapter = new ArrayAdapter<>(context, R.layout.product, new ArrayList<Recipe>());
            }
            if (true) {

            }
        }

        @Override
        protected void onCancelled() {
            adapter = new ArrayAdapter<>(context, R.layout.product, new ArrayList<Recipe>());
//            showProgress(false);
        }
    }
}
