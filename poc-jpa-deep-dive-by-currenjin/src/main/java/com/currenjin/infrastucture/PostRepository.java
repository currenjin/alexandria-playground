package com.currenjin.infrastucture;

import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.currenjin.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	// @QueryHints(@QueryHint(name = "javax.persistence.lock.timeout", value = "6000"))
	// H2에서는 타임아웃 컨트롤 불가
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Post p WHERE p.id = :id")
	Optional<Post> findByIdWithLock(@Param("id") Long id);
}
