package com.t3q.dranswer.repository;

import com.t3q.dranswer.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggingRepository extends JpaRepository<LogEntity,Long> {
}
