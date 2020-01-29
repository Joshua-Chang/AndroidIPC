package com.example.binderpool.aidl_impl;

import android.os.RemoteException;
import android.util.Log;

import com.example.binderpool.IPay;

/**
 * Created by >>> on 2017/8/26.
 * 支付模块的实现
 */

public class IPayImpl extends IPay.Stub  {
    @Override
    public void doPay(long payId, int amount) throws RemoteException {
        Log.i(">>>","IPayImpl  doPay, payId="+payId+",amount="+amount);
    }

    @Override
    public void cancelPay(long payId) throws RemoteException {
        Log.i(">>>","IPayImpl  doPay, payId="+payId);
    }
}