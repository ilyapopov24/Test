package ru.hetfieldan.rmr.app;

import android.app.Application;
import android.preference.PreferenceManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;


public class CollageApp extends Application
{
    public static final String ACCESS_TOKEN = "instagram_access_token";
    private Instagram instagram;
    @Override
    public void onCreate()
    {
        super.onCreate();
        initInstagram();
        initImageLoader();
    }

    private void initImageLoader()
    {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCacheSize(1024 * 1024 * 512)
                .defaultDisplayImageOptions(options)
                .build();

        ImageLoader.getInstance().init(config);

    }

    public void setAccessToken(String accessToken)
    {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(ACCESS_TOKEN, accessToken)
                .commit();
        initInstagram();
    }
    public Instagram getInstagram()
    {
        return instagram;
    }
    private void initInstagram()
    {
        String clientId = getString(R.string.instagram_client_id);
        String clientSecret = getString(R.string.instagram_client_secret);
        String accessToken = PreferenceManager.getDefaultSharedPreferences(this).getString(ACCESS_TOKEN, null);

        if (accessToken == null)
            return;
        instagram = new Instagram(clientId);
        instagram.setAccessToken(new Token(accessToken, clientSecret));
    }
}
