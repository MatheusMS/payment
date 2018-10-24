package br.com.vendingon;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import br.com.vendingon.entity.PaymentEntity;
import br.com.vendingon.usecase.StoneUseCase;
import br.com.vendingon.usecase.StoneUseCaseImpl;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

public class WebActivity extends AppCompatActivity implements WebContract.View {

    private ProgressBar mProgressBar;
    private WebView mWebView;
    private WebContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        //hideStatusBar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        loadUI();

        StoneUseCase stoneUseCase = new StoneUseCaseImpl(this);
        mPresenter = new WebPresenter(this, stoneUseCase);

        setupWebView();
    }

    private void loadUI() {
        mProgressBar = findViewById(R.id.progressBar);
        mWebView = findViewById(R.id.webView);
    }

    private void setupWebView() {
        mProgressBar.setProgress(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
        }

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                mProgressBar.setProgress(progress);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }
        });

        mWebView.addJavascriptInterface(new MyJavaScriptInterface(
                new MyJavaScriptInterface.CallBack() {
                    @Override
                    public void startTransaction(String jsonPayment) {
                        mPresenter.setPayment(getPayment(jsonPayment));
                        mPresenter.startTransaction();
                    }
                }
        ), "VendingonApp");

        mWebView.loadUrl("http://www.globoesporte.com");
    }

    public void onClickTransaction(View view) {
        mPresenter.setPayment(getPayment(PaymentEntity.getJsonMock()));
        mPresenter.startTransaction();
    }

    private PaymentEntity getPayment(String jsonPayment) {
        PaymentEntity payment = new Gson().fromJson(jsonPayment, PaymentEntity.class);
        return payment;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void transactionSuccessfully() {
        mWebView.evaluateJavascript("alert('Transação realizada com sucesso')", null);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
        else {
            finish();
        }
    }

    private void hideStatusBar() {
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);


    }
}
