package com.example.binderpool.aidl_impl;

import android.os.RemoteException;
import android.util.Log;

import com.example.binderpool.IOrder;

/**
 * Created by >>> on 2017/8/26.
 * 订单模块的实现
 */

public class IOrderImpl extends IOrder.Stub {
    @Override
    public void doOrder(long orderId) throws RemoteException {
        Log.i(">>>","IOrderImpl  doOrder, oderId="+orderId);
    }

    @Override
    public void cancelOrder(long orderId) throws RemoteException {
        Log.i(">>>","IOrderImpl  cancelOrder, oderId="+orderId);
    }
}