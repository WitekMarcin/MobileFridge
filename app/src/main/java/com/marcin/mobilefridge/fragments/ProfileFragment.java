package com.marcin.mobilefridge.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.marcin.mobilefridge.R;
import com.marcin.mobilefridge.model.AccountSettings;
import com.marcin.mobilefridge.services.LoginService;
import com.marcin.mobilefridge.util.SharedPreferencesUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static android.app.Activity.RESULT_OK;
import static com.marcin.mobilefridge.util.SharedPreferencesUtil.SHARED_PREFERENCES_FILE_PATH;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View view;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText ageEditText;
    private ImageView imageProfile;
    private TextView nickTextView;
    private LoginService loginService;
    private Bitmap profilePhoto;
    private SharedPreferencesUtil sharedPreferences;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        prepareViewConnectors();

        sharedPreferences = new SharedPreferencesUtil(
                this.getActivity().getSharedPreferences(SHARED_PREFERENCES_FILE_PATH, Context.MODE_PRIVATE));
        loginService = new LoginService();
        AccountSettingsTask accountSettingsTask = new AccountSettingsTask(this.getContext(), sharedPreferences);
        accountSettingsTask.execute();

//        nickTextView.setText("DUPA");
        nickTextView.setText(sharedPreferences.restoreData(SharedPreferencesUtil.LOGIN_PREFERENCES_PATH));

        return view;
    }

    private void prepareViewConnectors() {
        nickTextView = (TextView) view.findViewById(R.id.nick);
        firstNameEditText = (EditText) view.findViewById(R.id.nameField);
        lastNameEditText = (EditText) view.findViewById(R.id.lastNameField);
        ageEditText = (EditText) view.findViewById(R.id.ageEditText);
        imageProfile = (ImageView) view.findViewById(R.id.profileImage);
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhotoForRecipe();
            }
        });
        Button sendButton = (Button) view.findViewById(R.id.buttonSave);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountSettings accountSettings = new AccountSettings();

//
                imageProfile.setDrawingCacheEnabled(true);
                imageProfile.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                imageProfile.layout(0, 0, imageProfile.getMeasuredWidth(), imageProfile.getMeasuredHeight());
                imageProfile.buildDrawingCache(true);
                Bitmap b = Bitmap.createBitmap(imageProfile.getDrawingCache());
                imageProfile.setDrawingCacheEnabled(false);
//

                accountSettings.setImageBitmap(b);
                accountSettings.setImg("profileImage.png");
                accountSettings.setAge(Integer.valueOf(ageEditText.getText().toString()));
                accountSettings.setFirstName(firstNameEditText.getText().toString());
                accountSettings.setSecondName(lastNameEditText.getText().toString());

                AccountSettingsSaveTask accountSettingsSaveTask = new AccountSettingsSaveTask(getContext(), sharedPreferences, accountSettings);
                accountSettingsSaveTask.execute();
            }
        });
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

    private class AccountSettingsTask extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        private SharedPreferencesUtil sharedPreferences;
        private AccountSettings accountSettings;

        AccountSettingsTask(Context context, SharedPreferencesUtil sharedPreferences) {
            this.context = context;
            this.sharedPreferences = sharedPreferences;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                accountSettings = loginService.getAccountSettingsFromServer(sharedPreferences.restoreData(SharedPreferencesUtil.LOGIN_PREFERENCES_PATH),
                        sharedPreferences.restoreData(SharedPreferencesUtil.O_AUTH_KEY));
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                firstNameEditText.setText(accountSettings.getFirstName());
                lastNameEditText.setText(accountSettings.getSecondName());
                ageEditText.setText(String.valueOf(accountSettings.getAge()));
                if (accountSettings.getImg() != null) {
                    File folder = new File(context.getFilesDir() + File.separator + "images");
                    File downloadFile2 = new File(folder.getAbsolutePath(), "profileImage.png");
                    accountSettings.setImageBitmap(BitmapFactory.decodeFile(downloadFile2.getAbsolutePath()));
                    imageProfile.setImageBitmap(accountSettings.getImageBitmap());
                }
            } else {
                Toast.makeText(context, "Jest problem z połączeniem do serwera", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(context, "Jest problem z połączeniem do serwera", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ImageView miniature = (ImageView) view.findViewById(R.id.profileImage);
            profilePhoto = (Bitmap) data.getExtras().get("data");
            miniature.setImageBitmap(profilePhoto);
        }
    }

    public void makePhotoForRecipe() {
        Intent picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(picture, 1);
    }

    private class AccountSettingsSaveTask extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        private SharedPreferencesUtil sharedPreferences;
        private AccountSettings accountSettings;

        AccountSettingsSaveTask(Context context, SharedPreferencesUtil sharedPreferences, AccountSettings accountSettings) {
            this.context = context;
            this.sharedPreferences = sharedPreferences;
            this.accountSettings = accountSettings;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                if (accountSettings.getImageBitmap() != null) {
                    OutputStream fOut;
                    File root = new File(context.getFilesDir() + File.separator + "images");
                    File sdImageMainDirectory = new File(root, "profileImage.png");
                    fOut = new FileOutputStream(sdImageMainDirectory);
                    Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
                    accountSettings.getImageBitmap().compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                }
                loginService.sendAccountSettingsToServer(sharedPreferences.restoreData(SharedPreferencesUtil.LOGIN_PREFERENCES_PATH),
                        sharedPreferences.restoreData(SharedPreferencesUtil.O_AUTH_KEY), accountSettings);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Toast.makeText(context, "Poprawnie zapisano zmiany", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "Wystąpił bład", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(context, "Wystąpił bład", Toast.LENGTH_SHORT).show();
        }
    }

}
