package com.example.serviceBookCom.exceptions;

public class FileStorageException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FileStorageException(final String message) {
		super(message);
	}

	public FileStorageException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
