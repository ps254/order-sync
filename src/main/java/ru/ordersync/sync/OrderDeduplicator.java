package ru.ordersync.sync;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import ru.ordersync.phone.PhoneNormalizer;
import ru.ordersync.source.RawOrder;

@Component
public class OrderDeduplicator {

	private final PhoneNormalizer phones;

	public OrderDeduplicator(PhoneNormalizer phones) {
		this.phones = phones;
	}

	public DedupResult deduplicate(List<RawOrder> orders) {
		int invalidDropped = 0;
		Map<String, NormalizedClient> bestByPhone = new HashMap<>();
		for (RawOrder order : orders) {
			var phone = phones.normalize(order.phone());
			if (phone.isEmpty()) {
				invalidDropped++;
				continue;
			}
			NormalizedClient candidate = toClient(order, phone.get());
			bestByPhone.merge(candidate.phone(), candidate, this::prefer);
		}
		List<NormalizedClient> clients = bestByPhone.values().stream()
				.sorted(Comparator.comparing(NormalizedClient::phone))
				.toList();
		int unique = clients.size();
		int duplicatesMerged = orders.size() - invalidDropped - unique;
		return new DedupResult(clients, orders.size(), unique, duplicatesMerged, invalidDropped);
	}

	private NormalizedClient toClient(RawOrder order, String phone) {
		return new NormalizedClient(
				order.id() == null ? 0L : order.id(),
				order.name() == null ? "" : order.name(),
				phone,
				order.email() == null ? "" : order.email(),
				order.amount() == null ? 0L : order.amount(),
				order.createdAt() == null ? Instant.EPOCH : order.createdAt()
		);
	}

	/**
	 * Больший amount побеждает. При равенстве остаётся более поздний created_at.
	 * Если и время совпало, остаётся запись с большим id источника — результат стабильный.
	 */
	private NormalizedClient prefer(NormalizedClient current, NormalizedClient incoming) {
		int byAmount = Long.compare(incoming.amount(), current.amount());
		if (byAmount != 0) {
			return byAmount > 0 ? incoming : current;
		}
		int byTime = incoming.createdAt().compareTo(current.createdAt());
		if (byTime != 0) {
			return byTime > 0 ? incoming : current;
		}
		return incoming.sourceId() > current.sourceId() ? incoming : current;
	}

}
