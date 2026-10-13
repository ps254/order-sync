package ru.ordersync.web;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import ru.ordersync.sync.DedupResult;

@JsonPropertyOrder({"clients", "stats"})
public record ClientsResponse(List<ClientView> clients, StatsView stats) {

	public static ClientsResponse from(DedupResult result) {
		List<ClientView> clients = result.clients().stream()
				.map(client -> new ClientView(client.name(), client.phone(), client.email(), client.amount()))
				.toList();
		return new ClientsResponse(
				clients,
				new StatsView(result.received(), result.unique(), result.duplicatesMerged(), result.invalidDropped())
		);
	}

}
