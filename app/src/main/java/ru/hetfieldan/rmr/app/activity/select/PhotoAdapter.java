package ru.hetfieldan.rmr.app.activity.select;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import ru.hetfieldan.rmr.app.R;


/**
 * Created by hetfieldan24 on 15.08.2014.
 */
class PhotoAdapter extends ArrayAdapter<SelectablePhoto>
{
    public PhotoAdapter(Context context, int textViewResourceId)
    {
        super(context, textViewResourceId);
    }
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if(view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.select_item, null);

        PhotoGridCell cell = (PhotoGridCell)view;
        SelectablePhoto photo = getItem(position);
        view.setTag(photo.getId());

        if(!photo.getFull().equals(cell.getPhotoUrl()))
        {
            ImageLoader.getInstance().displayImage(photo.getThumb(), (ImageView)view.findViewById(R.id.photo));
            cell.setPhotoUrl(photo.getFull());
        }
        cell.setSelection(photo.isSelected());

        return view;

    }
}
