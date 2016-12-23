package com.ifd.androidbarcodereader.service;

import android.os.AsyncTask;
import android.widget.Toast;

import com.ifd.androidbarcodereader.activities.ListPdfActivity;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by LenVo on 12/21/16.
 */

public class SearchDocumentTask extends AsyncTask<Void, Void, Map<String, Object>> {
    private final String mUsername;
    private final String mPassword;
    private final String mBarcode;
    private ListPdfActivity activity;

    public SearchDocumentTask(String username, String password, String mBarcode, ListPdfActivity activity) {
        this.mUsername = username;
        this.mPassword = password;
        this.mBarcode = mBarcode;
        this.activity = activity;
    }
    @Override
    protected Map<String, Object> doInBackground(Void... params) {
        try {
            // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return null;
        }

        IviewService service = new IviewService();
        Map<String, Object> result = service.search(mUsername, mPassword, mBarcode);
        return result;
    }


    @Override
    protected void onPostExecute(final Map<String, Object> mapResult) {
        if (!mapResult.containsKey("result")) {
            Toast.makeText(this.activity.getApplicationContext(), "Can't get document from server. Please contact Administrator. Error code: " + mapResult.get("status_code"), Toast.LENGTH_LONG).show();
            return;
        }
        activity.updateListDocument((JSONObject) mapResult.get("result"));
    }

    @Override
    protected void onCancelled() {

    }
}
