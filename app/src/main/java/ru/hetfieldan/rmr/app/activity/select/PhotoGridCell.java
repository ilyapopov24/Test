package ru.hetfieldan.rmr.app.activity.select;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import ru.hetfieldan.rmr.app.R;

/**
 * Created by hetfieldan24 on 15.08.2014.
 */
public class PhotoGridCell extends FrameLayout
{
    private String photoUrl;
    public PhotoGridCell(Context context)
    {
        super(context);
    }
    public PhotoGridCell(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public PhotoGridCell(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    public void setSelected(boolean selected)
    {
        super.setSelected(selected);
        Log.e("Photo selected ", String.valueOf(selected));
        findViewById(R.id.selection).setVisibility(selected ? VISIBLE : GONE);
        findViewById(R.id.photo).setVisibility(selected ? GONE : VISIBLE);
    }
    public void setSelection(boolean selected)
    {
        findViewById(R.id.selection).setVisibility(selected ? VISIBLE : GONE);
    }
    public String getPhotoUrl()
    {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl)
    {
        this.photoUrl = photoUrl;
    }




}
