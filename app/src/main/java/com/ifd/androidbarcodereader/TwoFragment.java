package com.ifd.androidbarcodereader;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("iViewUrl", mIviewUrl.getText().toString());
                editor.apply();

                Toast toast = Toast.makeText(myFragmentView.getContext(), "iView Url was saved!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();

            }
        });

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        mIviewUrl.setText(sharedPref.getString("iViewUrl", ""));

        return myFragmentView;
    }
}
