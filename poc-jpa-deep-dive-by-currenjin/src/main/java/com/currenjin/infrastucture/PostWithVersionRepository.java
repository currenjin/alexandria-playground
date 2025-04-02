package com.currenjin.infrastucture;

import org.springframework.data.jpa.repository.JpaRepository;

import com.currenjin.domain.PostWithVersion;

public interface PostWithVersionRepository extends JpaRepository<PostWithVersion, Long> {
}
