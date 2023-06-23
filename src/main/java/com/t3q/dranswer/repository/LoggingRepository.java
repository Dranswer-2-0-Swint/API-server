package com.t3q.dranswer.repository;

import com.t3q.dranswer.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface LoggingRepository extends JpaRepository<LogEntity,Long> {
}
