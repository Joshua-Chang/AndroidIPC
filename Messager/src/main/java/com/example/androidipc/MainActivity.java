package com.example.androidipc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private Messenger mMessenger;
    private Messenger mGetReplyMessenger=new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            Log.e(">>>","MainActivity客户端端接收到--: "+message.getData().get("msg").toString());
            return true;
        }
    }));
    private ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mMessenger = new Messenger(iBinder);
            try {
                Message message = Message.obtain(null,200);            //创建一个what值为Constants.CLENT_MESSAGE的message
                Bundle bundle = new Bundle();
                bundle.putString("msg","我是来自客户端的信息");
                message.setData(bundle);
                message.replyTo=mGetReplyMessenger;
                mMessenger.send(message);
                Log.e(">>>","send...");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService(new Intent(this,MessageService.class),conn,BIND_AUTO_CREATE);
    }
}
