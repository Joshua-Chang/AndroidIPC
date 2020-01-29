// IPay.aidl
package com.example.binderpool;

// Declare any non-default types here with import statements
interface IPay {
    void doPay(long payId,int amount);
    void cancelPay(long payId);
}
