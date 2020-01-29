// com.example.binderpool.IOrder.aidl
package com.example.binderpool;

// Declare any non-default types here with import statements
interface IOrder {
    void doOrder(long orderId);
    void cancelOrder(long orderId);
}
