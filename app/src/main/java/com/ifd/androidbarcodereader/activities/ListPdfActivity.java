package com.ifd.androidbarcodereader.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ifd.androidbarcodereader.R;
import com.ifd.androidbarcodereader.model.CustomAdapterList;
import com.ifd.androidbarcodereader.model.PdfDocument;
import com.ifd.androidbarcodereader.service.SearchDocumentTask;
import com.ifd.androidbarcodereader.utils.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListPdfActivity extends AppCompatActivity {

    private String barcode;

    private ListView listview_document;

//    private ImageView menu_home;
//    private ImageView menu_share;
    private List<PdfDocument> list_document;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pdf);
        listview_document = (ListView) findViewById(R.id.list_document);
//        menu_home = (ImageView)findViewById(R.id.menu_home);
//        menu_share = (ImageView)findViewById(R.id.menu_share);
        mProgressView = findViewById(R.id.search_progress);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Constant.width_device = displaymetrics.widthPixels;
        Constant.height_device = displaymetrics.heightPixels;

//        updateUI();
        Bundle extra = getIntent().getExtras();
        if (extra == null) {
            Toast.makeText(getApplicationContext(), "Internal error, please try again", Toast.LENGTH_LONG).show();
            showHomeScreen();
            return;
        }
        barcode = (String) extra.get("barcode");
        if (barcode == null || barcode.equals("")) {
            Toast.makeText(getApplicationContext(), "Please scan bar code before use this feature", Toast.LENGTH_LONG).show();
            showHomeScreen();
            return;
        }
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_key_app), Context.MODE_PRIVATE);
        String userName = sharedPref.getString(getString(R.string.preference_user_name_key), null);
        String password = sharedPref.getString(getString(R.string.preference_password_key), null);
        if (userName == null || password == null) {
            showHomeScreen();
            Toast.makeText(getApplicationContext(), "Please login before use this feature", Toast.LENGTH_LONG).show();
            return;
        }
        showProgress(true);

        SearchDocumentTask task = new SearchDocumentTask(userName, password, barcode, this);
        task.execute((Void) null);

//
//
//
//        if(typePosition == TypeOfPosition.SEX_KNOWLEDGE) {
//            list_position.setAdapter(new CustomAdapterList(this, DatabaseHelper.list_sex_knowledge));
//            title_knowledge.setBackgroundResource(R.drawable.sex_knowledge);
//        }
//        else {
//            list_position.setAdapter(new CustomAdapterList(this, DatabaseHelper.getListSexPositionByType(typePosition)));
//            title_knowledge.setBackgroundResource(R.drawable.list_position_title);
//        }

//        menu_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showHomeScreen();
//            }
//        });
//
//        menu_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shareApp();
//            }
//        });

    }
    public void updateListDocument(JSONObject jsonObject) {
        if (jsonObject == null || !jsonObject.has("documents")) {
            return;
        }
        try {
            list_document = new ArrayList<>();
            JSONArray documents = jsonObject.getJSONArray("documents");
            for (int i = 0; i < documents.length(); i++) {
                JSONObject documentJson = documents.getJSONObject(i);
                PdfDocument document = new PdfDocument(documentJson);
                list_document.add(document);
                listview_document.setAdapter(new CustomAdapterList(this, list_document));
            }
            showProgress(false);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void showHomeScreen() {
        Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(mainIntent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showHomeScreen();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    private void shareApp()
    {
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.sex_position_splash_screen);
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        Uri imageUri = Utils.getLocalBitmapUri(bm);
//
//        //text
//        intent.putExtra(Intent.EXTRA_TEXT, "\nLink tải ứng dụng: " + Constant.server_url);
//        //image
//        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        //type of things
//        intent.setType("*/*");
//        //sending
//        startActivity(intent);
    }
    private void updateUI()
    {
//        int width_img = Constant.width_device/10;
//        RelativeLayout.LayoutParams img_parms = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        img_parms.rightMargin = Utils.getPixelsFromDpUnits(this,10);
//        img_parms.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
//        img_parms.height =  img_parms.width = width_img;
//        menu_home.setLayoutParams(img_parms);
//
//
//        RelativeLayout.LayoutParams img_parms_share = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        img_parms_share.rightMargin = Utils.getPixelsFromDpUnits(this,20) + width_img;
//        img_parms_share.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
////        img_parms.addRule(RelativeLayout.LEFT_OF, R.id.menu_home);
////        img_parms.addRule(RelativeLayout.START_OF, R.id.menu_home);
//        img_parms_share.height =  img_parms_share.width = width_img;
//        menu_share.setLayoutParams(img_parms_share);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
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
        }
    }
}
