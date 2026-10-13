package ru.ordersync.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class ClientEntity {

	@Id
	@Column(nullable = false, length = 16)
	private String phone;

	@Column(nullable = false, length = 500)
	private String name;

	@Column(nullable = false, length = 320)
	private String email;

	@Column(nullable = false)
	private long amount;

	protected ClientEntity() {
	}

	public ClientEntity(String phone, String name, String email, long amount) {
		this.phone = phone;
		this.name = name;
		this.email = email;
		this.amount = amount;
	}

	public String getPhone() {
		return phone;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public long getAmount() {
		return amount;
	}

}
