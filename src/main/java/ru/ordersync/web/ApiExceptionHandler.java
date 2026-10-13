package ru.ordersync.web;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.ordersync.source.OrdersSourceException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(OrdersSourceException.class)
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	public Map<String, String> sourceUnavailable(OrdersSourceException ex) {
		return Map.of("error", ex.getMessage());
	}

}
