package com.example.binderobserve;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.Nullable;

public class BookManagerService extends Service {
    Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
            onNewBookArrived(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerList.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerList.unregister(listener);
            mIsServiceDestoryed.set(true);
            destory=true;
        }
    };
    /**
     * 因为AIDL方法是在服务端线程池中进行的，存在线程同步的问题，可以直接使用CopyOnWriteArrayList来进行自动的线程同步，它支持并发读写。
     * 注意点中说AIDL只支持ArrayList，虽然这里服务端中声明集合为CopyOnWriteArrayList，但是在Binder中会按照List的规范去访问数据，最终形成一个ArrayList返回给客户端，并不影响结论。
     */
    CopyOnWriteArrayList<Book> mBookList;

    /**
     * RemoteCallbackList是一个接口，支持管理任意AIDL接口
     * 它的内部有一个map，值是Callback，键是IBinder。Callback就是监听接口，
     * ArrayMap<IBinder, Callback> mCallbacks = new ArrayMap<IBinder, Callback>();
     * 虽然说跨进程传输客户端的通过一个对象会在服务端生成不同的对象，但是这些不同的对象有一个共同的特点就是它们底层的Binder对象是同一个。
     * RemoteCallback就利用了这一点。另外RemoteCallback还自动实现了线程同步
     */
    RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<>();
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
    private boolean destory;


    /*
    每隔5s新书到达，遍历监听去通知所有订阅者*/
    private void onNewBookArrived(Book book) {
        int size = mListenerList.beginBroadcast();
        for (int i = 0; i < size; i++) {
            IOnNewBookArrivedListener listener = mListenerList.getBroadcastItem(i);
            if (listener != null) {
                try {
                    listener.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mListenerList = new RemoteCallbackList<>();
        mBookList = new CopyOnWriteArrayList<>();
        mBookList.add(new Book(1, "aaaaa"));
        mBookList.add(new Book(0, "book"));
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (!mIsServiceDestoryed.get()||!destory) {
                    try {
                        Thread.sleep(5000);
                        int bookId = mBookList.size() + 1;
                        String bookName = "书名" + bookId;
                        Book book = new Book(bookId, bookName);
                        mBookList.add(book);
                        onNewBookArrived(book);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        /*
        权限验证
        *方法一：Manifest
        方法二：在服务端的onTransact()中加入pid/uid   在onbind时验证
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String packageName = null;
        String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
        if (packages != null && packages.length > 0) {
            packageName = packages[0];
        }
        if (!packageName.startsWith("com.utte")) {
            return false;
        }

        return super.onTransact(code, data, reply, flags);
        * */
        int check = checkCallingOrSelfPermission("com.example.binderobserve.aidl.ACCESS_BOOK_SERVICE");
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }

//        方法二
//        if (Binder.getCallingPid()=="xxx"&&Binder.getCallingUid()=="xxx") {
//
//        }
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServiceDestoryed.set(true);
        destory=true;
    }
}