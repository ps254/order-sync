package ru.ordersync.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.ordersync.domain.SyncStatsEntity;
import ru.ordersync.source.OrdersClient;
import ru.ordersync.sync.DedupResult;
import ru.ordersync.sync.OrderDeduplicator;
import ru.ordersync.web.ClientView;
import ru.ordersync.web.ClientsResponse;
import ru.ordersync.web.StatsView;

@Service
public class SyncService {

	private static final Logger log = LoggerFactory.getLogger(SyncService.class);

	private final OrdersClient ordersClient;
	private final OrderDeduplicator deduplicator;
	private final ClientStore clientStore;
	private final Object lock = new Object();

	public SyncService(OrdersClient ordersClient, OrderDeduplicator deduplicator, ClientStore clientStore) {
		this.ordersClient = ordersClient;
		this.deduplicator = deduplicator;
		this.clientStore = clientStore;
	}

	public ClientsResponse synchronize() {
		synchronized (lock) {
			DedupResult result = deduplicator.deduplicate(ordersClient.fetch());
			clientStore.replace(result);
			log.info(
					"Синхронизация: received={}, unique={}, duplicates_merged={}, invalid_dropped={}",
					result.received(),
					result.unique(),
					result.duplicatesMerged(),
					result.invalidDropped()
			);
			return ClientsResponse.from(result);
		}
	}

	public ClientsResponse current() {
		SyncStatsEntity stats = clientStore.stats();
		StatsView statsView = stats == null
				? new StatsView(0, 0, 0, 0)
				: new StatsView(
						stats.getReceived(),
						stats.getUniqueClients(),
						stats.getDuplicatesMerged(),
						stats.getInvalidDropped()
				);
		List<ClientView> clients = clientStore.clients().stream()
				.map(client -> new ClientView(client.getName(), client.getPhone(), client.getEmail(), client.getAmount()))
				.toList();
		return new ClientsResponse(clients, statsView);
	}

}
