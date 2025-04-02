package com.tdd.domain.consumer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerJpaRepository extends JpaRepository<ConsumerEntity, Long> {
}
