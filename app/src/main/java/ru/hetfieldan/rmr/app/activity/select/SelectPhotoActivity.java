package ru.hetfieldan.rmr.app.activity.select;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.jinstagram.Instagram;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

import java.util.ArrayList;

import ru.hetfieldan.rmr.app.CollageApp;
import ru.hetfieldan.rmr.app.R;
import ru.hetfieldan.rmr.app.activity.collage.CollageActivity;

/**
 * Created by hetfieldan24 on 15.08.2014.
 */
public class SelectPhotoActivity extends SherlockActivity
{
    private  final static String IMAGE_LOADED_EVENT = "image_loaded";

    private Long userId;
    private Instagram instagram;
    private ArrayAdapter<SelectablePhoto> adapter;
    private int selectedCount;
    private int loadingCount;
    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        userId = getIntent().getLongExtra("user_id", -1);
        instagram = ((CollageApp)getApplication()).getInstagram();
        getSupportActionBar().setTitle(getString(R.string.select_title));

        setContentView(R.layout.select);
        grid = (GridView)findViewById(R.id.grid);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                SelectablePhoto item = adapter.getItem(position);
                item.invertSelection();
                ((PhotoGridCell)grid.findViewWithTag(item.getId())).setSelection(item.isSelected());

                selectedCount += item.isSelected() ? 1 : -1;
                supportInvalidateOptionsMenu();
                if(item.isSelected())
                    ImageLoader.getInstance().loadImage(item.getFull(), new PhotoLoadingListener());
            }
        });

        adapter = new PhotoAdapter(this, 0);
        grid.setAdapter(adapter);
        loadPhotos();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getSupportMenuInflater().inflate(R.menu.select, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.findItem(R.id.collage).setVisible(selectedCount > 0);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.collage:
                if(loadingCount == 0)
                    openCollageActivity();
                else
                    openWaitingDialog();
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }
    private void loadPhotos()
    {
        new AsyncTask<Void, Void, MediaFeed>()
        {
            @Override
            protected MediaFeed doInBackground(Void... params)
            {
                try
                {
                    return instagram.getRecentMediaFeed(userId);
                }
                catch (InstagramException e)
                {
                    Log.e("Error loading recent media", e.toString());
                    Toast.makeText(SelectPhotoActivity.this, getString(R.string.select_error_load), Toast.LENGTH_LONG).show();
                }
                return null;
            }
            @Override
            protected void onPostExecute(MediaFeed feed)
            {
                if(feed != null)
                {
                    for(MediaFeedData data : feed.getData())
                        if("image".equals(data.getType()))
                            adapter.add(new SelectablePhoto(data.getImages(), data.getId()));
                }
            }
        }.execute();
    }
    private void openCollageActivity()
    {
        ArrayList<String> urls = new ArrayList<String>();
        for(int i = 0; i < adapter.getCount(); i++)
        {
            SelectablePhoto images = adapter.getItem(i);
            if(images.isSelected())
                urls.add(images.getFull());
        }
        Intent intent = new Intent(SelectPhotoActivity.this, CollageActivity.class);
        intent.putStringArrayListExtra("image_url_list", urls);
        startActivity(intent);
    }
    private void openWaitingDialog()
    {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(getString(R.string.select_dialog_title));
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.setMax(selectedCount);
        dialog.setProgress(selectedCount - loadingCount);
        dialog.show();

        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                dialog.setProgress(selectedCount - loadingCount);
                 if(loadingCount == 0)
                 {
                     dialog.dismiss();
                     unregisterReceiver(this);
                     openCollageActivity();
                 }
            }
        }, new IntentFilter(IMAGE_LOADED_EVENT));
    }
    private class PhotoLoadingListener implements ImageLoadingListener
    {
        @Override
        public void onLoadingStarted(String imageUri, View view)
        {
            loadingCount++;
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason)
        {
            loadingCount--;
            sendEvent();
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
        {
            loadingCount--;
            sendEvent();
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view)
        {
            loadingCount--;
            sendEvent();
        }
        private void sendEvent()
        {
            sendBroadcast(new Intent(IMAGE_LOADED_EVENT));
        }
    }
}
