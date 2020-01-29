package com.example.binder.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.IAddMangerService;
import com.example.binder.IMyAidlInterface;
import com.example.binder.MainActivity;

import androidx.annotation.Nullable;


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2020-01-27 23:44
 * @创建人：常守达
 */
public class AddService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(">>>","server onBind");
        return new IAddMangerService.Stub() {

            @Override
            public void add(int a) throws RemoteException {
//                Log.e(">>>","server add");
                Log.e(">>>", "f、onTransact相应的进程解包后，相应的Service onBind执行");

                serviceStartActivity(a);
            }

            @Override
            public void addResult(int a) throws RemoteException {
                Log.e(">>>","server result");
            }
        };
//        return new IMyAidlInterface.Stub() {
//            @Override
//            public void add(int Int) throws RemoteException {
//                Log.e(">>>","server add");
//                serviceStartActivity(Int);
//            }
//
//            @Override
//            public void result(int Int) throws RemoteException {
//                Log.e(">>>","server result");
//            }
//        };
    }
    private void serviceStartActivity(int a) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("add",a);
        startActivity(intent);
    }
}
