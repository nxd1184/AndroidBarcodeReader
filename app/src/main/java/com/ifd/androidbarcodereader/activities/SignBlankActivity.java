package com.ifd.androidbarcodereader.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ifd.androidbarcodereader.R;
import com.ifd.androidbarcodereader.model.DrawingViewSignature;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class SignBlankActivity extends Activity implements View.OnClickListener {

    //custom drawing view
    private DrawingViewSignature drawView;
    //buttons
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn;
    //sizes
    private float smallBrush, mediumBrush, largeBrush;

    private int left, top, width, heigh;
    private String archiveName;
    private String fileName;
    private int pageNum;
    String userName;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_blank);

        //get drawing view
        drawView = (DrawingViewSignature)findViewById(R.id.drawing);

        //get the palette and first color button
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        //sizes from dimensions
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        //draw button
        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        //set initial size
        drawView.setBrushSize(smallBrush);

//        //erase button
//        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
//        eraseBtn.setOnClickListener(this);

        //new button
        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        //save button
        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        Bundle extra = getIntent().getExtras();
        if (extra == null) {
            Toast.makeText(getApplicationContext(), "Internal error, please try again", Toast.LENGTH_LONG).show();
            showHomeScreen();
            return;
        }
        archiveName = (String) extra.get("archiveName");
        if (archiveName == null || archiveName.equals("")) {
            Toast.makeText(getApplicationContext(), "Please choose PDF file before use this feature", Toast.LENGTH_LONG).show();
            showListPDF();
            return;
        }
        fileName = (String) extra.get("fileName");
        if (fileName == null || fileName.equals("")) {
            Toast.makeText(getApplicationContext(), "Please choose PDF file before use this feature", Toast.LENGTH_LONG).show();
            showListPDF();
            return;
        }
        pageNum = (int) extra.get("pageNum");
        if (pageNum == 0) {
            Toast.makeText(getApplicationContext(), "Please choose PDF file before use this feature", Toast.LENGTH_LONG).show();
            showListPDF();
            return;
        }

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_key_app), Context.MODE_PRIVATE);
        userName = sharedPref.getString(getString(R.string.preference_user_name_key), null);
        password = sharedPref.getString(getString(R.string.preference_password_key), null);
        if (userName == null || password == null) {
            showHomeScreen();
            Toast.makeText(getApplicationContext(), "Please login before use this feature", Toast.LENGTH_LONG).show();
            return;
        }

    }

    private void showListPDF() {
        this.finish();
    }
    private void showHomeScreen() {
        Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(mainIntent);
    }
    //user clicked paint
    public void paintClicked(View view){
        //use chosen color

        //set erase false
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());

        if(view!=currPaint){
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            //update ui
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }

    @Override
    public void onClick(View view){

        if(view.getId()==R.id.draw_btn){
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            //listen for clicks on size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            //show and wait for user interaction
            brushDialog.show();
        }
//        else if(view.getId()==R.id.erase_btn){
//            //switch to erase - choose size
//            final Dialog brushDialog = new Dialog(this);
//            brushDialog.setTitle("Eraser size:");
//            brushDialog.setContentView(R.layout.brush_chooser);
//            //size buttons
//            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
//            smallBtn.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setErase(true);
//                    drawView.setBrushSize(smallBrush);
//                    brushDialog.dismiss();
//                }
//            });
//            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
//            mediumBtn.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setErase(true);
//                    drawView.setBrushSize(mediumBrush);
//                    brushDialog.dismiss();
//                }
//            });
//            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
//            largeBtn.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setErase(true);
//                    drawView.setBrushSize(largeBrush);
//                    brushDialog.dismiss();
//                }
//            });
//            brushDialog.show();
//        }
        else if(view.getId()==R.id.new_btn){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        else if(view.getId()==R.id.save_btn){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    drawView.setDrawingCacheEnabled(true);
                    Bitmap saveBitMap = eraseBG(drawView.getCanvasBitmap(), -1);         // use for white background
                    String filename = UUID.randomUUID().toString()+".png";
                    FileOutputStream out = null;
                    try {
                        File sd = Environment.getExternalStorageDirectory();
                        File dest = new File(sd.getAbsoluteFile(), filename);

                        out = new FileOutputStream(dest);
                        saveBitMap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery! ", Toast.LENGTH_SHORT);


                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        saveBitMap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream .toByteArray();
                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);


                        //Store png file to base64
                        File dest2 = new File(sd.getAbsoluteFile(), filename + ".txt");
                        FileOutputStream out2 = new FileOutputStream(dest2);
                        out2.write(encoded.getBytes());
                        out2.close();

                        savedToast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
//                    //attempt to save
//                    String imgSaved = MediaStore.Images.Media.insertImage(
//                            getContentResolver(), drawView.getDrawingCache(),
//                            UUID.randomUUID().toString()+".png", "drawing");
//                    //feedback
//                    if(imgSaved!=null){
//                        Toast savedToast = Toast.makeText(getApplicationContext(),
//                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
//                        savedToast.show();
//                    }
//                    else{
//                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
//                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
//                        unsavedToast.show();
//                    }
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
    }
    private static Bitmap eraseBG(Bitmap src, int color) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap b = src.copy(Bitmap.Config.ARGB_8888, true);
        b.setHasAlpha(true);

        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < width * height; i++) {
            if (pixels[i] == color) {
                pixels[i] = 0;
            }
        }

        b.setPixels(pixels, 0, width, 0, 0, width, height);

        return b;
    }
}
