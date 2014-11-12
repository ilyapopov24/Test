package ru.hetfieldan.rmr.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.hetfieldan.rmr.app.CollageApp;
import ru.hetfieldan.rmr.app.R;

/**
 * Created by hetfieldan24 on 15.08.2014.
 */
public class LoginActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.login_title);
        final WebView webView = new WebView(this);
        setContentView(webView);

        webView.setWebViewClient(
            new WebViewClient()
            {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    view.loadUrl(url);
                    return true;
                }
                @Override
                public void doUpdateVisitedHistory(WebView view, String url, boolean isReload)
                {
                    if(url.contains("#access_token"))
                    {
                        webView.setEnabled(false);
                        webView.setVisibility(View.GONE);
                        webView.getSettings().setLoadWithOverviewMode(true);
                        webView.getSettings().setUseWideViewPort(true);
                        webView.setWebChromeClient(new WebChromeClient());
                        String accessToken = url.substring(url.indexOf("#access_token") +14 );
                        ((CollageApp)getApplication()).setAccessToken(accessToken);

                        startActivity(new Intent(LoginActivity.this, SearchActivity.class));
                    }
                    else
                    {
                        super.doUpdateVisitedHistory(view, url, isReload);
                    }
                }
            });
        String clientId = getString(R.string.instagram_client_id);
        String redirectUri = getString(R.string.instagram_redirect_uri);
        String url = String.format("https://instagram.com/oauth/authorize/?client_id=%s&redirect_uri=%s&response_type=token", clientId, redirectUri);
        webView.loadUrl(url);
    }
}
