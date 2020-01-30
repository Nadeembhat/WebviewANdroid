package in.ernadeembhat.webview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



public class Splash extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.

        setContentView(R.layout.splash);
        findViewById(R.id.spalshlayout).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE

                | View.SYSTEM_UI_FLAG_FULLSCREEN

                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE

                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
                Window.PROGRESS_VISIBILITY_ON);
        final SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (pref.getBoolean("activity_executed", false)) {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Splash.this, UrlFragment.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_SCREEN_TIME_OUT);


    }
}
