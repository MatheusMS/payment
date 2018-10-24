package br.com.vendingon;

import android.content.Context;
import android.support.annotation.NonNull;

import br.com.vendingon.entity.PaymentEntity;

public interface WebContract {

    interface View {
        Context getContext();
        void showToast(String message);
    }

    interface Presenter {
        void setPayment(@NonNull PaymentEntity payment);
        void startTransaction();
    }
}
