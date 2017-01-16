package com.ifd.androidbarcodereader.model;

import com.ifd.androidbarcodereader.utils.ColorConvertor;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by LenVo on 1/14/17.
 */

public class DefineBoxIview implements Serializable{
    private int top;
    private int left;
    private int width;
    private int height;
    private int paintColor = 0xFF000000;
    private int lineWidth = 1;
    public DefineBoxIview(JSONObject jsonObject)
    {
        try {
            left = jsonObject.getInt("xLoc");
            top = jsonObject.getInt("yLoc");
            width = jsonObject.getInt("width");
            height = jsonObject.getInt("height");
            paintColor = ColorConvertor.convertStringColorToInt(jsonObject.getString("color").trim());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public DefineBoxIview(int left, int top,  int width, int height, int paintColor, int lineWidth) {
        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;
    }
    public DefineBoxIview clone() {
        DefineBoxIview clone = new DefineBoxIview(left, top, width, height, paintColor, lineWidth);
        return clone;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
