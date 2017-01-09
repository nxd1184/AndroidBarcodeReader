package com.ifd.androidbarcodereader.service;

import android.os.AsyncTask;

import com.ifd.androidbarcodereader.activities.SignBlankActivity;

import java.util.Map;

/**
 * Created by LenVo on 1/5/17.
 */

public class SaveSignatureTask extends AsyncTask<Void, Void, Map<String, Object>> {
    private final String mUsername;
    private final String mPassword;
    private final String mArchive;
    private final String mFileName;
    private final int mPage;
    private final String mBase64;
    private final int mLeft;
    private final int mTop;
    private final int mWidth;
    private final int mHeight;
    private SignBlankActivity activity;

    public SaveSignatureTask(SignBlankActivity activity, String mUsername, String mPassword, String mArchive, String mFileName, int mPage, String mBase64, int mLeft, int mTop, int mWidth, int mHeight) {
        this.activity = activity;
        this.mUsername = mUsername;
        this.mPassword = mPassword;
        this.mArchive = mArchive;
        this.mFileName = mFileName;
        this.mPage = mPage;
        this.mBase64 = mBase64;
        this.mLeft = mLeft;
        this.mTop = mTop;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
    }

    @Override
    protected Map<String, Object> doInBackground(Void... params) {
        IviewService service = new IviewService();
        Map<String, Object> result = service.savePdfDocument(mUsername,mPassword,mArchive,mFileName,mPage,mBase64,mLeft,mTop,mWidth,mHeight);
        return result;
    }


    @Override
    protected void onPostExecute(final Map<String, Object> mapResult) {
        activity.saveSignatureComplete(mapResult);
    }

    @Override
    protected void onCancelled() {

    }
}
