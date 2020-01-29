package com.example.binderpool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                doBinder();
            }
        }).start();
    }

    private void doBinder() {
        BinderPoolMananger bm = BinderPoolMananger.getInstance(MainActivity.this);

        IOrder order = IOrder.Stub.asInterface(bm.query(Constans.TYPE_ORDER));
        try {
            order.doOrder(123L);
            order.cancelOrder(123L);
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.d(">>>", "order 模块出现异常，e="+e.getMessage());
        }

        IPay pay = IPay.Stub.asInterface(bm.query(Constans.TYPE_PAY));

        try {
            pay.doPay(123L,1000);
            pay.cancelPay(123L);
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.d(">>>", "pay 模块出现异常，e="+e.getMessage());
        }


        IUser user = IUser.Stub.asInterface(bm.query(Constans.TYPE_USER));

        try {
            user.login(">>>","123456");
            user.logout(">>>");
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.d(">>>", "user 模块出现异常，e="+e.getMessage());
        }
    }
}
