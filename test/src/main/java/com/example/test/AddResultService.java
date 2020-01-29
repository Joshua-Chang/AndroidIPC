package com.example.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.IAddMangerService;
import com.example.binder.IMyAidlInterface;

import androidx.annotation.Nullable;


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2020-01-27 23:44
 * @创建人：常守达
 */
public class AddResultService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(">>>","client onBind");
        return new IAddMangerService.Stub() {

            @Override
            public void add(int a) throws RemoteException {
                Log.e(">>>","client add");

            }

            @Override
            public void addResult(int a) throws RemoteException {
//                Log.e(">>>","client result");
                Log.e(">>>", "5、onTransact相应的进程解包后，相应的Service onBind执行");
                serviceStartActivity(a);
            }

        };
    }
    private void serviceStartActivity(int a) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("result",a);
        startActivity(intent);
    }
}
