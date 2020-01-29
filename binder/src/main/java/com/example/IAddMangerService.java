package com.example;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.example.binder.BuildConfig.TAG;


/**
 * 定义接口服务
 * 服务端具备功能提供给客户端，定义一个接口继承IInterface
 */
public interface IAddMangerService extends IInterface {
    public static final String DESCRIPTOR = "com.example.add";

//    public static final String DESCRIPTOR = "com.example.binder.add";

    public static abstract class Stub extends Binder implements IAddMangerService {

        static final int TRANSACTION_add = FIRST_CALL_TRANSACTION + 0;
        static final int TRANSACTION_add_result = FIRST_CALL_TRANSACTION + 1;

        @Override
        public IBinder asBinder() {
            return this;
        }

        public static IAddMangerService asInterface(IBinder obj){
            Log.e(TAG, "2、asInterface判断是否在同一个进程，不是调用代理");
            if (obj == null) {
                return null;
            }
            IInterface iInterface = obj.queryLocalInterface(DESCRIPTOR);
            if ((iInterface != null) &&iInterface instanceof IAddMangerService){
                return (IAddMangerService) iInterface;
            }
            return new Proxy(obj);
        }
        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION:
                    reply.writeString(DESCRIPTOR);
                    return true;
//                case TRANSACTION_add:
//                    data.enforceInterface(DESCRIPTOR);
//                    int _arg0;
//                    _arg0 = data.readInt();
//                    reply.writeNoException();
//                    reply.writeInt(this.add(_arg0));
//                    return true;
                    case TRANSACTION_add:
                    data.enforceInterface(DESCRIPTOR);// TODO: 2020-01-28 2.解包
                        Log.e(">>>", "e、onTransact代理解包");
                    this.add(data.readInt());
                    reply.writeNoException();
                    return true;
                    case TRANSACTION_add_result:
                    data.enforceInterface(DESCRIPTOR);
                    this.addResult(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(0);
                    return true;
            }
            return super.onTransact(code, data, reply, flags);
        }
        public static class Proxy implements IAddMangerService {

            private final IBinder remote;

            public Proxy(IBinder iBinder) {
                remote = iBinder;
            }

            @Override
            public IBinder asBinder() {
                return remote;
            }

//            @Override
//            public int add(int a) throws RemoteException {
//                Parcel _data = Parcel.obtain();
//                Parcel _reply = Parcel.obtain();
//                int _result;
//                try {
//                    _data.writeInterfaceToken(DESCRIPTOR);
//                    _data.writeInt(a);
//                    remote.transact(TRANSACTION_add,_data,_reply,0);
//                    _reply.readException();
//                    _result = _reply.readInt();
//                } finally {
//                    _reply.recycle();
//                    _data.recycle();
//                }
//                return _result;
//            }


            @Override
            public void add(int a) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(a);
                    remote.transact(TRANSACTION_add,_data,_reply,0);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void addResult(int a) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(a);
                    Log.e(TAG, "3.1、transact代理发包挂起，直到onTransact执行完毕");
                    remote.transact(TRANSACTION_add_result,_data,_reply,0);
                    Log.e(TAG, "3.2、transact代理发包结束");
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }

    //    int add(int a) throws RemoteException;
    void add(int a) throws RemoteException;
    void addResult(int a) throws RemoteException;
}
