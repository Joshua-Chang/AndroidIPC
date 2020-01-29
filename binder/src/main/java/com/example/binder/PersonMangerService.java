package com.example.binder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static com.example.binder.BuildConfig.TAG;

/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2020-01-26 23:43
 * @创建人：常守达
 */
public class PersonMangerService extends Service {
    private List<Person> mPeople = new ArrayList<>();

    @Override
    public void onCreate() {
        mPeople.add(new Person());
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        return new Stub() {
//            @Override
//            public void addPerson(Person person) {
//                if (person == null) {
//                    person = new Person();
//                    Log.e(TAG, "null obj");
//                }
//                mPeople.add(person);
//                Log.e(TAG, mPeople.size() + "");
//            }
//
//            @Override
//            public List<Person> getPersonList() {
//                return mPeople;
//            }
//        };
        return new IPerson2Manger.Stub() {
            @Override
            public void addPerson(Person person) throws RemoteException {
                if (person == null) {
                    person = new Person();
                    Log.e(TAG, "null obj");
                }
                mPeople.add(person);
                Log.e(TAG, mPeople.size() + "");
            }

            @Override
            public List<Person> getPersonList() throws RemoteException {
                return mPeople;
            }
        };
    }
}
