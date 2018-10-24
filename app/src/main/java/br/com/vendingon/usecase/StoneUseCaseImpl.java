package br.com.vendingon.usecase;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.vendingon.shared.ICalbackApp;
import stone.application.enums.Action;
import stone.application.enums.InstalmentTransactionEnum;
import stone.application.enums.TransactionStatusEnum;
import stone.application.enums.TypeOfTransactionEnum;
import stone.application.interfaces.StoneActionCallback;
import stone.application.interfaces.StoneCallbackInterface;
import stone.environment.Environment;
import stone.providers.ActiveApplicationProvider;
import stone.providers.BluetoothConnectionProvider;
import stone.providers.TransactionProvider;
import stone.utils.PinpadObject;
import stone.utils.Stone;
import stone.utils.StoneTransaction;

public class StoneUseCaseImpl implements StoneUseCase {

    private Context mContext;

    public StoneUseCaseImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public void setEnvironment(String environment) {
        if (environment.equals(Environment.PRODUCTION)) {
            Stone.setEnvironment(Environment.PRODUCTION);
        }
        else {
            Stone.setEnvironment(Environment.STAGING);
        }
    }

    @Override
    public void activeApplication(String stoneCode,
                                  StoneCallbackInterface callbackInterface) {
        List<String> stoneCodeList = new ArrayList<>();
        stoneCodeList.add(stoneCode);

        final ActiveApplicationProvider provider = new ActiveApplicationProvider(mContext);
        provider.setDialogMessage("Ativando o aplicativo...");
        provider.setDialogTitle("Aguarde");
        provider.useDefaultUI(false);
        provider.setConnectionCallback(callbackInterface);
        provider.activate(stoneCodeList);
    }

    @Override
    public void connectPinPad(String name,
                              String mac,
                              StoneCallbackInterface callbackInterface) {
        if (Stone.getPinpadListSize() > 0) {
            callbackInterface.onSuccess();
            return;
        }

        PinpadObject pinpadSelected = new PinpadObject(name, mac, false);

        BluetoothConnectionProvider bluetoothConnectionProvider = new BluetoothConnectionProvider(mContext, pinpadSelected);
        bluetoothConnectionProvider.setDialogMessage("Criando conexao com o pinpad selecionado");
        bluetoothConnectionProvider.useDefaultUI(false); // Informa que haverá um feedback para o usuário.
        bluetoothConnectionProvider.setConnectionCallback(callbackInterface);
        bluetoothConnectionProvider.execute();
    }

    @Override
    public void sendTransaction(String value,
                                int qtdInstalment,
                                boolean isDebit,
                                final ICalbackApp callback) {
        // Cria o objeto de transacao. Usar o "Stone.getPinpadFromListAt"
        // significa que devera estar conectado com ao menos um pinpad, pois o metodo
        // cria uma lista de conectados e conecta com quem estiver na posicao "0".
        StoneTransaction stoneTransaction = new StoneTransaction(Stone.getPinpadFromListAt(0));

        stoneTransaction.setAmount(value);
        stoneTransaction.setEmailClient(null);
        stoneTransaction.setUserModel(Stone.getUserModel(0));
        stoneTransaction.setSignature(null);
        stoneTransaction.setCapture(false);

        // AVISO IMPORTANTE: Nao e recomendado alterar o campo abaixo do
        // ITK, pois ele gera um valor unico. Contudo, caso seja
        // necessario, faca conforme a linha abaixo.
//                stoneTransaction.setInitiatorTransactionKey("SEU_IDENTIFICADOR_UNICO_AQUI");

        // Informa a quantidade de parcelas.
        stoneTransaction.setInstalmentTransactionEnum(InstalmentTransactionEnum.getAt(qtdInstalment-1));

        // Verifica a forma de pagamento selecionada.
        if (isDebit) {
            stoneTransaction.setTypeOfTransaction(TypeOfTransactionEnum.DEBIT);
        } else {
            stoneTransaction.setTypeOfTransaction(TypeOfTransactionEnum.CREDIT);
        }

        // Processo para envio da transacao.
        final TransactionProvider provider = new TransactionProvider(mContext, stoneTransaction, Stone.getPinpadFromListAt(0));
        provider.useDefaultUI(false);
        provider.setDialogMessage("Enviando..");
        provider.setDialogTitle("Aguarde");
        provider.setConnectionCallback(new StoneActionCallback() {
            @Override
            public void onStatusChanged(Action action) {
                Log.d("TRANSACTION_STATUS", action.name());
            }
            public void onSuccess() {
                if (provider.getTransactionStatus() == TransactionStatusEnum.APPROVED) {
                    callback.onSucess();
                } else {
                    callback.onError("Erro na transação: \"" + provider.getMessageFromAuthorize() + "\"");
                }
            }
            public void onError() {
                callback.onError("Erro na transação");
            }
        });
        provider.execute();
    }
}
