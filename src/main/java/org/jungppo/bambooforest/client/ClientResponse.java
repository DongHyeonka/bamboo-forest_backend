package org.jungppo.bambooforest.client;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientResponse<T> {
	private T data;

	public static <T> ClientResponse<T> success(T data) {
		return new ClientResponse<>(data);
	}

	public static <T> ClientResponse<T> failure() {
		return new ClientResponse<>(null);
	}

	public Optional<T> getData() {
		return Optional.ofNullable(data);
	}
}
