package br.com.db1.christmastree;

import java.util.function.Supplier;

public class UserNotFoundException extends RuntimeException implements Supplier<UserNotFoundException> {

	public UserNotFoundException(String message) {
		super(message);
	}

	@Override
	public UserNotFoundException get() {
		return this;
	}
}
