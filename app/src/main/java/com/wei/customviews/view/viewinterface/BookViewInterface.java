package com.wei.customviews.view.viewinterface;

import com.wei.customviews.model.Book;

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
