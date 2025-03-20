package com.currenjin.iterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Books {
    private final List<Book> books = new ArrayList<>();

    public Books(Book ... books) {
        this.books.addAll(Arrays.asList(books));
    }


    public Iterator<Book> getBooks() {
        final Iterator<Book> iterator = books.iterator();

        return new Iterator<>() {
            @Override
            public Book next() {
                return iterator.next();
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("custom exception message");
            }
        };
    }
}
