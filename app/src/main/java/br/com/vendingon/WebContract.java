package br.com.vendingon;

public interface WebContract {

    interface View {

    }

    interface Presenter {
        void setEnvironment(String environment);
        void activeApplication(String stoneCode);
        void connectPinPad(String name, String mac);
        void sendTransaction();
    }
}
