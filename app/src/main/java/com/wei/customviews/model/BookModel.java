package com.wei.customviews.model;

import java.util.List;

/**
 * author: WEI
 * date: 2017/3/10
 */

public interface BookModel
{
    void saveBooks(List<Book> books);
    void loadBooksFromCache();
}
