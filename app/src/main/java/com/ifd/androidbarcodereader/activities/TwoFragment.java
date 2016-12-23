package com.ifd.androidbarcodereader.activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ifd.androidbarcodereader.R;
import com.ifd.androidbarcodereader.utils.Constant;

public class TwoFragment extends Fragment {

    private View myFragmentView;
    private EditText mIviewUrl;
    private Button mSave;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_two, container, false);
        mIviewUrl = (EditText) myFragmentView.findViewById(R.id.txtIviewUrl);
        mSave = (Button) myFragmentView.findViewById(R.id.btnSave);
        final SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(
                getString(R.string.preference_key_app), Context.MODE_PRIVATE);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIviewUrl.getText().toString() == null || mIviewUrl.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(myFragmentView.getContext(), "Please enter iView Url", Toast.LENGTH_SHORT);
                    toast.show();
                }

                Constant.server_url = mIviewUrl.getText().toString();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.preference_server_url_key), Constant.server_url);
                editor.commit();

                Toast toast = Toast.makeText(myFragmentView.getContext(), "iView Url was saved!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        Constant.server_url = sharedPref.getString(getString(R.string.preference_server_url_key),Constant.server_url);
        mIviewUrl.setText(Constant.server_url);
        return myFragmentView;
    }
}
