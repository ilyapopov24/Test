package ru.hetfieldan.rmr.app.activity.collage;

/**
 * Created by hetfieldan24 on 12.11.2014.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by hetfieldan24 on 12.11.2014.
 */
class MyAsyncTask extends AsyncTask<Void,Void,Void>
{
    ArrayList<Bitmap> images = new ArrayList<Bitmap>();
    Bitmap image;
    ProgressDialog pd;
    ArrayList<String> urls = new ArrayList<String>();
    Context context;

    public MyAsyncTask(Context context, ArrayList<String> urls)
    {
        this.urls = urls;
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setMessage("Loading..");
        pd.show();
    }


    @Override
    protected Void doInBackground(Void... params) {
        // TODO Auto-generated method stub
        try
        {
            for (String url : urls)
            {
                images.add(downloadBitmap(url));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        MySingleton.getInstance().setImages(images);
        pd.dismiss();
    }
    private Bitmap downloadBitmap(String url) {
        // initilize the default HTTP client object
        final DefaultHttpClient client = new DefaultHttpClient();

        //forming a HttoGet request
        final HttpGet getRequest = new HttpGet(url);
        try {

            HttpResponse response = client.execute(getRequest);

            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;

            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    // getting contents from the stream
                    inputStream = entity.getContent();

                    // decoding stream data back into image Bitmap that android understands
                    image = BitmapFactory.decodeStream(inputStream);

                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // You Could provide a more explicit error message for IOException
            getRequest.abort();
            Log.e("ImageDownloader", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
        }

        return image;
    }
}