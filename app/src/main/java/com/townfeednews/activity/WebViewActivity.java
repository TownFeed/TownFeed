package com.townfeednews.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.townfeednews.R;

public class WebViewActivity extends AppCompatActivity {
    private Bundle bundle = null;
    private WebView webView;
    private ProgressBar progressBarWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.webView);
        progressBarWebView = findViewById(R.id.progressBarWebView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            bundle = this.getIntent().getExtras();
            String openUrlInWebView = bundle.getString("url");
            String toolBarTitle = bundle.getString("title");
            getSupportActionBar().setTitle(toolBarTitle);

            webView.setWebViewClient(new MyBrowser(progressBarWebView));

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.loadUrl(openUrlInWebView);
            Log.d("TAG", "onCreate: WebView " + openUrlInWebView + " Title : " + toolBarTitle);
        } catch (Exception e){
            Toast.makeText(this, "Oops! Something went wrong. Please try after sometime.", Toast.LENGTH_SHORT).show();
            this.finish();
        }

    }
    private class MyBrowser extends WebViewClient {
        private ProgressBar progressBar;

        public MyBrowser(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
