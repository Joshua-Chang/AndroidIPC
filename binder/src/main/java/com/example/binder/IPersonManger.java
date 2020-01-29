package com.example.binder;

import android.os.IInterface;

import java.util.List;

/**
 * 定义接口服务
 * 服务端具备功能提供给客户端，定义一个接口继承IInterface
 */
public interface IPersonManger extends IInterface {

    void addPerson(Person person);

    List<Person> getPersonList();
}