package com.currenjin.iterator;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void next() {
        Book book1 = new Book();
        Book book2 = new Book();

        Books books = new Books(book1, book2);

        Iterator<Book> newBooks = books.getBooks();

        assertDoesNotThrow(newBooks::next);
        assertDoesNotThrow(newBooks::next);
        assertThrows(NoSuchElementException.class, newBooks::next);
    }

    @Test
    void cannot_remove_books() {
        Book book1 = new Book();
        Book book2 = new Book();

        Books books = new Books(book1, book2);

        Iterator<Book> newBooks = books.getBooks();

        UnsupportedOperationException exception =
                assertThrows(UnsupportedOperationException.class, newBooks::remove);
        assertEquals("custom exception message", exception.getMessage());
    }
}