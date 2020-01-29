package com.example.binder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.IAddMangerService;
import com.example.binder.test.AddService;

import static com.example.binder.BuildConfig.TAG;

/**
 * bindService传入一个ServiceConnection对象，在与服务端建立连接时，
 * 通过我们定义好的BinderObj(Stub)的asInterface方法返回一个代理对象，再调用方法进行交互
 */
public class MainActivity extends AppCompatActivity {//hierarchy
    private boolean isConnect = false;
//    private IPersonManger personManger;
//    private IPerson2Manger personManger;
    private IAddMangerService personManger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, AddService.class);
//        Intent intent = new Intent(this, PersonMangerService.class);
//        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
//    }
//
//    public void start(View view) {
//        if (personManger == null) {
//            Log.e(TAG, "connect error");
//            return;
//        }
//        personManger.addPerson(new Person());
//        Log.e(TAG, personManger.getPersonList().size() + "");
//    }
//    private ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            Log.e(TAG, "connect success");
//            isConnect = true;
//            personManger = Stub.asInterface(service);
//            List<Person> personList = personManger.getPersonList();
//            Log.e(TAG, personList.size() + "");
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Log.e(TAG, "connect failed");
//            isConnect = false;
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (isConnect) unbindService(mServiceConnection);
//    }







//        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
//    }
//
//    public void start(View view) {
//        if (personManger == null) {
//            Log.e(TAG, "connect error");
//            return;
//        }
//        try {
//            personManger.addPerson(new Person());
//            Log.e(TAG, personManger.getPersonList().size() + "");
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
//    private ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            Log.e(TAG, "connect success");
//            isConnect = true;
//            personManger = IPerson2Manger.Stub.asInterface(service);
//            List<Person> personList = null;
//            try {
//                personList = personManger.getPersonList();
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//            Log.e(TAG, personList.size() + "");
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Log.e(TAG, "connect failed");
//            isConnect = false;
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (isConnect) unbindService(mServiceConnection);
//    }






//        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
//    }
//
//    public void start(View view) {
//        if (personManger == null) {
//            Log.e(TAG, "connect error");
//            return;
//        }
//        try {
//            int i = personManger.add(10);
//            Log.e(TAG, "?+10="+i);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
//    private ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            Log.e(TAG, "connect success");
//            isConnect = true;
//            personManger = IAddMangerService.Stub.asInterface(service);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Log.e(TAG, "connect failed");
//            isConnect = false;
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (isConnect) unbindService(mServiceConnection);
//    }


        initBindService();
//        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initBindService() {
        Intent intent=new Intent();
        intent.setAction("BinderClient_Action");
        intent.setPackage("com.example.test");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        if (getIntent() != null) {
            int i = getIntent().getIntExtra("add", 0);
            ((TextView) findViewById(R.id.tv)).setText(i +"+10="+(i+10));
        }
    }

    public void start(View view) {
        if (personManger == null) {
            Log.e(TAG, "connect error");
            return;
        }
        try {
            Log.e(TAG, "addResult跨进程service调用开始--------------------------");
            personManger.addResult(20);
            Log.e(TAG, "addResult跨进程service调用结束--------------------------");

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "1、跨进程service");
            isConnect = true;
            personManger = IAddMangerService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "connect failed");
            isConnect = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isConnect) unbindService(mServiceConnection);
    }
}
