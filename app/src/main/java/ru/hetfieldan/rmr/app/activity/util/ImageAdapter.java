package ru.hetfieldan.rmr.app.activity.util;

/**
 * Created by hetfieldan24 on 12.11.2014.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Bitmap> images;

    // Constructor
    public ImageAdapter(Context c, ArrayList<Bitmap> images) {
        mContext = c;
        this.images = images;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return images.size(); // длина
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView(mContext);

        imageView.setImageBitmap(images.get(position));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(120, 110));

        return imageView;
    }
}