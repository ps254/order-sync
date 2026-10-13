package ru.ordersync.phone;

import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class PhoneNormalizer {

	private static final Pattern NON_DIGITS = Pattern.compile("\\D");

	/**
	 * Приводит телефон к {@code +7XXXXXXXXXX}.
	 * 11 цифр с ведущей 8 заменяются на 7, 10 цифр дополняются кодом страны 7.
	 * Всё, что не сводится к 11 цифрам с ведущей 7, считается невалидным.
	 */
	public Optional<String> normalize(String raw) {
		if (raw == null || raw.isBlank()) {
			return Optional.empty();
		}
		String digits = NON_DIGITS.matcher(raw).replaceAll("");
		if (digits.length() == 10) {
			digits = "7" + digits;
		}
		else if (digits.length() == 11 && digits.charAt(0) == '8') {
			digits = "7" + digits.substring(1);
		}
		if (digits.length() == 11 && digits.charAt(0) == '7') {
			return Optional.of("+" + digits);
		}
		return Optional.empty();
	}

}
