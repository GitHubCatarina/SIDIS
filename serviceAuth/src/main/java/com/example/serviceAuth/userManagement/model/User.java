package com.example.serviceAuth.userManagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "T_USER")
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Getter
	@Setter
	private Long id;

	@Version
	@Getter
	private Long version;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	@Getter
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	@Getter
	private LocalDateTime modifiedAt;

	@CreatedBy
	@Column(nullable = false, updatable = false)
	@Getter
	private String createdBy;

	@LastModifiedBy
	@Column(nullable = false)
	@Getter
	private String modifiedBy;

	@Setter
	@Getter
	private boolean enabled = true;

	@Column(unique = true, updatable = false, nullable = false)
	@Getter
	@Email
	@NotNull
	@NotBlank
	private String username;

	@Column(nullable = false)
	@Getter
	@NotNull
	@NotBlank
	private String password;

	@Getter
	@Setter
	private String fullName;

	@ElementCollection
	@Getter
	private final Set<Role> authorities = new HashSet<>();

	public User() {}

	public User(final String username, final String password) {
		this.username = username;
		setPassword(password);
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public User(final String username, final String password, final String fullName) {
		setUsername(username);
		setPassword(password);
		this.fullName = fullName;
	}

	public static User newUser(final String username, final String password, final String fullName) {
		final var u = new User(username, password);
		u.setFullName(fullName);
		return u;
	}

	public static User newUser(final String username, final String password, final String fullName, final String role) {
        var u = newUser(username, password, fullName);
		u.addAuthority(new Role(role));
		return u;
	}

	public void setPassword(final String password) {
		this.password = Objects.requireNonNull(password);
	}

	public void addAuthority(final Role r) {
		authorities.add(r);
	}

	@Override
	public boolean isAccountNonExpired() {
		return isEnabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		return isEnabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isEnabled();
	}
}