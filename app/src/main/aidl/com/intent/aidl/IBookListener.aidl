package com.intent.aidl;
import com.intent.aidl.Book;
interface IBookListener{
    void onBookArrive(in Book book);
}