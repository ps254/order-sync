package ru.ordersync.phone;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PhoneNormalizerTest {

	private final PhoneNormalizer normalizer = new PhoneNormalizer();

	@Test
	void normalizesRussianFormatsToPlus7() {
		assertThat(normalizer.normalize("+7 999 123-45-67")).contains("+79991234567");
		assertThat(normalizer.normalize("8 (999) 123 45 67")).contains("+79991234567");
		assertThat(normalizer.normalize("79991234567")).contains("+79991234567");
		assertThat(normalizer.normalize("8-916-000-00-01")).contains("+79160000001");
		assertThat(normalizer.normalize("+7 (916) 000 00 01")).contains("+79160000001");
		assertThat(normalizer.normalize("8 903 111 22 33")).contains("+79031112233");
		assertThat(normalizer.normalize("+79219998877")).contains("+79219998877");
		assertThat(normalizer.normalize("8 921 999 88 77")).contains("+79219998877");
		assertThat(normalizer.normalize("9005551212")).contains("+79005551212");
		assertThat(normalizer.normalize("+7-926-123-45-67")).contains("+79261234567");
		assertThat(normalizer.normalize("8 (926) 123-45-67")).contains("+79261234567");
		assertThat(normalizer.normalize("8 999 111 00 00")).contains("+79991110000");
		assertThat(normalizer.normalize("+7 916 445 56 67")).contains("+79164455667");
	}

	@Test
	void dropsNumbersThatDoNotBecomeElevenDigits() {
		assertThat(normalizer.normalize("123")).isEmpty();
		assertThat(normalizer.normalize("")).isEmpty();
		assertThat(normalizer.normalize("   ")).isEmpty();
		assertThat(normalizer.normalize(null)).isEmpty();
		assertThat(normalizer.normalize("не указан")).isEmpty();
		assertThat(normalizer.normalize("+7 999")).isEmpty();
		assertThat(normalizer.normalize("123456789012")).isEmpty();
		assertThat(normalizer.normalize("+1 202 555 0123")).isEmpty();
	}

}
