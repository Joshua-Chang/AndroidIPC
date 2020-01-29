//package com.example.binder.test;
//
//import android.os.Binder;
//import android.os.IBinder;
//import android.os.IInterface;
//import android.os.Parcel;
//import android.os.RemoteException;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import static com.example.binder.Stub.DESCRIPTOR;
//
//
///**
// * 定义接口服务
// * 服务端具备功能提供给客户端，定义一个接口继承IInterface
// */
//public interface IAddMangerService extends IInterface {
////    public static final String DESCRIPTOR = "com.example.binder.add";
//
//    public static abstract class Stub extends Binder implements IAddMangerService{
//
//        static final int TRANSACTION_add = FIRST_CALL_TRANSACTION + 0;
//
//        @Override
//        public IBinder asBinder() {
//            return this;
//        }
//
//        public static IAddMangerService asInterface(IBinder obj){
//            if (obj == null) {
//                return null;
//            }
//            IInterface iInterface = obj.queryLocalInterface(DESCRIPTOR);
//            if ((iInterface != null) &&iInterface instanceof IAddMangerService){
//                return (IAddMangerService) iInterface;
//            }
//            return new Proxy(obj);
//        }
//        @Override
//        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
//            switch (code) {
//                case INTERFACE_TRANSACTION:
//                    reply.writeString(DESCRIPTOR);
//                    return true;
//                case TRANSACTION_add:
//                    data.enforceInterface(DESCRIPTOR);
//                    int _arg0;
//                    _arg0 = data.readInt();
//                    reply.writeNoException();
//                    reply.writeInt(this.add(_arg0));
//                    return true;
//            }
//            return super.onTransact(code, data, reply, flags);
//        }
//        public static class Proxy implements IAddMangerService{
//
//            private final IBinder remote;
//
//            public Proxy(IBinder iBinder) {
//                remote = iBinder;
//            }
//
//            @Override
//            public IBinder asBinder() {
//                return remote;
//            }
//
//            @Override
//            public int add(int a) throws RemoteException {
//                android.os.Parcel _data = android.os.Parcel.obtain();
//                android.os.Parcel _reply = android.os.Parcel.obtain();
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
//        }
//    }
//
//    int add(int a) throws RemoteException;
//}
