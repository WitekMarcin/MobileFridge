package com.marcin.mobilefridge.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.marcin.mobilefridge.R;
import com.marcin.mobilefridge.services.FridgeService;
import com.marcin.mobilefridge.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FridgeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FridgeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ArrayAdapter<HashMap<String, String>> adapter;

    public FridgeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fridge, container, false);

        ListView list = (ListView) view.findViewById(R.id.listOfProducts);
        SharedPreferencesUtil sharedPreferences = new SharedPreferencesUtil(
                this.getActivity().getPreferences(Context.MODE_PRIVATE));
        FridgeService fridgeService = new FridgeService(sharedPreferences.restoreData(
                SharedPreferencesUtil.O_AUTH_KEY), sharedPreferences.restoreData
                (SharedPreferencesUtil.LOGIN_PREFERENCES_PATH));

        String cars[] = {"Mercedes", "Fiat", "Ferrari", "Aston Martin", "Lamborghini", "Skoda", "Volkswagen", "Audi", "Citroen"};

        ArrayList<String> carL = new ArrayList<String>();
        carL.addAll(Arrays.asList(cars));

        try {
            adapter = new ArrayAdapter<HashMap<String, String>>(this.getContext(), R.layout.product, fridgeService.getProducts());
        } catch (Exception e) {
            adapter = new ArrayAdapter<HashMap<String, String>>(this.getContext(), R.layout.product, new ArrayList<HashMap<String, String>>());
            e.printStackTrace();
        }

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
}
