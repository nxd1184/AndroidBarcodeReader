package com.ifd.androidbarcodereader.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ifd.androidbarcodereader.R;
import com.ifd.androidbarcodereader.model.DefineBoxIview;
import com.ifd.androidbarcodereader.model.DrawingViewInPDF2;
import com.ifd.androidbarcodereader.service.GetPdfDocumentTask;
import com.ifd.androidbarcodereader.service.SaveSignatureLocationTask;
import com.ifd.androidbarcodereader.utils.Constant;

import org.json.JSONObject;

import java.util.Map;

public class DetailPdfActivity extends Activity implements View.OnClickListener {

    //custom drawing view
    private DrawingViewInPDF2 drawView;
    //buttons
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, nextBtn, preBtn, cropBtn;
    //sizes
    private float smallBrush, mediumBrush, largeBrush;

    private View mProgressView;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder;

    int currentPage = 1;
    int totalPage = 1;
    private String archiveName;
    private String fileName;
    String userName;
    String password;
    String role;
    TextView txtPage;
    private String base64;
    private String barcode;

    DetailPdfActivity applicationContext;
    LinearLayout paintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pdf);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Constant.width_device = displaymetrics.widthPixels;
        Constant.height_device = displaymetrics.heightPixels;
        applicationContext = this;
        //get drawing view
        drawView = (DrawingViewInPDF2)findViewById(R.id.drawing);

        //get the palette and first color button
        paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
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
        drawView.setBrushSize(mediumBrush);

        //erase button
//		eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
//		eraseBtn.setOnClickListener(this);

        //new button
//        newBtn = (ImageButton)findViewById(R.id.new_btn);
//        newBtn.setOnClickListener(this);

        //save button
//        saveBtn = (ImageButton)findViewById(R.id.save_btn);
//        saveBtn.setOnClickListener(this);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        nextBtn = (ImageButton)findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this);
        preBtn = (ImageButton)findViewById(R.id.previous_btn);
        preBtn.setOnClickListener(this);

        txtPage = (TextView) findViewById(R.id.txtPage);
        txtPage.setOnClickListener(this);

        cropBtn = (ImageButton)findViewById(R.id.crop_btn);
        cropBtn.setOnClickListener(this);
        saveBtn = (ImageButton)findViewById(R.id.save_btn1);
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

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_key_app), Context.MODE_PRIVATE);
        userName = sharedPref.getString(getString(R.string.preference_user_name_key), null);
        password = sharedPref.getString(getString(R.string.preference_password_key), null);
        role = sharedPref.getString(getString(R.string.preference_user_role_key), "USER");
        if (userName == null || password == null) {
            showHomeScreen();
            Toast.makeText(getApplicationContext(), "Please login before use this feature", Toast.LENGTH_LONG).show();
            return;
        }
        barcode = (String) extra.get("barcode");

        base64 = (String) extra.get("base64");
        if (base64 != null) {
            currentPage = (int) extra.get("pageNum");
            totalPage = (int) extra.get("totalPage");
            DefineBoxIview definedBox = (DefineBoxIview) extra.get("definedBox");
            updatePDFWithBase64(base64, definedBox);
            updatePageNumber();
            return;
        }

        showProgress(true);
        GetPdfDocumentTask task = new GetPdfDocumentTask(userName, password, archiveName, fileName, 1, true, this);
        task.execute((Void) null);

