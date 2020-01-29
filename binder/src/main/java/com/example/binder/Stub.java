package com.example.binder;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.example.Proxy;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2020-01-26 23:49
 * @创建人：常守达
 */
public abstract class Stub extends Binder implements IPersonManger {
    // Binder唯一标识
    public static final String DESCRIPTOR = "com.netease.binder";
    // 方法标识
    public static final int GET_PERSON = IBinder.FIRST_CALL_TRANSACTION;
    public static final int ADD_PERSON = IBinder.FIRST_CALL_TRANSACTION + 1;

    /**
     * 通过queryLocalInterface方法，查找本地Binder对象
     * 如果返回的就是PersonManger，说明client和server处于同一个进程，直接返回
     * 如果不是，返回给一个代理对象
     *
     * @param iBinder Binder驱动传来的IBinder对象
     */
    public static IPersonManger asInterface(IBinder iBinder) {
        IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
        if (iInterface instanceof IPersonManger) {
            return (IPersonManger) iInterface;
        }
        return new Proxy(iBinder);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        // 1. 收到Binder驱动通知后，Server 进程通过回调Binder对象onTransact（）进行数据解包 & 调用目标方法
        // code即在transact（）中约定的目标方法的标识符
        switch (code) {
            case INTERFACE_TRANSACTION:
                return true;

            case GET_PERSON:
                // 解包Parcel中的数据
                data.enforceInterface(DESCRIPTOR);
                List<Person> personList = this.getPersonList();
                if (reply != null) {
                    reply.writeNoException();
                    reply.writeTypedList(personList);
                }
                return true;

            case ADD_PERSON:
                data.enforceInterface(DESCRIPTOR);
                Person person = null;
                // 解析目标方法对象的标识符
                if (data.readInt() != 0) {
                    person = Person.CREATOR.createFromParcel(data);
                }
                this.addPerson(person);
                // 将结果写入到reply
                if (reply != null) reply.writeNoException();
                return true;
        }
        // 2. 将结算结果返回 到Binder驱动
        return super.onTransact(code, data, reply, flags);
    }
}

