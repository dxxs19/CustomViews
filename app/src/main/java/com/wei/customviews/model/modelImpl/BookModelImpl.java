package com.wei.customviews.model.modelImpl;

import android.os.Parcel;

import com.wei.customviews.model.Book;
import com.wei.customviews.model.BookModel;
import com.wei.utillibrary.utils.LogUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * author: WEI
 * date: 2017/3/10
 */

public class BookModelImpl implements BookModel
{
    private final String TAG = getClass().getSimpleName();
    private List<Book> mBooks = new LinkedList<>();

    @Override
    public void saveBooks(List<Book> books) {
        mBooks.addAll(books);
    }

    @Override
    public void loadBooksFromCache() {
        LogUtil.e(TAG, "--- loadBooksFromCache ---");
    }
}
