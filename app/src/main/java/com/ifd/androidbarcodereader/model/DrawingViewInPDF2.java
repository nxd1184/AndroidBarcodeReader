package com.ifd.androidbarcodereader.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.ifd.androidbarcodereader.R;
import com.ifd.androidbarcodereader.utils.Constant;

/**
 * This is demo code to accompany the Mobiletuts+ tutorial series:
 * - Android SDK: Create a Drawing App
 * 
 * Sue Smith
 * August 2013
 *
 */
public class DrawingViewInPDF2 extends View {

	//drawing path
	private Path drawPath;

	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//initial color
	private int paintColor = 0xFF0000FF;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;
	//brush sizes
	private float brushSize, lastBrushSize;
	//erase flag
	private boolean erase=false;
	private Bitmap tempCanvasBitmap;
	private Bitmap transparentBitmap;


	private float startX, startY, endX, endY;

	private final String TAG = "DrawingView";

	public DrawingViewInPDF2(Context context, AttributeSet attrs){
		super(context, attrs);
		setupDrawing();
	}
	public int getLeftRec() {
		return (int)(Math.min(startX, endX));
	}
	public int getTopRec() {
		return (int)(Math.min(startY, endY));
	}
	public int getWidthRec() {
		return (int)(Math.abs(startX - endX));
	}
	public int getHeightRec() {
		return (int)(Math.abs(startY - endY));
	}
	public Bitmap getCanvasBitmap() {
		return canvasBitmap;
	}
	public Bitmap getTransparentBitmap() {
		Canvas canvas = new Canvas(transparentBitmap);

//		Paint transPainter = new Paint();
//		transPainter.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

//		canvas.drawPaint(transPainter);
//		canvas.drawColor(Color.TRANSPARENT);
		return transparentBitmap;
	}

	//setup drawing
	private void setupDrawing(){

		//prepare for drawing and setup paint stroke properties
		brushSize = getResources().getInteger(R.integer.small_size) *scale_x;
		lastBrushSize = brushSize;
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}
	public void showPDFContent(String encodedImage)
	{
		byte[] bytes = Base64.decode(encodedImage.replace("data:image/png;base64,",""), Base64.DEFAULT);
		Log.i(TAG, "On size Change, Bytes length: "+ bytes.length);
		BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
//		options.inSampleSize = 2;
		tempCanvasBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
		canvasBitmap = tempCanvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
//		transparentBitmap = Bitmap.createBitmap(tempCanvasBitmap.getWidth(), tempCanvasBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//		transparentBitmap.eraseColor(Color.WHITE);
//		transparentBitmap.eraseColor(Color.TRANSPARENT);
		drawCanvas = new Canvas(canvasBitmap);

		int width = Constant.width_device;
		int height = Constant.height_device;
		scale_x = ((float)canvasBitmap.getWidth())/width;
		scale_y = ((float)canvasBitmap.getHeight())/height;
		brushSize = getResources().getInteger(R.integer.small_size) *scale_x;
		lastBrushSize = brushSize;

	}

	//size assigned to view
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (canvasBitmap == null && w > 0 && h > 0)
			canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

//		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.test);
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//		byte[] byteArrayImage = baos.toByteArray();
//		String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

//		byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
//		Log.i(TAG, "On size Change, Bytes length: "+ bytes.length);
//		BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
//		options.inSampleSize = 2;
//		tempCanvasBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
//		canvasBitmap = tempCanvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
//
//		transparentBitmap = Bitmap.createBitmap(tempCanvasBitmap.getWidth(), tempCanvasBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//		transparentBitmap.eraseColor(Color.WHITE);
////		transparentBitmap.eraseColor(Color.TRANSPARENT);
//		drawCanvas = new Canvas(canvasBitmap);
//		drawTransparentCanvas = new Canvas(transparentBitmap);

	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		return true;
//
//	}
	private float scale_x = 0, scale_y = 0;
	//draw the view - will be called after touch event
	@Override
	protected void onDraw(Canvas canvas) {
		int width = Constant.width_device;
		int height = Constant.height_device;
		if (canvasBitmap == null)
			canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		Rect src = new Rect(0,0, canvasBitmap.getWidth()-1, canvasBitmap.getHeight()-1);
		Rect dest = new Rect(0,0,width-1, height-1);


		Log.i(TAG, "On start Draw, width=" + width + " -height: " + height + " -canvasBitmap.getWidth(): " + canvasBitmap.getWidth() + " -canvasBitmap.getHeight(): " + canvasBitmap.getHeight() +" -scale_x: " + scale_x + " -scale_y: " +scale_y  + " -lastBrushSize: " + lastBrushSize + " -brushSize: " + brushSize);
//		Rect dest = new Rect(-zoom,-zoom,canvasBitmap.getWidth()-1, canvasBitmap.getWidth()-1);
		canvas.drawBitmap(canvasBitmap, src, dest, canvasPaint);


//		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);

//		canvas.drawBitmap(transparentBitmap, 0, 0, canvasPaint);
//		canvas.drawPath(drawPath, drawPaint);
	}

