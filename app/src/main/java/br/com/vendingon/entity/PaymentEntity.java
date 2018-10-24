package br.com.vendingon.entity;

public class PaymentEntity {

    private String environment;
    private String stoneCode;
    private String pinPadName;
    private String pinPadMacAddress;
    private String value;
    private int qtdInstalment;
    private boolean debit;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getStoneCode() {
        return stoneCode;
    }

    public void setStoneCode(String stoneCode) {
        this.stoneCode = stoneCode;
    }

    public String getPinPadName() {
        return pinPadName;
    }

    public void setPinPadName(String pinPadName) {
        this.pinPadName = pinPadName;
    }

    public String getPinPadMacAddress() {
        return pinPadMacAddress;
    }

    public void setPinPadMacAddress(String pinPadMacAddress) {
        this.pinPadMacAddress = pinPadMacAddress;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getQtdInstalment() {
        return qtdInstalment;
    }

    public void setQtdInstalment(int qtdInstalment) {
        this.qtdInstalment = qtdInstalment;
    }

    public boolean isDebit() {
        return debit;
    }

    public void setDebit(boolean debit) {
        this.debit = debit;
    }
}
