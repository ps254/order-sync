package ru.ordersync.source;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import ru.ordersync.config.AppProperties;

@Component
public class OrdersClient {

	private final RestClient restClient;
	private final AppProperties properties;

	public OrdersClient(AppProperties properties) {
		HttpClient httpClient = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(5))
				.build();
		JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
		requestFactory.setReadTimeout(Duration.ofSeconds(20));
		this.restClient = RestClient.builder().requestFactory(requestFactory).build();
		this.properties = properties;
	}

	public List<RawOrder> fetch() {
		try {
			RawOrder[] body = restClient.get()
					.uri(properties.ordersUrl())
					.retrieve()
					.body(RawOrder[].class);
			return body == null ? List.of() : List.of(body);
		}
		catch (RestClientException ex) {
			throw new OrdersSourceException("Не удалось получить заявки с источника", ex);
		}
	}

}
