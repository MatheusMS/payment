package br.com.vendingon;

import android.Manifest;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import stone.application.interfaces.StoneCallbackInterface;
import stone.environment.Environment;
import stone.providers.ActiveApplicationProvider;
import stone.utils.Stone;

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

    }

    @Override
    public void sendTransaction() {
        /*
        // Cria o objeto de transacao. Usar o "Stone.getPinpadFromListAt"
        // significa que devera estar conectado com ao menos um pinpad, pois o metodo
        // cria uma lista de conectados e conecta com quem estiver na posicao "0".
        StoneTransaction stoneTransaction = new StoneTransaction(Stone.getPinpadFromListAt(0));

        // A seguir deve-se popular o objeto.
        stoneTransaction.setAmount(valueEditText.getText().toString());
        stoneTransaction.setEmailClient(null);
        stoneTransaction.setUserModel(Stone.getUserModel(0));
        stoneTransaction.setSignature(BitmapFactory.decodeResource(getResources(), R.drawable.signature));
        stoneTransaction.setCapture(captureTransactionCheckBox.isChecked());

        // AVISO IMPORTANTE: Nao e recomendado alterar o campo abaixo do
        // ITK, pois ele gera um valor unico. Contudo, caso seja
        // necessario, faca conforme a linha abaixo.
//                stoneTransaction.setInitiatorTransactionKey("SEU_IDENTIFICADOR_UNICO_AQUI");

        // Informa a quantidade de parcelas.
        stoneTransaction.setInstalmentTransactionEnum(InstalmentTransactionEnum.getAt(instalmentsSpinner.getSelectedItemPosition()));

        // Verifica a forma de pagamento selecionada.
        if (debitRadioButton.isChecked()) {
            stoneTransaction.setTypeOfTransaction(TypeOfTransactionEnum.DEBIT);
        } else {
            stoneTransaction.setTypeOfTransaction(TypeOfTransactionEnum.CREDIT);
        }
         */
    }
}