//		IviewService service = new IviewService();
//		Map<String, Object> mapResult = service.getPdfDocument("anguyen", "anguyen", "GRABBERANH", "ISIGN-KALA0286-161028-ANGUYEN-494336-ANGUYEN-000079.PDF", 1, true);
//		if (!mapResult.containsKey("result")) {
//			Toast.makeText(getApplicationContext(), "Can't get PDF document from server. Please try again. Internal error!", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		JSONObject jsonObject = (JSONObject) mapResult.get("result");



    }
    private void showListPDF() {
        Intent listPdfIntent = new Intent(getApplicationContext(), ListPdfActivity.class);
        listPdfIntent.putExtra("barcode", barcode);
        startActivity(listPdfIntent);
    }
    private void showHomeScreen() {
        Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(mainIntent);
    }
    private void showPageNum(int numPage)
    {
        showProgress(true);
        GetPdfDocumentTask task = new GetPdfDocumentTask(userName, password, archiveName, fileName, numPage, true, this);
        task.execute((Void) null);
    }

    private void updatePageNumber() {
        String pageStr =new String("Page " + currentPage + "/" + totalPage);
        SpannableString content = new SpannableString(pageStr);
        content.setSpan(new UnderlineSpan(), 0, pageStr.length(), 0);
        txtPage.setText(content);
    }
    private void updatePDFWithBase64(String base64, DefineBoxIview definedBox) {
        drawView.setErase(false);
        drawView.setBrushSize(smallBrush);
        drawView.setLastBrushSize(smallBrush);
        drawView.setColor(definedBox.getPaintColor());
        drawView.showPDFContent(base64, definedBox);
        if(definedBox != null)
        {
            if (role.equals("USER")){
                cropBtn.setVisibility(View.GONE);
            }
        }

    }
    public void showPdfDocument(JSONObject jsonObject) {
        if (jsonObject == null || !jsonObject.has("code")) {
            return;
        }
        try {
            if (!jsonObject.has("code")) {
                Toast.makeText(getApplicationContext(), "Can't get PDF document from server. Please try again.", Toast.LENGTH_SHORT).show();
                return;
            }
            DefineBoxIview definedBox = null;
            if (jsonObject.has("signature_location")) {
                definedBox = new DefineBoxIview(jsonObject.getJSONObject("signature_location"));
            }
            try {
                if(jsonObject.has("numberOfPages"))
                    totalPage = Integer.parseInt(jsonObject.getString("numberOfPages"));

                if (jsonObject.getString("code").equals("0")) {
                    Toast.makeText(getApplicationContext(), "Please login to use this feature.", Toast.LENGTH_SHORT).show();
                    showHomeScreen();
                    return;
                }
                String base64 = jsonObject.getString("image");
                updatePDFWithBase64(base64, definedBox);
                updatePageNumber();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            showProgress(false);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//			mProgressView.animate().setDuration(shortAnimTime).alpha(
//					show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//				@Override
//				public void onAnimationEnd(Animator animation) {
//					mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//				}
//			});

            if(show) {
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);
            }
            else {
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
            }

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBarHolder.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    private boolean draw_definebox = false;

    @Override
    public void onClick(View view){

        if(view.getId()==R.id.draw_btn){
            //TODO: Get left, top, width, height of the rectangle
            DefineBoxIview definedBox = drawView.getDefinedBox();
            if(definedBox == null)
            {
                Toast.makeText(getApplicationContext(), "Please define the box before signing", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(getApplicationContext(), SignBlankActivity.class);
            intent.putExtra("definedBox", definedBox);

            intent.putExtra("archiveName", archiveName);
            intent.putExtra("fileName", fileName);
            intent.putExtra("pageNum", currentPage);
            intent.putExtra("totalPage", totalPage);
            intent.putExtra("barcode", barcode);
            applicationContext.startActivity(intent);

        }
        else if(view.getId()==R.id.crop_btn){
            draw_definebox = !draw_definebox;
            if(draw_definebox) {
                paintLayout.setVisibility(View.VISIBLE);
            }
            else {
                paintLayout.setVisibility(View.GONE);
                drawView.startNew();
            }
            drawView.setFlagDefineBox(draw_definebox);
        }
//		else if(view.getId()==R.id.erase_btn){
//			//switch to erase - choose size
//			final Dialog brushDialog = new Dialog(this);
//			brushDialog.setTitle("Eraser size:");
//			brushDialog.setContentView(R.layout.brush_chooser);
//			//size buttons
//			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
//			smallBtn.setOnClickListener(new OnClickListener(){
//				@Override
//				public void onClick(View v) {
//					drawView.setErase(true);
//					drawView.setBrushSize(smallBrush);
//					brushDialog.dismiss();
//				}
//			});
//			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
//			mediumBtn.setOnClickListener(new OnClickListener(){
//				@Override
//				public void onClick(View v) {
//					drawView.setErase(true);
//					drawView.setBrushSize(mediumBrush);
//					brushDialog.dismiss();
//				}
//			});
//			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
//			largeBtn.setOnClickListener(new OnClickListener(){
//				@Override
//				public void onClick(View v) {
//					drawView.setErase(true);
//					drawView.setBrushSize(largeBrush);
//					brushDialog.dismiss();
//				}
//			});
//			brushDialog.show();
//		}
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
        else if(view.getId()==R.id.save_btn1){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save Signature Location?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                //TODO: Get drawbox -> save it into the server -> reload the view
                DefineBoxIview drawbox = drawView.getDrawBox();
                if(drawbox == null )
                {
                    Toast.makeText(getApplicationContext(), "Please define the box before save signature location", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgress(true);
                SaveSignatureLocationTask task = new SaveSignatureLocationTask(applicationContext, userName, password, archiveName, drawbox.getLeft(), drawbox.getTop(), drawbox.getWidth(), drawbox.getHeight(), drawView.getPaintColor());
                task.execute((Void) null);

                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
        else if (view.getId()==R.id.next_btn){
            if (currentPage < totalPage)
                currentPage++;
            showPageNum(currentPage);
        }
        else if (view.getId()==R.id.previous_btn){
            if (currentPage > 0)
                currentPage--;
            showPageNum(currentPage);
        }
        else if (view.getId() == R.id.txtPage) {
            //draw button clicked
            final Dialog pageDialog = new Dialog(this);
            pageDialog.setTitle("Go To Page:");
            pageDialog.setContentView(R.layout.page_chooser);
            final EditText txtNumPage = (EditText) pageDialog.findViewById(R.id.txtNumPage);
            txtNumPage.setHint(currentPage + "");
            Button OK_bnt = (Button) pageDialog.findViewById(R.id.OK_btn);
            OK_bnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!txtNumPage.getText().toString().equals("")) {
                        int numPage = Integer.parseInt(txtNumPage.getText().toString());
                        if(numPage <= totalPage && numPage > 0) {
                            currentPage = numPage;
                            showPageNum(numPage);
                        } else {
                          Toast.makeText(getApplicationContext(), "Please enter the page from [1," + totalPage + "]", Toast.LENGTH_SHORT).show();
                        }
                    }
                    pageDialog.dismiss();
                }
            });
            //show and wait for user interaction
            pageDialog.show();
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
    @Override
    public void onBackPressed() {
        showListPDF();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showListPDF();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    public void saveSignatureLocationComplete(Map<String, Object> mapResult)
    {
        paintLayout.setVisibility(View.GONE);
        draw_definebox = false;
        drawView.setFlagDefineBox(draw_definebox);
        if (!mapResult.containsKey("result")) {
            Toast.makeText(getApplicationContext(), "Can't save signature location, please try again. Error code: " + mapResult.get("status_code"), Toast.LENGTH_LONG).show();
            showProgress(false);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Signature Location saved successfull! ", Toast.LENGTH_SHORT).show();
            GetPdfDocumentTask task = new GetPdfDocumentTask(userName, password, archiveName, fileName, currentPage, true, this);
            task.execute((Void) null);
        }

    }

}
