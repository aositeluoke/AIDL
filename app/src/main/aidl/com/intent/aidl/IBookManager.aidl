package com.intent.aidl;
import com.intent.aidl.Book;
import com.intent.aidl.IBookListener;
interface IBookManager{
List<Book> getBookList();
void addBook(in Book book);
void register(in IBookListener listener);
void unRegister(in IBookListener listener);
}