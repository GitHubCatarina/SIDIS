package com.example.serviceAuth.userManagement.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserView {

	private String id;

	private String username;
	private String fullName;
}
