package br.com.vendingon;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import br.com.vendingon.entity.PaymentEntity;
import br.com.vendingon.usecase.StoneUseCase;
import br.com.vendingon.usecase.StoneUseCaseImpl;

public class WebActivity extends AppCompatActivity implements WebContract.View {

    private WebContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        StoneUseCase stoneUseCase = new StoneUseCaseImpl(this);
        mPresenter = new WebPresenter(this, stoneUseCase);
    }

    public void onClickTransaction(View view) {
        mPresenter.setPayment(getPayment());
        mPresenter.startTransaction();
    }

    private PaymentEntity getPayment() {
        String jsonPayment = "{\n" +
                "\t\"environment\": \"STAGING\",\n" +
                "\t\"stoneCode\": \"748892689\",\n" +
                "\t\"pinPadName\": \"MOBIPIN-04903158\",\n" +
                "\t\"pinPadMacAddress\": \"D4:F5:13:5D:A2:97\",\n" +
                "\t\"value\": \"1,00\",\n" +
                "\t\"qtdInstalment\": 1,\n" +
                "\t\"debit\": true\n" +
                "}";
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
}
