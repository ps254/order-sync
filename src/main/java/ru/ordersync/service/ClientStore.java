package ru.ordersync.service;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ordersync.domain.ClientEntity;
import ru.ordersync.domain.ClientRepository;
import ru.ordersync.domain.SyncStatsEntity;
import ru.ordersync.domain.SyncStatsRepository;
import ru.ordersync.sync.DedupResult;

@Service
public class ClientStore {

	private final ClientRepository clients;
	private final SyncStatsRepository stats;
	private final EntityManager entityManager;

	public ClientStore(ClientRepository clients, SyncStatsRepository stats, EntityManager entityManager) {
		this.clients = clients;
		this.stats = stats;
		this.entityManager = entityManager;
	}

	@Transactional
	public void replace(DedupResult result) {
		clients.deleteAllInBatch();
		entityManager.flush();
		entityManager.clear();
		List<ClientEntity> entities = result.clients().stream()
				.map(client -> new ClientEntity(client.phone(), client.name(), client.email(), client.amount()))
				.toList();
		clients.saveAll(entities);
		stats.save(new SyncStatsEntity(
				1,
				result.received(),
				result.unique(),
				result.duplicatesMerged(),
				result.invalidDropped(),
				Instant.now()
		));
	}

	@Transactional(readOnly = true)
	public List<ClientEntity> clients() {
		return clients.findAllByOrderByPhoneAsc();
	}

	@Transactional(readOnly = true)
	public SyncStatsEntity stats() {
		return stats.findById(1).orElse(null);
	}

}
