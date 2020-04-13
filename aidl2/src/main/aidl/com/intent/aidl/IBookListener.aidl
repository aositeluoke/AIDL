// IBookListener.aidl
package com.intent.aidl;
import com.intent.aidl.Book;

// Declare any non-default types here with import statements

interface IBookListener {
   void onNewBook(in Book book);
}
