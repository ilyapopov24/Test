package ru.hetfieldan.rmr.app.activity.collage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import ru.hetfieldan.rmr.app.R;

public class NewCollageBuildActivity extends SherlockActivity
{
    Bitmap bitmap;
    File file;
    GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Создание коллажа");
        setContentView(R.layout.activity_new_collage_build);

        new MyAsyncTask(NewCollageBuildActivity.this, MySingleton.getInstance().getArray()).execute();
        gridView = (GridView) findViewById(R.id.grid_view);
    }
    public void onClick(View view)
    {
        Log.e("urls", MySingleton.getInstance().getArray().toString());

        gridView.setAdapter(new ImageAdapter(this, MySingleton.getInstance().getImages()));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_send:
                send();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getSupportMenuInflater().inflate(R.menu.collage, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void send()
    {
        bitmap = loadBitmapFromView(gridView);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/image");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.collage_email_subject));
        saveImage(bitmap);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
        startActivity(Intent.createChooser(intent, getString(R.string.collage_send_dialog_title)));
    }
    private void saveImage(Bitmap finalBitmap)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        file = new File (myDir, fname);
        if (file.exists ())
            file.delete();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static Bitmap loadBitmapFromView(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap( view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getWidth(), view.getHeight());
        view.draw(canvas);
        return bitmap;
    }
}
