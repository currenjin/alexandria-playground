package com.currenjin.service;

import com.currenjin.domain.Author;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    
    public Author getAuthorById(String id) {
        return Author.getById(id);
    }
    
    public List<Author> getAllAuthors() {
        return Author.getAll();
    }
}