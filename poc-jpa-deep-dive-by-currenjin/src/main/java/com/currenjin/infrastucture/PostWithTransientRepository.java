package com.currenjin.infrastucture;

import com.currenjin.domain.PostWithTransient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostWithTransientRepository extends JpaRepository<PostWithTransient, Long> {
}
