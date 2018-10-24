package br.com.vendingon;

import android.support.annotation.NonNull;

import java.util.List;

import br.com.vendingon.entity.PaymentEntity;
import br.com.vendingon.shared.ICalbackApp;
import br.com.vendingon.usecase.StoneUseCase;
import stone.application.StoneStart;
import stone.application.interfaces.StoneCallbackInterface;
import stone.user.UserModel;

public class WebPresenter implements WebContract.Presenter {

    private WebContract.View mView;
    private List<UserModel> mUserStone;
    private StoneUseCase mStoneUseCase;
    private PaymentEntity mPayment;

    public WebPresenter(WebContract.View view,
                        StoneUseCase stoneUseCase) {
        mView = view;
        mStoneUseCase = stoneUseCase;
    }

    @Override
    public void setPayment(@NonNull PaymentEntity payment) {
        this.mPayment = payment;
    }

    @Override
    public void startTransaction() {
        mStoneUseCase.setEnvironment(mPayment.getEnvironment());

        // se retornar nulo, voce provavelmente nao ativou a SDK
        // ou as informacoes da Stone SDK foram excluidas
        mUserStone = StoneStart.init(mView.getContext());

        // se retornar nulo, voce provavelmente nao ativou a SDK
        // ou as informacoes da Stone SDK foram excluidas
        if (mUserStone != null) {
            /* caso ja tenha as informacoes da SDK e chamado o ActiveApplicationProvider anteriormente
               sua aplicacao podera seguir o fluxo normal */
            connectPinPad();
        }
        else {
            activeStoneApplication();
        }
    }

    private void activeStoneApplication() {
        mStoneUseCase.activeApplication(mPayment.getStoneCode(),
                new StoneCallbackInterface() {
                    @Override
                    public void onSuccess() {
                        connectPinPad();
                    }

                    @Override
                    public void onError() {
                        mView.showToast("Erro na ativacao do aplicativo Stone");
                    }
                });
    }

    private void connectPinPad() {
        mStoneUseCase.connectPinPad(mPayment.getPinPadName(),
                mPayment.getPinPadMacAddress(),
                new StoneCallbackInterface() {
                    @Override
                    public void onSuccess() {
                        sendTransaction();
                    }

                    @Override
                    public void onError() {
                        mView.showToast("Erro durante a conexao com o PinPad");
                    }
                });
    }

    private void sendTransaction() {
        mStoneUseCase.sendTransaction(mPayment.getValue(),
                mPayment.getQtdInstalment(),
                mPayment.isDebit(),
                new ICalbackApp() {
                    @Override
                    public void onSucess() {
                        mView.showToast("Transação realizada com sucesso");
                    }

                    @Override
                    public void onError(String messageError) {
                        mView.showToast(messageError);
                    }
                });
    }
}
