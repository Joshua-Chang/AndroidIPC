package com.example.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.binderpool.aidl_impl.BinderPoolImpl;

import androidx.annotation.Nullable;

public class BinderPoolService extends Service {

    private BinderPoolImpl mBinderPoolImp = new BinderPoolImpl();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinderPoolImp;
    }
}