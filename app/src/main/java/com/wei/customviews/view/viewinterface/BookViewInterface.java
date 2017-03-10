package com.wei.customviews.view.viewinterface;

import com.wei.customviews.model.Book;
import com.wei.customviews.test.designmode.Student;

import java.util.List;

/**
 * author: WEI
 * date: 2017/3/10
 */

public interface BookViewInterface
{
    void showBooks(List<Book> books);
    void showLoading();
    void hideLoading();
}
