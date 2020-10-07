package com.applex.utsav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.applex.utsav.preferences.IntroPref;

import java.util.Locale;
import java.util.Objects;

public class Webview extends AppCompatActivity {

    private ProgressBar progressBar;
    private android.webkit.WebView wv;
    IntroPref introPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        introPref = new IntroPref(this);
        String lang= introPref.getLanguage();
        Locale locale= new Locale(lang);
        Locale.setDefault(locale);
        Configuration config= new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_web_view);

        Toolbar tb = findViewById(R.id.toolb);
        tb.setTitle("Utsav");
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        progressBar = findViewById(R.id.progressBar1);

        wv = findViewById(R.id.webview) ;
        wv.setVisibility(View.INVISIBLE);
        wv.setWebChromeClient(new WebChromeClient());

        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);

        wv.setWebViewClient(new WebViewClient() {
            public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(Webview.this,"Loading...",Toast.LENGTH_SHORT).show();
            }

            public void onPageFinished(android.webkit.WebView view, String url) {
                super.onPageFinished(view, url);
                wv.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

        Intent i = getIntent();
        final String text = i.getStringExtra("text");

        ///////////bool=1 for Contact us, 2 for Privacy Policy and 3 for Translate/////////////
        if(getIntent().getStringExtra("bool")!=null && Objects.requireNonNull(getIntent().getStringExtra("bool")).matches("1")) {
            wv.loadUrl(text);
            tb.setTitle("applex.in");
        }
        else if(getIntent().getStringExtra("bool")!=null && Objects.requireNonNull(getIntent().getStringExtra("bool")).matches("2")) {
            wv.loadUrl(text);
            tb.setTitle("applex.in");
        }
        else if(getIntent().getStringExtra("option") != null){
            String option = getIntent().getStringExtra("option");
            if(option.matches("1")){

            }
            else if(option.matches("2")){
                wv.loadUrl(getIntent().getStringExtra("Link"));
                tb.setTitle("Utsav");
            }
            else if(option.matches("3")){

            }
        }
//        else if(getIntent().getStringExtra("bool")!=null && Objects.requireNonNull(getIntent().getStringExtra("bool")).matches("3")){
//            tb.setTitle("Translate");
//            wv.loadUrl("https://translate.google.com/#auto/hi/" + text);
//        }
        ///////////bool=1 for Contact us, 2 for Privacy Policy and 3 for Translate/////////////


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id==android.R.id.home)
            super.onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}