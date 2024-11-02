package com.example.serviceAuth.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
	@NotNull
	@Email
	private String username;

	@NotNull
	private String password;
}