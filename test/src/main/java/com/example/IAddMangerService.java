package com.example;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * 定义接口服务
 * 服务端具备功能提供给客户端，定义一个接口继承IInterface
 */
public interface IAddMangerService extends IInterface {
    public static final String DESCRIPTOR = "com.example.add";


    public static abstract class Stub extends Binder implements IAddMangerService {

        static final int TRANSACTION_add = FIRST_CALL_TRANSACTION + 0;
        static final int TRANSACTION_add_result = FIRST_CALL_TRANSACTION + 1;

        @Override
        public IBinder asBinder() {
            return this;
        }
        /**
         * 通过queryLocalInterface方法查找，如果有本地Binder对象，说明client和server处于同一个进程，直接返回
         * 如果不是，返回给一个代理远程对象BinderProxy
         * @param obj Binder驱动传来的IBinder对象
         */
        public static IAddMangerService asInterface(IBinder obj) {
            Log.e(">>>", "b、asInterface判断是否在同一个进程，不是调用代理");
            if (obj == null) {
                return null;
            }
            IInterface iInterface = obj.queryLocalInterface(DESCRIPTOR);
            if ((iInterface != null) && iInterface instanceof IAddMangerService) {
                return (IAddMangerService) iInterface;
            }
            return new Proxy(obj);
        }

        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            // 1. 收到Binder驱动通知后，Server 进程通过回调Binder对象onTransact（）进行数据解包 & 调用目标方法
            // code即在transact（）中约定的目标方法的标识符
            switch (code) {
                case INTERFACE_TRANSACTION:
                    reply.writeString(DESCRIPTOR);
                    return true;
                case TRANSACTION_add:
                    data.enforceInterface(DESCRIPTOR);                // 解包Parcel中的数据
                    this.add(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_add_result:
                    data.enforceInterface(DESCRIPTOR);
                    Log.e(">>>", "4、onTransact代理解包");
                    this.addResult(data.readInt());
                    reply.writeNoException();
                    return true;
            }
            return super.onTransact(code, data, reply, flags);
        }
        /**
         * 代理对象实质就是client最终拿到的代理服务，通过这个就可以和Server进行通信了
         * 首先通过Parcel将数据序列化，然后调用 remote.transact()将方法code，和data传输过去，
         * 对应的会回调在在Server中的onTransact()中
         */
        public static class Proxy implements IAddMangerService {

            private final IBinder remote;

            public Proxy(IBinder iBinder) {
                remote = iBinder;
            }

            @Override
            public IBinder asBinder() {
                return remote;
            }

            @Override
            public void add(int a) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(a);
                    // TODO: 2020-01-28 1.发包
                    // 通过 调用代理对象的transact（） 将 上述数据发送到Binder驱动
                    Log.e(">>>", "c.1、transact代理发包挂起，直到onTransact执行完毕");
                    remote.transact(TRANSACTION_add, _data, _reply, 0);
                    // 注：在发送数据后，Client进程的该线程会暂时被挂起
                    Log.e(">>>", "c.2、transact代理发包结束");

                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            // 3. Binder驱动根据 代理对象 找到对应的真身Binder对象所在的Server 进程（系统自动执行）
            // 4. Binder驱动把 数据 发送到Server 进程中，并通知Server 进程执行解包（系统自动执行）

            @Override
            public void addResult(int a) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(a);
                    remote.transact(TRANSACTION_add_result, _data, _reply, 0);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }

    void add(int a) throws RemoteException;

    void addResult(int a) throws RemoteException;
}
