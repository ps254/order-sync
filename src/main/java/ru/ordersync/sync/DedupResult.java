package ru.ordersync.sync;

import java.util.List;

public record DedupResult(
		List<NormalizedClient> clients,
		int received,
		int unique,
		int duplicatesMerged,
		int invalidDropped
) {
}
