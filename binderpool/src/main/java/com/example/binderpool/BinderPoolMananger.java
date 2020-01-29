package com.example.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

public class BinderPoolMananger {


    private Context mContext;
    private static BinderPoolMananger sInstance;
    private IBinderPool mBinderPool;
    private CountDownLatch mCountDownLatch;

    private BinderPoolMananger(Context context){
        this.mContext = context;
        bindBinderPoolService();
    }

    public static BinderPoolMananger getInstance(Context context){
        if(sInstance == null){
            sInstance = new BinderPoolMananger(context);
        }
        return sInstance;
    }


    private ServiceConnection mServiceConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(">>>", "onServiceConnected() 被调用了");
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                //死亡通知
                mBinderPool.asBinder().linkToDeath(new IBinder.DeathRecipient() {
                    @Override
                    public void binderDied() {
                        mBinderPool.asBinder().unlinkToDeath(this, 0);
                        mBinderPool = null;
                        bindBinderPoolService();
                    }
                }, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void bindBinderPoolService(){
        Log.d(">>>", "bindService()  被调用");
        mCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext,BinderPoolService.class);
        Log.d(">>>", "开始 bind service");
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(">>>", "finish to bind service");
    }


    public IBinder query(int code){
        Log.d(">>>", "query() 被调用, code = " + code);
        IBinder binder = null;
        try {
            binder = mBinderPool.queryBinder(code);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }
}