package in.ernadeembhat.webview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    // set your custom url here
    String url = null;

    // if you want to show progress bar on splash screen
    //Boolean showProgressOnSplashScreen = true;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AppCompatEditText urlText;
    private AppCompatButton saveUrl;
    private SharedPreferences preferences;
    private CMSPreferenceManager mPrefManager;


    WebView mWebView;
    ProgressBar prgs;
    AppCompatTextView textView;
    RelativeLayout splash, main_layout;

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mPrefManager = new CMSPreferenceManager(preferences);
        url = mPrefManager.getServerUrl();
        Log.e("WEB URL\t", "--\t" + url);
        drawerLayout = findViewById(R.id.drawerlayout);
        mWebView = findViewById(R.id.wv);
        navigationView = findViewById(R.id.navigationview);
        prgs = findViewById(R.id.progressBar);
        textView = findViewById(R.id.text);
        main_layout = findViewById(R.id.main_layout);


        urlText = navigationView.getHeaderView(0).findViewById(R.id.urlEdit);
        urlText.setText(mPrefManager.getServerUrl());
        saveUrl = navigationView.getHeaderView(0).findViewById(R.id.submit);
        navigationView.setNavigationItemSelectedListener(this);


        saveUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.clearCache(true);
                mWebView.loadUrl(urlText.getText().toString());
                mPrefManager.saveUrl(urlText.getText().toString());
                drawerLayout.closeDrawers();
                //hideSystemUI();
                findViewById(R.id.main_layout).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE

                        | View.SYSTEM_UI_FLAG_FULLSCREEN

                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE

                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                hideKeyboard(MainActivity.this);
            }
        });

        findViewById(R.id.main_layout).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE

                | View.SYSTEM_UI_FLAG_FULLSCREEN

                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE

                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
                Window.PROGRESS_VISIBILITY_ON);

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                final DrawerLayout drawer = findViewById(R.id.drawerlayout);
                drawer.openDrawer(GravityCompat.START);
                return true;
            }

        });


        mWebView.loadUrl(url);
        // control javaScript and add html5 features
        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDatabasePath(
                this.getFilesDir().getPath() + this.getPackageName()
                        + "/databases/");

        // this force use chromeWebClient
        mWebView.getSettings().setSupportMultipleWindows(true);
        Log.i("WebViewActivity", "UA: " + mWebView.getSettings().getUserAgentString());
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }


            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                //handler.proceed();
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                String message = "SSL Certificate error.";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The certificate Hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;
                }
                message += " Do you want to continue anyway?";

                builder.setTitle("SSL Certificate Error");
                builder.setMessage(message);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (prgs.getVisibility() == View.GONE) {
                    prgs.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (prgs.getVisibility() == View.VISIBLE)
                    prgs.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
            }

        });

    }

    /**
     * To animate view slide out from top to bottom
     *
    // * @param view
     */
    // void slideToBottom(View view) {
    // TranslateAnimation animate = new TranslateAnimation(0, 0, 0,
    // view.getHeight());
    // animate.setDuration(2000);
    // animate.setFillAfter(true);
    // view.startAnimation(animate);
    // view.setVisibility(View.GONE);
    // }
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    public static void hideKeyboard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((Activity) mContext).getWindow()
                .getCurrentFocus().getWindowToken(), 0);
    }

  public String  getWebviewVersionInfo() {
        // Overridden UA string
        String alreadySetUA = mWebView.getSettings().getUserAgentString();

        // Next call to getUserAgentString() will get us the default
        mWebView.getSettings().setUserAgentString(null);

        // Devise a method for parsing the UA string
        String webViewVersion = mWebView.getSettings().getUserAgentString();

        // Revert to overriden UA string
        mWebView.getSettings().setUserAgentString(alreadySetUA);

        return webViewVersion;
    }
}
