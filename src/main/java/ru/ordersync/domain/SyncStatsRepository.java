package ru.ordersync.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncStatsRepository extends JpaRepository<SyncStatsEntity, Integer> {
}
