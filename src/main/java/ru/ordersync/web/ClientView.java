package ru.ordersync.web;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"name", "phone", "email", "amount"})
public record ClientView(String name, String phone, String email, long amount) {
}
