package ru.ordersync.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"received", "unique", "duplicates_merged", "invalid_dropped"})
public record StatsView(
		int received,
		@JsonProperty("unique") int unique,
		@JsonProperty("duplicates_merged") int duplicatesMerged,
		@JsonProperty("invalid_dropped") int invalidDropped
) {
}
