package ru.hetfieldan.rmr.app.activity.collage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockActivity;

import java.util.ArrayList;

import ru.hetfieldan.rmr.app.R;
import ru.hetfieldan.rmr.app.activity.util.MySingleton;

/**
 * Created by hetfieldan24 on 15.08.2014.
 */

public class CollageActivity extends SherlockActivity
{
    ImageView collage;
    ProgressBar progressBar;
    private ArrayList<String> imageUrls;
    //private ArrayList<String> collageImages;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getString(R.string.collage_title));
        setContentView(R.layout.collage);

        progressBar = (ProgressBar)findViewById(R.id.progress);
        imageUrls = getIntent().getStringArrayListExtra("image_url_list");
        collage = (ImageView)findViewById(R.id.collage);
        int index = 0;
        /*collageImages = new ArrayList<String>();
        for(int i = 0; i < 25; i++)
        {
            collageImages.add(imageUrls.get(index));
            index++;
            if (index >= imageUrls.size())
                index = 0;
        }
        Collections.shuffle(collageImages);
        */
        MySingleton.getInstance().setArray(imageUrls);
        buildCollage();
    }
    @Override
    protected void onStop()
    {
        super.onStop();
    }
    private void buildCollage()
    {
        startActivity(new Intent(CollageActivity.this, NewCollageBuildActivity.class));
    }
}
