package com.intent.aidl;
import com.intent.aidl.Book;
import com.intent.aidl.IBookListener;
interface IBookManager{
    void addBook(in Book book);
    List<Book> getBookList();
    void register(in IBookListener listener);
    void unregister(in IBookListener listener);
}