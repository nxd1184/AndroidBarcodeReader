package com.ifd.androidbarcodereader.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ifd.androidbarcodereader.R;
import com.ifd.androidbarcodereader.service.IviewService;
import com.ifd.androidbarcodereader.utils.Constant;

public class OneFragment extends Fragment {

    private FragmentIntentIntegrator integrator;
    private TextView mResult;
    private final String NOT_FOUND = "Not Found";
    private String barCodeResult = NOT_FOUND;
    private Button mScanner;
    private Button mSearchInIviewButton;
    private Resources res;
    private TextView mLogoutTxt;
    private View myFragmentView;


    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("result", barCodeResult);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_one, container, false);
        res = getResources();

        integrator = new FragmentIntentIntegrator(this);
        this.mResult = (TextView) myFragmentView.findViewById(R.id.resultTextView);
        mResult.setText(res.getString(R.string.scan_result,this.barCodeResult));
        mScanner = (Button) myFragmentView.findViewById(R.id.scanner2);
        mScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                integrator.initiateScan();
            }
        });

        mSearchInIviewButton = (Button) myFragmentView.findViewById(R.id.searchInIviewButton);
        mLogoutTxt = (TextView) myFragmentView.findViewById(R.id.logout);
        final SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(
                getString(R.string.preference_key_app), Context.MODE_PRIVATE);
        if(sharedPref.contains(getString(R.string.preference_user_name_key)) && sharedPref.contains(getString(R.string.preference_password_key))){
            TextView welcome = (TextView) myFragmentView.findViewById(R.id.welcome);
            welcome.setText("Welcome, " + sharedPref.getString(getString(R.string.preference_user_name_key),""));
            mLogoutTxt.setVisibility(View.VISIBLE);
        }

        mLogoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(getString(R.string.preference_user_name_key));
                editor.remove(getString(R.string.preference_password_key));
                editor.commit();
                TextView welcome = (TextView) myFragmentView.findViewById(R.id.welcome);
                welcome.setText("Welcome, GUEST");
                mLogoutTxt.setVisibility(View.INVISIBLE);
            }
        });


        mSearchInIviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barCodeResult = "1234567";
                if (Constant.server_url == null || Constant.server_url.equals("")) {
                    Toast.makeText(getContext(), "Please config server iView url before use this feature", Toast.LENGTH_SHORT).show();
                    return;
                }
                IviewService iviewService = new IviewService();
                if(barCodeResult == NOT_FOUND) {
                    Toast.makeText(getContext(), "Please scan bar code before use this feature", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!sharedPref.contains(getString(R.string.preference_user_name_key)) || !sharedPref.contains(getString(R.string.preference_password_key))){
                    Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                    loginIntent.putExtra("barcode", barCodeResult);
                    startActivity(loginIntent);
                }else{
                    String username = sharedPref.getString(getString(R.string.preference_user_name_key),"");
                    String password = sharedPref.getString(getString(R.string.preference_password_key), "");
                    if(iviewService.login(username, password)){
                        String role = iviewService.getAuthentication(username, password);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.preference_user_role_key), role);
                        editor.commit();

                        Intent listPdfIntent = new Intent(getContext(), ListPdfActivity.class);
                        listPdfIntent.putExtra("barcode", barCodeResult);
                        startActivity(listPdfIntent);
                    }else{
                        Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                        loginIntent.putExtra("barcode", barCodeResult);
                        startActivity(loginIntent);
                    }
                }




//                String iViewUrl = sharedPref.getString("iViewUrl", "");
//                if(iViewUrl.isEmpty()){
//                    Toast toast = Toast.makeText(myFragmentView.getContext(), "Please set iView Url in Configuration Tab", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                    toast.show();
//                }else{
//
//                    if(barCodeResult.equals(NOT_FOUND)){
//                        Toast toast = Toast.makeText(myFragmentView.getContext(), "Barcode Not Found, Please scan barcode first", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                        toast.show();
//                    }else{
//                        Uri uriUrl = Uri.parse(iViewUrl + "?barcode=" + barCodeResult);
//                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
//                        startActivity(launchBrowser);
//                    }
//                }
            }
        });



        return myFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            this.barCodeResult = savedInstanceState.getString("result");
            mResult.setText(res.getString(R.string.scan_result,this.barCodeResult));
        }
    }

    // Get the results:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                barCodeResult = NOT_FOUND;
            } else {
                barCodeResult = result.getContents();
            }

            mResult.setText(res.getString(R.string.scan_result,this.barCodeResult));
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    class FragmentIntentIntegrator extends IntentIntegrator {

        private final Fragment fragment;

        FragmentIntentIntegrator(Fragment fragment) {
            super(fragment.getActivity());
            this.fragment = fragment;
        }

        @Override
        protected void startActivityForResult(Intent intent, int code) {
            fragment.startActivityForResult(intent, code);
        }
    }
}
