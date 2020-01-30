package in.ernadeembhat.webview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

public class UrlFragment extends AppCompatActivity {

    private AppCompatButton saveUrlButton;
    private AppCompatEditText getUrlEditText;
    private View mRootView;
    private SharedPreferences preferences;
    private CMSPreferenceManager mPrefManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urlfragemnt);
        getUrlEditText = findViewById(R.id.urlEdittext);
        saveUrlButton = findViewById(R.id.saveUrlButton);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mPrefManager = new CMSPreferenceManager(preferences);

        findViewById(R.id.urlLayout).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE

                | View.SYSTEM_UI_FLAG_FULLSCREEN

                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE

                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
                Window.PROGRESS_VISIBILITY_ON);
        getUrlEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        saveUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getUrlEditText.getText().toString();
                Log.e("URL\t",url);
                mPrefManager.saveUrl(getUrlEditText.getText().toString());
                SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = pref.edit();
                edt.putBoolean("activity_executed", true);
                edt.apply();
                startActivity(new Intent(UrlFragment.this,MainActivity.class));
            }
        });
    }


}
