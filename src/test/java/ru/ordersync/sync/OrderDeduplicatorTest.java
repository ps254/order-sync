package ru.ordersync.sync;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import ru.ordersync.phone.PhoneNormalizer;
import ru.ordersync.source.RawOrder;
import tools.jackson.databind.json.JsonMapper;

class OrderDeduplicatorTest {

	private final OrderDeduplicator deduplicator = new OrderDeduplicator(new PhoneNormalizer());
	private final JsonMapper jsonMapper = JsonMapper.builder().build();

	@Test
	void keepsMaxAmountAndLaterTimestampFromSampleFeed() throws IOException {
		DedupResult result = deduplicator.deduplicate(sampleOrders());

		assertThat(result.received()).isEqualTo(17);
		assertThat(result.unique()).isEqualTo(8);
		assertThat(result.duplicatesMerged()).isEqualTo(5);
		assertThat(result.invalidDropped()).isEqualTo(4);

		Map<String, NormalizedClient> byPhone = result.clients().stream()
				.collect(Collectors.toMap(NormalizedClient::phone, Function.identity()));
		assertThat(byPhone.get("+79991234567")).isEqualTo(client(2, "Иван П.", "+79991234567", "", 5000, "2026-10-02T11:00:00Z"));
		assertThat(byPhone.get("+79160000001")).isEqualTo(client(5, "Мария С.", "+79160000001", "maria@example.com", 200, "2026-10-05T12:00:00Z"));
		assertThat(byPhone.get("+79219998877")).isEqualTo(client(8, "Анна В.", "+79219998877", "", 1500, "2026-10-06T16:00:00Z"));
		assertThat(byPhone.get("+79261234567")).isEqualTo(client(10, "Елена Морозова", "+79261234567", "elena@example.com", 9000, "2026-10-03T13:00:00Z"));
		assertThat(byPhone.get("+79005551212").name()).isEqualTo("Павел Орлов");
		assertThat(result.clients()).extracting(NormalizedClient::phone).isSorted();
	}

	@Test
	void secondPassOnTheSameFeedIsIdentical() throws IOException {
		List<RawOrder> orders = sampleOrders();
		assertThat(deduplicator.deduplicate(orders)).isEqualTo(deduplicator.deduplicate(orders));
	}

	@Test
	void equalAmountKeepsLaterCreatedAt() {
		List<RawOrder> orders = List.of(
				new RawOrder(1L, "Первый", "89990001122", "a@example.com", 100L, Instant.parse("2026-10-01T00:00:00Z")),
				new RawOrder(2L, "Второй", "+7 999 000 11 22", "b@example.com", 100L, Instant.parse("2026-10-02T00:00:00Z"))
		);

		DedupResult result = deduplicator.deduplicate(orders);

		assertThat(result.unique()).isEqualTo(1);
		assertThat(result.duplicatesMerged()).isEqualTo(1);
		assertThat(result.clients().getFirst().name()).isEqualTo("Второй");
		assertThat(result.clients().getFirst().email()).isEqualTo("b@example.com");
	}

	private List<RawOrder> sampleOrders() throws IOException {
		try (InputStream input = getClass().getResourceAsStream("/orders-sample.json")) {
			RawOrder[] orders = jsonMapper.readValue(input, RawOrder[].class);
			return List.of(orders);
		}
	}

	private static NormalizedClient client(long id, String name, String phone, String email, long amount, String createdAt) {
		return new NormalizedClient(id, name, phone, email, amount, Instant.parse(createdAt));
	}

}
