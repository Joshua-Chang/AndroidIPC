package com.example.binderpool.aidl_impl;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.binderpool.Constans;
import com.example.binderpool.IBinderPool;

/**
 * Created by >>> on 2017/8/26.
 * 连接池
 */

public class BinderPoolImpl extends IBinderPool.Stub {

    /**
     * query  方法就是根据请求功能模块的代码，返回相应模块的实现
     * @param binderCode
     * @return
     * @throws RemoteException
     */
    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        Log.d(">>>", "BinderPoolImpl queryBinder  binderCode="+binderCode);
        switch (binderCode){
            case Constans.TYPE_ORDER:
                return new IOrderImpl();
            case Constans.TYPE_PAY:
                return new IPayImpl();
            case Constans.TYPE_USER:
                return new IUserImpl();
        }
        return null;
    }
}