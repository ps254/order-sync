package ru.ordersync.source;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RawOrder(
		Long id,
		String name,
		String phone,
		String email,
		Long amount,
		@JsonProperty("created_at") Instant createdAt
) {
}
