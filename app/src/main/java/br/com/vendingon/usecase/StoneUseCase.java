package br.com.vendingon.usecase;

import br.com.vendingon.shared.ICalbackApp;
import stone.application.interfaces.StoneCallbackInterface;

public interface StoneUseCase {
    void setEnvironment(String environment);
    void activeApplication(String stoneCode, StoneCallbackInterface callbackInterface);
    void connectPinPad(String name,
                       String macAddress,
                       StoneCallbackInterface callbackInterface);
    void sendTransaction(String value,
                         int qtdInstalment,
                         boolean isDebit,
                         ICalbackApp callback);
}
