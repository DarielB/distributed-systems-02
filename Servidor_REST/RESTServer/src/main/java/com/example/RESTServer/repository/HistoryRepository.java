package com.example.RESTServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.RESTServer.domain.entity.ExchangeHistoryEntity;

@Repository
public interface HistoryRepository extends JpaRepository<ExchangeHistoryEntity, Long> {
}
