package com.bakatumu.bakatumu.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bakatumu.bakatumu.R;
import com.bakatumu.bakatumu.app.MyApplication;

public class AerActivity extends AppCompatActivity {

    private WebView mWebView;
    private String appUrl = "http://103.240.110.254/aer/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (MyApplication.getInstance().getPrefManager().getUser() == null) {
            launchLoginActivity();
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aer);

        // cara pendek
        mWebView = (WebView) findViewById(R.id.activity_aer_webview);

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // Force links and redirects to open in the WebView instead of in a browser

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                view.loadUrl("file:///android_asset/www/error_handler.html");
            }
        });

        mWebView.loadUrl(appUrl);

        final ProgressBar myProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        final Activity MyActivity = this;

        MyActivity.setTitle(R.string.app_title);

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Make the bar visible
                MyActivity.setTitle("Loading...");
                myProgressBar.setVisibility(View.VISIBLE);

                // Return the bar to invisible state
                if (progress == 100) {
                    myProgressBar.setVisibility(View.INVISIBLE);
                    MyActivity.setTitle(R.string.app_title);
                }
            }

            // Ask permission to access location
            public void onGeolocationPermissionsShowPrompt(
                    String origin,
                    GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_logout:
                MyApplication.getInstance().logout();
                break;

            case R.id.action_user:
                MyApplication.getInstance().userActivity();
                break;

            case R.id.action_chat_room:
                MyApplication.getInstance().mainActivity();
                break;

            case R.id.action_aer:
                MyApplication.getInstance().aerActivity();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(AerActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /*
    // Tombol Back
    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    */
}
