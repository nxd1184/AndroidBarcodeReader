package com.ifd.androidbarcodereader.service;

import android.os.AsyncTask;
import android.widget.Toast;

import com.ifd.androidbarcodereader.activities.DetailPdfActivity;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by LenVo on 12/26/16.
 */

public class GetPdfDocumentTask  extends AsyncTask<Void, Void, Map<String, Object>> {
    private final String mUsername;
    private final String mPassword;
    private final String mArchive;
    private final String mFileName;
    private final int mPage;
    private final boolean mGetNumOfPage;

    //TODO: It is old way to drawing. need to open bellow code to get the PDF file from server.
//    private SignPdfActivity activity;
    private DetailPdfActivity activity;
    public GetPdfDocumentTask(String username, String password, String mArchive, String mFileName, int mPage, boolean mGetNumOfPage ,DetailPdfActivity activity) {
        this.mUsername = username;
        this.mPassword = password;
        this.mArchive = mArchive;
        this.mFileName = mFileName;
        this.mGetNumOfPage = mGetNumOfPage;
        this.mPage = mPage;
        this.activity = activity;
    }
    @Override
    protected Map<String, Object> doInBackground(Void... params) {
        IviewService service = new IviewService();
        Map<String, Object> result = service.getPdfDocument(mUsername, mPassword, mArchive, mFileName, mPage, mGetNumOfPage);
        return result;
    }


    @Override
    protected void onPostExecute(final Map<String, Object> mapResult) {
        if (!mapResult.containsKey("result")) {
            Toast.makeText(this.activity.getApplicationContext(), "Can't get document from server. Please contact Administrator. Error code: " + mapResult.get("status_code"), Toast.LENGTH_LONG).show();
            return;
        }
        activity.showPdfDocument((JSONObject) mapResult.get("result"));
    }

    @Override
    protected void onCancelled() {

    }
}
