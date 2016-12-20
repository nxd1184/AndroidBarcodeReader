package com.ifd.androidbarcodereader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ifd.androidbarcodereader.service.IviewService;

import java.util.Map;

public class ListPdfActivity extends AppCompatActivity {

    private TextView barcodeResultTextView;
    private IviewService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pdf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String barcode = getIntent().getExtras().getString("barcode");
        barcodeResultTextView = (TextView) findViewById(R.id.textView);
        barcodeResultTextView.setText(barcode);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String username = sharedPref.getString("username", "");
        String password = sharedPref.getString("password", "");
        service = new IviewService();
        barcode = "abc";
        Map<String, Object> result = service.search(username, password, barcode);

        Log.d("ListPdfActivity", result.get("code").toString());
    }

}
