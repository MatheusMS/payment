package br.com.vendingon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import stone.application.StoneStart;
import stone.user.UserModel;

public class WebActivity extends AppCompatActivity implements WebContract.View {

    private WebContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        mPresenter = new WebPresenter(this);
        initiateStoneSDK();
    }

    private void initiateStoneSDK() {
        /**
         * Este deve ser, obrigatoriamente, o primeiro metodo
         * a ser chamado. E um metodo que trabalha com sessao.
         */
        List<UserModel> user = StoneStart.init(this);

        // se retornar nulo, voce provavelmente nao ativou a SDK
        // ou as informacoes da Stone SDK foram excluidas
        if (user != null) {
            /* caso ja tenha as informacoes da SDK e chamado o ActiveApplicationProvider anteriormente
               sua aplicacao podera seguir o fluxo normal */

        }
        teste();
    }

    private void teste() {
        mPresenter.setEnvironment("STG");
        mPresenter.activeApplication("748892689");
//        mPresenter.connectPinPad("MOBIPIN-04903158", "D4:F5:13:5D:A2:97");
//        mPresenter.sendTransaction();
    }
}
