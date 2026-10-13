package ru.ordersync.sync;

import java.time.Instant;

public record NormalizedClient(
		long sourceId,
		String name,
		String phone,
		String email,
		long amount,
		Instant createdAt
) {
}
