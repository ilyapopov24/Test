package ru.hetfieldan.rmr.app.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

import org.jinstagram.Instagram;
import org.jinstagram.entity.users.feed.UserFeed;
import org.jinstagram.exceptions.InstagramException;

import ru.hetfieldan.rmr.app.CollageApp;
import ru.hetfieldan.rmr.app.R;
import ru.hetfieldan.rmr.app.activity.select.SelectPhotoActivity;


/**
 * Created by hetfieldan24 on 15.08.2014.
 */
public class SearchActivity extends SherlockActivity
{
    private Instagram instagram;

    EditText textField;
    Button findButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String accessToken = PreferenceManager.getDefaultSharedPreferences(this).getString(CollageApp.ACCESS_TOKEN, null);
        if(accessToken == null)
        {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        getSupportActionBar().setTitle(getString(R.string.search_title));

        setContentView(R.layout.search);
        textField = (EditText)findViewById(R.id.editText);
        findButton = (Button)findViewById(R.id.button);
        instagram = ((CollageApp)getApplication()).getInstagram();
    }

    public void searchUserButton(View view)
    {
        new AsyncTask<String, Void, UserFeed>()
        {
            @Override
            protected void onPreExecute()
            {
                findButton.setEnabled(false);
                textField.setEnabled(false);
            }
            @Override
            protected UserFeed doInBackground(String... params)
            {
                try
                {
                    return instagram.searchUser(params[0]);
                }
                catch(InstagramException e)
                {
                    Log.e("Error while searching user", e.toString());
                    Toast.makeText(SearchActivity.this, getString(R.string.search_error_instagram), Toast.LENGTH_LONG).show();
                }
                return null;
            }
            @Override
            protected void onPostExecute(UserFeed feed)
            {
                findButton.setEnabled(true);
                textField.setEnabled(true);
                if(feed != null)
                {
                    if (feed.getUserList().isEmpty())
                        Toast.makeText(SearchActivity.this, getString(R.string.search_error_not_found), Toast.LENGTH_LONG).show();
                    else
                    {
                        Intent intent = new Intent(SearchActivity.this, SelectPhotoActivity.class);
                        intent.putExtra("user_id", feed.getUserList().get(0).getId());
                        startActivity(intent);
                    }
                }
            }
        }.execute(textField.getText().toString());
    }
}
