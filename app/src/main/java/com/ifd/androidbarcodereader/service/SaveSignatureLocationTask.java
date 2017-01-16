package com.ifd.androidbarcodereader.service;

import android.os.AsyncTask;

import com.ifd.androidbarcodereader.activities.DetailPdfActivity;
import com.ifd.androidbarcodereader.utils.ColorConvertor;

import java.util.Map;

/**
 * Created by LenVo on 1/5/17.
 */

public class SaveSignatureLocationTask extends AsyncTask<Void, Void, Map<String, Object>> {
    private final static Double lineWidth = 1.0;
    private final static int widthCanvas = 207;
    private final static int heightCanvas = 417;
    private final String mUsername;
    private final String mPassword;
    private final String mArchive;
//    private final String mFileName;
//    private final int mPage;
    private final int mLeft;
    private final int mTop;
    private final int mWidth;
    private final int mHeight;
    private DetailPdfActivity activity;
    private String mColor;

    public SaveSignatureLocationTask(DetailPdfActivity activity, String mUsername, String mPassword, String mArchive, int mLeft, int mTop, int mWidth, int mHeight, int color) {
        this.activity = activity;
        this.mUsername = mUsername;
        this.mPassword = mPassword;
        this.mArchive = mArchive;
//        this.mFileName = mFileName;
//        this.mPage = mPage;
        this.mLeft = mLeft;
        this.mTop = mTop;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.mColor = ColorConvertor.convertIntColorToString(color);
    }

    @Override
    protected Map<String, Object> doInBackground(Void... params) {
        IviewService service = new IviewService();
        Map<String, Object> result = service.saveSignLocation(mUsername,mPassword,mArchive, mLeft, mTop, mWidth, mHeight,mColor,lineWidth, widthCanvas, heightCanvas);
        return result;
    }


    @Override
    protected void onPostExecute(final Map<String, Object> mapResult) {
        activity.saveSignatureLocationComplete(mapResult);
    }

    @Override
    protected void onCancelled() {

    }
}
