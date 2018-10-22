package br.com.vendingon;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import permissions.dispatcher.NeedsPermission;

public class WebActivity extends AppCompatActivity implements WebContract.View {

    private WebContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        mPresenter = new WebPresenter(this);
        initiateApp();
        teste();
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void initiateApp() {

    }

    private void teste() {
        mPresenter.setEnvironment("STG");
        mPresenter.activeApplication("748892689");
        mPresenter.connectPinPad("MOBIPIN-04903158", "D4:F5:13:5D:A2:97");
        mPresenter.sendTransaction();
    }
}
