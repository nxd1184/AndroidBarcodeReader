package com.ifd.androidbarcodereader.model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by LenVo on 12/21/16.
 */

public class PdfDocument {
    private String fileName;
    private String filePath;
    private String archiveName;
    private JSONArray indexResults;
    private Boolean canWrite;
    private Boolean canRead;
    private String documentURL;

    public PdfDocument(JSONObject jsonObject) {
        try {
            if (jsonObject.has("fileName"))
                fileName = jsonObject.getString("fileName").replace("\n", "").replace(".PDF", "");
            if (jsonObject.has("filePath"))
                filePath = jsonObject.getString("filePath");
            if (jsonObject.has("canWrite"))
                canWrite = jsonObject.getBoolean("canWrite");
            if (jsonObject.has("canRead"))
                canRead = jsonObject.getBoolean("canRead");
            if (jsonObject.has("archiveName"))
                archiveName = jsonObject.getString("archiveName");
            if (jsonObject.has("documentURL"))
                documentURL = jsonObject.getString("documentURL");
            if (jsonObject.has("indexResults"))
                indexResults = jsonObject.getJSONArray("indexResults");
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getArchiveName() {
        return archiveName;
    }

    public void setArchiveName(String archiveName) {
        this.archiveName = archiveName;
    }

    public JSONArray getIndexResults() {
        return indexResults;
    }

    public void setIndexResults(JSONArray indexResults) {
        this.indexResults = indexResults;
    }

    public Boolean getCanWrite() {
        return canWrite;
    }

    public void setCanWrite(Boolean canWrite) {
        this.canWrite = canWrite;
    }

    public Boolean getCanRead() {
        return canRead;
    }

    public void setCanRead(Boolean canRead) {
        this.canRead = canRead;
    }

    public String getDocumentURL() {
        return documentURL;
    }

    public void setDocumentURL(String documentURL) {
        this.documentURL = documentURL;
    }
}
