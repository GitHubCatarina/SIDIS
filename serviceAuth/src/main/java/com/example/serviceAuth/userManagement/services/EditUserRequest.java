package com.example.serviceAuth.userManagement.services;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditUserRequest {
	@NotBlank
	private String fullName;

	private Set<String> authorities;
}
