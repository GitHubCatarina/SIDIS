package com.example.serviceAuth.userManagement.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchRequest<T> {
	@Valid
	@NotNull
    Page page;

	@Valid
	@NotNull
	T query;
}
