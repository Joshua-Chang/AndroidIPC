package com.example;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.example.binder.IPersonManger;
import com.example.binder.Person;
import com.example.binder.Stub;

import java.util.List;

import static com.example.binder.Stub.DESCRIPTOR;


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2020-01-26 23:56
 * @创建人：常守达
 */

/**
 * 代理对象实质就是client最终拿到的代理服务，通过这个就可以和Server进行通信了
 * 首先通过Parcel将数据序列化，然后调用 remote.transact()将方法code，和data传输过去，
 * 对应的会回调在在Server中的onTransact()中
 */
public class Proxy implements IPersonManger {
    private IBinder mIBinder;

    public Proxy(IBinder iBinder) {
        this.mIBinder = iBinder;
    }
    @Override
    public void addPerson(Person person) {
        // Client进程 将需要传送的数据写入到Parcel对象中
        // data = 数据 = 目标方法的参数（Client进程传进来的，此处就是整数a和b） + IInterface接口对象的标识符descriptor
        Parcel data = Parcel.obtain();
        Parcel replay = Parcel.obtain();

        try {
            data.writeInterfaceToken(DESCRIPTOR);
            if (person != null) {
                data.writeInt(1);
                person.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            // 通过 调用代理对象的transact（） 将 上述数据发送到Binder驱动
            // Stub.add：目标方法的标识符（Client进程 和 Server进程 自身约定，可为任意）
            // data ：上述的Parcel对象
            // reply：返回结果
            mIBinder.transact(Stub.ADD_PERSON, data, replay, 0);
            // 注：在发送数据后，Client进程的该线程会暂时被挂起
            replay.readException();
        } catch (RemoteException e){
            e.printStackTrace();
        } finally {
            replay.recycle();
            data.recycle();
        }
    }

    // 3. Binder驱动根据 代理对象 找到对应的真身Binder对象所在的Server 进程（系统自动执行）
    // 4. Binder驱动把 数据 发送到Server 进程中，并通知Server 进程执行解包（系统自动执行）

    @Override
    public List<Person> getPersonList() {
        Parcel data = Parcel.obtain();
        Parcel replay = Parcel.obtain();

        List<Person> result = null;
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            mIBinder.transact(Stub.GET_PERSON, data, replay, 0);
            replay.readException();
            result = replay.createTypedArrayList(Person.CREATOR);
        }catch (RemoteException e){
            e.printStackTrace();
        } finally{
            replay.recycle();
            data.recycle();
        }
        return result;
    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
