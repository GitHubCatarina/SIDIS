package com.example.serviceAuth.userManagement.model;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

@Value
@AllArgsConstructor
public class Role implements GrantedAuthority {
	private static final long serialVersionUID = 1L;

	public static final String ADMIN = "ADMIN";
	public static final String LIBRARIAN = "LIBRARIAN";
	public static final String READER = "READER";

	private String authority;
}
