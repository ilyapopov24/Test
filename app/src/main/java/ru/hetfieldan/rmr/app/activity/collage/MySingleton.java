package ru.hetfieldan.rmr.app.activity.collage;

/**
 * Created by hetfieldan24 on 12.11.2014.
 */
import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by hetfieldan24 on 11.11.2014.
 */
public class MySingleton
{
    private static MySingleton instance;

    private ArrayList<String> array = new ArrayList<String>();
    private ArrayList<Bitmap> images = new ArrayList<Bitmap>();

    private MySingleton()
    {

    }

    public ArrayList<String> getArray()
    {
        return this.array;
    }

    public void setArray(ArrayList<String> array)
    {
        this.array = array;
    }

    public ArrayList<Bitmap> getImages()
    {
        return this.images;
    }

    public void setImages(ArrayList<Bitmap> images)
    {
        this.images = images;
    }

    public static synchronized MySingleton getInstance()
    {
        if(instance == null)
        {
            instance = new MySingleton();
        }
        return instance;
    }
}