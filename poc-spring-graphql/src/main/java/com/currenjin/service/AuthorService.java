package com.currenjin.service;

import com.currenjin.domain.Author;
import com.currenjin.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    
    private final AuthorRepository authorRepository;
    
    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    
    public Optional<Author> getAuthorById(String id) {
        return authorRepository.findById(id);
    }
    
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }
    
    public void deleteAuthor(String id) {
        authorRepository.deleteById(id);
    }
}