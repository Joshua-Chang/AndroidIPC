// IPerson2Manger.aidl
package com.example.binder;
import com.example.binder.Person;
interface IPerson2Manger {
     void addPerson(in Person person);
     List<Person> getPersonList();
}
