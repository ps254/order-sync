package ru.ordersync.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "sync_stats")
public class SyncStatsEntity {

	@Id
	private Integer id;

	@Column(nullable = false)
	private int received;

	@Column(name = "unique_clients", nullable = false)
	private int uniqueClients;

	@Column(name = "duplicates_merged", nullable = false)
	private int duplicatesMerged;

	@Column(name = "invalid_dropped", nullable = false)
	private int invalidDropped;

	@Column(name = "synced_at", nullable = false)
	private Instant syncedAt;

	protected SyncStatsEntity() {
	}

	public SyncStatsEntity(
			Integer id,
			int received,
			int uniqueClients,
			int duplicatesMerged,
			int invalidDropped,
			Instant syncedAt
	) {
		this.id = id;
		this.received = received;
		this.uniqueClients = uniqueClients;
		this.duplicatesMerged = duplicatesMerged;
		this.invalidDropped = invalidDropped;
		this.syncedAt = syncedAt;
	}

	public int getReceived() {
		return received;
	}

	public int getUniqueClients() {
		return uniqueClients;
	}

	public int getDuplicatesMerged() {
		return duplicatesMerged;
	}

	public int getInvalidDropped() {
		return invalidDropped;
	}

}