	//register user touches as drawing action
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (drawCanvas == null)
			return true;
		float touchX = event.getX()*scale_x;
		touchX = (touchX < 0)? 0: touchX;
		touchX = (touchX > canvasBitmap.getWidth())? canvasBitmap.getWidth(): touchX;

		float touchY = Math.abs(event.getY()*scale_y);
		touchY = (touchY < 0)? 0: touchY;
		touchY = (touchY > canvasBitmap.getHeight())? canvasBitmap.getHeight(): touchY;

		Log.i(TAG, "On Touch Event, event.getAction()=" + event.getAction() + " -event.getX()=" + event.getX() + " event.getY()=" + event.getY() + " -touchX: " + touchX + " -touchY: "+ touchY );
		//respond to down, move and up events
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = touchX;
			startY = touchY;
			canvasBitmap = tempCanvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
			drawCanvas = new Canvas(canvasBitmap);
//			drawPath.moveTo(event.getX(), event.getY());
//			drawScalePath.moveTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:
//			drawPath.lineTo(event.getX(), event.getY());
//			drawScalePath.lineTo(touchX, touchY);

			break;
		case MotionEvent.ACTION_UP:
			endX = touchX;
			endY = touchY;
//			drawCanvas.drawRect(startX, startY, endX, endY, drawPaint);
			drawPath.addRect(startX, startY, endX, endY, Path.Direction.CCW);
			drawCanvas.drawPath(drawPath, drawPaint);
//			Toast.makeText(getContext(), "Defined box: Left: " + getLeftRec() + " -Top: " + getTopRec() + " -Width: " + getWidthRec() + " -Height: " + getHeightRec(), Toast.LENGTH_SHORT).show();
//			drawPath.lineTo(touchX, touchY);
//			drawScalePath.lineTo(touchX, touchY);
//			drawCanvas.drawPath(drawScalePath, drawPaint);
//			drawTransparentCanvas.drawPath(drawScalePath, drawPaint);
			drawPath.reset();
//			drawScalePath.reset();
			break;
		default:
			return false;
		}
		//redraw
		invalidate();
		return true;

	}

	//update color
	public void setColor(String newColor){
		if (drawPaint == null)
			return;
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}

	//set brush size
	public void setBrushSize(float newSize){
		if (drawPaint == null)
			return;
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				newSize, getResources().getDisplayMetrics());

		Log.i(TAG, "brushSize: " + brushSize + " -lastBrushSize: " + lastBrushSize);
		brushSize=newSize*scale_x;
		lastBrushSize=newSize*scale_x;
		drawPaint.setStrokeWidth(brushSize);
	}

	//get and set last brush size
	public void setLastBrushSize(float lastSize){
		lastBrushSize=lastSize;
	}
	public float getLastBrushSize(){
		return lastBrushSize;
	}

	//set erase true or false
	public void setErase(boolean isErase){
		erase=isErase;
		if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		else drawPaint.setXfermode(null);
	}

	//start new drawing
	public void startNew(){
		if (canvasBitmap == null)
			return;
		canvasBitmap = tempCanvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
		drawCanvas = new Canvas(canvasBitmap);

//		transparentBitmap = Bitmap.createBitmap(tempCanvasBitmap.getWidth(), tempCanvasBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//		drawTransparentCanvas = new Canvas(transparentBitmap);
//		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		invalidate();
	}
}
