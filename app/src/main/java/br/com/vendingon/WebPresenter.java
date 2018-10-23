package br.com.vendingon;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

public class WebPresenter implements WebContract.Presenter {

    private WebContract.View mView;

    public WebPresenter(WebContract.View view) {
        mView = view;
    }

    @Override
    public void setEnvironment(String environment) {
        Stone.setEnvironment(Environment.STAGING);
    }

    @Override
    public void activeApplication(String stoneCode) {
        List<String> stoneCodeList = new ArrayList<>();
        // Adicione seu Stonecode abaixo, como string.
        stoneCodeList.add(stoneCode);

        final Context context = (Context) mView;

        final ActiveApplicationProvider provider = new ActiveApplicationProvider(context);
        provider.setDialogMessage("Ativando o aplicativo...");
        provider.setDialogTitle("Aguarde");
        provider.useDefaultUI(false);
        provider.setConnectionCallback(new StoneCallbackInterface() {
            /* Metodo chamado se for executado sem erros */
            public void onSuccess() {
                Toast.makeText(context, "Ativado com sucesso, iniciando o aplicativo", Toast.LENGTH_SHORT).show();
                //continueApplication();
                connectPinPad("MOBIPIN-04903158", "D4:F5:13:5D:A2:97");
            }

            /* metodo chamado caso ocorra alguma excecao */
            public void onError() {
                Toast.makeText(context, "Erro na ativacao do aplicativo, verifique a lista de erros do provider", Toast.LENGTH_SHORT).show();

                /* Chame o metodo abaixo para verificar a lista de erros. Para mais detalhes, leia a documentacao: */
                Log.e("TESTE", "onError: " + provider.getListOfErrors().toString());

            }
        });
        provider.activate(stoneCodeList);
    }

    @Override
    public void connectPinPad(String name, String mac) {
        final Context context = (Context) mView;

        // Pega o pinpad selecionado do ListView.
        PinpadObject pinpadSelected = new PinpadObject(name, mac, false);

        // Passa o pinpad selecionado para o provider de conexão bluetooth.
        BluetoothConnectionProvider bluetoothConnectionProvider = new BluetoothConnectionProvider(context, pinpadSelected);
        bluetoothConnectionProvider.setDialogMessage("Criando conexao com o pinpad selecionado"); // Mensagem exibida do dialog.
        bluetoothConnectionProvider.useDefaultUI(false); // Informa que haverá um feedback para o usuário.
        bluetoothConnectionProvider.setConnectionCallback(new StoneCallbackInterface() {

            public void onSuccess() {
                Toast.makeText(context, "Pinpad conectado", Toast.LENGTH_SHORT).show();
//                btConnected = true;
//                finish();
                sendTransaction();
            }

            public void onError() {
                Toast.makeText(context, "Erro durante a conexao. Verifique a lista de erros do provider para mais informacoes", Toast.LENGTH_SHORT).show();
            }
        });
        bluetoothConnectionProvider.execute(); // Executa o provider de conexão bluetooth.
    }

    @Override
    public void sendTransaction() {
        // Cria o objeto de transacao. Usar o "Stone.getPinpadFromListAt"
        // significa que devera estar conectado com ao menos um pinpad, pois o metodo
        // cria uma lista de conectados e conecta com quem estiver na posicao "0".
        StoneTransaction stoneTransaction = new StoneTransaction(Stone.getPinpadFromListAt(0));

        // A seguir deve-se popular o objeto.
        stoneTransaction.setAmount("10,00");
        stoneTransaction.setEmailClient(null);
        stoneTransaction.setUserModel(Stone.getUserModel(0));
        stoneTransaction.setSignature(null);//BitmapFactory.decodeResource(getResources(), R.drawable.signature));
        stoneTransaction.setCapture(false);//captureTransactionCheckBox.isChecked());

        // AVISO IMPORTANTE: Nao e recomendado alterar o campo abaixo do
        // ITK, pois ele gera um valor unico. Contudo, caso seja
        // necessario, faca conforme a linha abaixo.
//                stoneTransaction.setInitiatorTransactionKey("SEU_IDENTIFICADOR_UNICO_AQUI");

        // Informa a quantidade de parcelas.
        stoneTransaction.setInstalmentTransactionEnum(InstalmentTransactionEnum.getAt(0));

        // Verifica a forma de pagamento selecionada.
//        if (debitRadioButton.isChecked()) {
            stoneTransaction.setTypeOfTransaction(TypeOfTransactionEnum.DEBIT);
//        } else {
//            stoneTransaction.setTypeOfTransaction(TypeOfTransactionEnum.CREDIT);
//        }

        final Context context = (Context) mView;

        // Processo para envio da transacao.
        final TransactionProvider provider = new TransactionProvider(context, stoneTransaction, Stone.getPinpadFromListAt(0));
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
                    Toast.makeText(context, "Transação enviada com sucesso e salva no banco. Para acessar, use o TransactionDAO.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Erro na transação: \"" + provider.getMessageFromAuthorize() + "\"", Toast.LENGTH_LONG).show();
                }
//                finish();
            }

            public void onError() {
                Toast.makeText(context, "Erro na transação", Toast.LENGTH_SHORT).show();
            }
        });
        provider.execute();
    }
}
