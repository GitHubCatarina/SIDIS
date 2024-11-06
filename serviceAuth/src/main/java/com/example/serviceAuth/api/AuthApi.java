package com.example.serviceAuth.api;

import com.example.serviceAuth.userManagement.api.UserView;
import com.example.serviceAuth.userManagement.api.UserViewMapper;
import com.example.serviceAuth.userManagement.dto.UserDTO;
import com.example.serviceAuth.userManagement.model.User;
import com.example.serviceAuth.userManagement.services.CreateUserRequest;
import com.example.serviceAuth.userManagement.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import com.example.serviceAuth.userManagement.sync.SyncRequest;
import org.springframework.web.client.RestTemplate;


import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;


@Tag(name = "Authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/auth")
public class AuthApi {
	private final AuthenticationManager authenticationManager;

	private final JwtEncoder jwtEncoder;

	private final JwtDecoder jwtDecoder;

	private final UserViewMapper userViewMapper;

	private final UserService userService;
	private final RestTemplate restTemplate;
	@Value("${webhook.url}")
	private String webhookUrl;

	@PostMapping("login")
	public ResponseEntity<UserView> login(@RequestBody @Valid final AuthRequest request) {
		try {
			final Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			final User user = (User) authentication.getPrincipal();

			final Instant now = Instant.now();
			//final long expiry = 1200L; // 2 minutes
			final long expiry = 2592000L; // 1 month is usually too long for a token to be valid. adjust for production
			final String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(joining(" "));

			final JwtClaimsSet claims = JwtClaimsSet.builder().issuer("example.io").issuedAt(now)
					.expiresAt(now.plusSeconds(expiry)).subject(format("%s,%s", user.getId(), user.getUsername()))
					.claim("roles", scope).build();

			final String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

			return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body(userViewMapper.toUserView(user));
		} catch (final BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@PostMapping("register")
	public UserView register(@RequestBody @Valid final CreateUserRequest request) {
		System.out.println("Request to register user: " + request);

		// Service I1
		final var user = userService.create(request);
		if (user.getId() == null) {
			throw new IllegalStateException("User ID não pode ser nulo para sincronização.");
		}

		System.out.println("feito userService.create: " + request);
		System.out.println("Usuário criado, iniciando sincronização com outra instância...");

		// Manda para I2
		sincronizarComOutraInstancia(user);

		return userViewMapper.toUserView(user);
	}

	@GetMapping("/roles")
	public ResponseEntity<List<String>> getUserRoles(@RequestHeader("Authorization") String authorization) {
		String token = authorization.replace("Bearer ", "");
		System.out.println("Este é o token: " + token);

		Jwt jwt = jwtDecoder.decode(token);
		String subject = jwt.getClaimAsString("sub");
		System.out.println("Este é o subject completo: " + subject);

		// To separate id from username
		String username = subject.split(",")[1];
		System.out.println("Este é o username extraído: " + username);


		User user = userService.loadUserByUsername(username); // Search user by username
		System.out.println("Este é o user q saiu: " + user);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		List<String> roles = user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		System.out.println("Estes são os roles: " + roles);

		return ResponseEntity.ok(roles);
	}


	@Retryable(
			value = { Exception.class },
			maxAttempts = 5,
			backoff = @Backoff(delay = 2000) // Intervalo de 2 segundos entre tentativas
	)
	public void sincronizarComOutraInstancia(User user) {

		UserDTO userDTO = toUserDTO(user);
		if (user.getUsername() == null) {
			throw new IllegalArgumentException("O username do user não deve ser nulo");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		// Criar o SyncRequest com os dados do User
		SyncRequest syncRequest = new SyncRequest();
		syncRequest.setId(user.getId());
		syncRequest.setResource(userDTO);
		syncRequest.setDesiredVersion(0);

		// Enviar o SyncRequest
		HttpEntity<SyncRequest> requestEntity = new HttpEntity<>(syncRequest, headers);

		try {
			restTemplate.exchange(
					webhookUrl + "/webhook/sync",
					HttpMethod.PUT,
					requestEntity,
					Void.class
			);
			System.out.println("Sincronização (criação ou atualização) bem-sucedida com a outra instância.");
		} catch (Exception e) {
			System.out.println("Erro na sincronização. Tentando novamente... " + e.getMessage());
			throw e; // Relança a exceção para ativar nova tentativa
		}
	}
	private UserDTO toUserDTO(User user) {
		return new UserDTO(
				user.getId(),
				user.getVersion(),
				user.getCreatedAt(),
				user.getModifiedAt(),
				user.getCreatedBy(),
				user.getModifiedBy(),
				user.isEnabled(),
				user.getUsername(),
				user.getPassword(),  // Incluir apenas se necessário
				user.getFullName(),
				user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet())  // Corrigido para usar getAuthority()
		);
	}
}
