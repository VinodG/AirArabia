package com.winit.airarabia;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

/**
 * Created by rozina.j on 2/14/2018.
 */

public class MangageBookingWebViewActivity extends BaseActivity {

    WebView webViewManageBooking;
    LinearLayout llHome;

    @Override
    public void initilize() {

        llHome = (LinearLayout) layoutInflater.inflate(R.layout.activity_managebooking_webview, null);
        llMiddleBase.addView(llHome, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        tvHeaderTitle.setText("Flight + Hotel");
        intializeControl();
        loadUrl();
    }

    @Override
    public void bindingControl() {

    }

    private void intializeControl() {
        webViewManageBooking = (WebView) llHome.findViewById(R.id.webViewManageBooking);
    }

    private void loadUrl()
    {

//       socicalNetWebView.setWebViewClient(new MywebViewClient() );
        webViewManageBooking.setWebViewClient(new WebViewClient() );
        webViewManageBooking.getSettings().setBuiltInZoomControls(true);
        webViewManageBooking.getSettings().setJavaScriptEnabled(true);
        webViewManageBooking.getSettings().setLoadWithOverviewMode(true);
        webViewManageBooking.getSettings().setUseWideViewPort(true);
        webViewManageBooking.getSettings().setDomStorageEnabled(true);
        webViewManageBooking.loadUrl("https://holidays.airarabia.com/en/");
        webViewManageBooking.setWebViewClient(new MyWebViewClient());



    }


    class MyWebViewClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoader("Please Wait..");
        }

//        @Override
//        public boolean shouldOverrideUrlLoading(WebView webView, String url)
//        {
//            webView.loadUrl(url);
//            return false;
//        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            hideLoader();
        }


    }
}
