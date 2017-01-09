package com.ifd.androidbarcodereader.model;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifd.androidbarcodereader.R;
import com.ifd.androidbarcodereader.activities.DetailPdfActivity;
import com.ifd.androidbarcodereader.activities.ListPdfActivity;

import java.util.List;


/**
 * Created by LenVo on 2/28/16.
 */
public class CustomAdapterList extends BaseAdapter {
    private Context context;
    private List<PdfDocument> list_document;
    private static LayoutInflater inflater=null;
    public CustomAdapterList(ListPdfActivity mainActivity, List<PdfDocument> list_document) {
        // TODO Auto-generated constructor stub
        this.list_document = list_document;
        context=mainActivity;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list_document.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        TextView tv2;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.list_pdf_document, null);
        holder.tv =(TextView) rowView.findViewById(R.id.textView1);
        holder.tv2 =(TextView) rowView.findViewById(R.id.textView2);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

//        LinearLayout.LayoutParams img_parms = new LinearLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        img_parms.height =  img_parms.width = Constant.width_device/10;
//        holder.img.setScaleType(ImageView.ScaleType.FIT_XY);
//        holder.img.setLayoutParams(img_parms);

//        if(Constant.width_device > 600)
//        {
//            holder.tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
//        }
        holder.tv.setText(list_document.get(position).getFileName());
        holder.tv2.setTypeface(holder.tv2.getTypeface(), Typeface.BOLD);
        holder.tv2.setText("Archive: " + list_document.get(position).getArchiveName());

//        holder.tv.setText(String.format("%02d",position + 1) + ". " + list_document.get(position).getFileName());
//        int img_id = context.getResources().getIdentifier(list_document.get(position).getImage_file(), "drawable", context.getPackageName());
//        holder.img.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), R.drawable.ic_launcher, 1440, 2560));

        //TODO: It is old way to drawing. need to open bellow code to get the PDF file from server.
//        rowView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, SignPdfActivity.class);
//                intent.putExtra("archiveName", list_document.get(position).getArchiveName());
//                intent.putExtra("fileName", list_document.get(position).getFileName() + ".PDF");
//                context.startActivity(intent);
//                Toast.makeText(context, "Clicked on " + list_document.get(position).getFileName(), Toast.LENGTH_LONG).show();
//
//            }
//        });

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailPdfActivity.class);
                intent.putExtra("archiveName", list_document.get(position).getArchiveName());
                intent.putExtra("fileName", list_document.get(position).getFileName() + ".PDF");
                context.startActivity(intent);
//                Toast.makeText(context, "Clicked on " + list_document.get(position).getFileName(), Toast.LENGTH_LONG).show();

            }
        });

        return rowView;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inSampleSize = 4;
        options.inDither=false;                     //Disable Dithering mode
        options.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
        return BitmapFactory.decodeResource(res, resId, options);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
