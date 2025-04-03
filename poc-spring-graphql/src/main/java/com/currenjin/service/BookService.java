package com.currenjin.service;

import com.currenjin.domain.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    
    public Book getBookById(String id) {
        return Book.getById(id);
    }
    
    public List<Book> getAllBooks() {
        return Book.getAll();
    }
}