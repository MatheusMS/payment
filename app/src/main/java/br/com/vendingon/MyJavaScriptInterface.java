package br.com.vendingon;

import android.webkit.JavascriptInterface;

public class MyJavaScriptInterface {

    private CallBack mCallBack;

    public MyJavaScriptInterface(CallBack callBack) {
        this.mCallBack = callBack;
    }

    @JavascriptInterface
    public void startTransaction(String jsonPayment) {
        mCallBack.startTransaction(jsonPayment);
    }

    public interface CallBack {
        void startTransaction(String jsonPayment);
    }

}
