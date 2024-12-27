package com.currenjin.jpa.save;

import org.hibernate.Session;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.engine.spi.Status;
import org.hibernate.internal.SessionImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SaveTest {
    @Autowired
    SampleRepository repository;

    @PersistenceContext
    EntityManager em;

    private SessionImpl getSessionImpl() {
        SharedSessionContractImplementor session = em.unwrap(SharedSessionContractImplementor.class);
        return (SessionImpl) session;
    }

    @Test
    @Transactional
    void persist() {
        Post post = new Post();

        repository.save(post);

        EntityEntry entry = getSessionImpl()
                .getPersistenceContextInternal()
                .getEntry(post);

        assertEquals(Status.MANAGED, entry.getStatus());
        assertTrue(entry.isExistsInDatabase());
    }

    @Test
    @Transactional
    void merge() {
        Post post = new Post();
        Post savedPost = repository.save(post);

        savedPost.setId(savedPost.getId() + 1L);
        repository.save(post);

        EntityEntry mergedEntry = getSessionImpl()
                .getPersistenceContextInternal()
                .getEntry(savedPost);

        assertEquals(Status.MANAGED, mergedEntry.getStatus());
        assertTrue(mergedEntry.isExistsInDatabase());
    }
}
