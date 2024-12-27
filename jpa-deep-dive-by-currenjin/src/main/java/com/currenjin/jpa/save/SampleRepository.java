package com.currenjin.jpa.save;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Post, Long> {
}
