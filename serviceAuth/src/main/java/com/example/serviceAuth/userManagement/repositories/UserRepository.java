package com.example.serviceAuth.userManagement.repositories;

import com.example.serviceAuth.exceptions.NotFoundException;
import com.example.serviceAuth.userManagement.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "users")
public interface UserRepository extends UserRepoCustom, CrudRepository<User, Long> {

	@Override
	@CacheEvict(allEntries = true)
	<S extends User> List<S> saveAll(Iterable<S> entities);

	@Override
	@Caching(evict = { @CacheEvict(key = "#p0.id", condition = "#p0.id != null"),
			@CacheEvict(key = "#p0.username", condition = "#p0.username != null") })
	<S extends User> S save(S entity);


	@Override
	@Cacheable
	Optional<User> findById(Long objectId);


	@Cacheable
	default User getById(final Long id) {
		final Optional<User> maybeUser = findById(id);
		return maybeUser.filter(User::isEnabled).orElseThrow(() -> new NotFoundException(User.class, id));
	}

	@Cacheable
	default User getByUsername(final String username) {
		final Optional<User> maybeUser = findByUsername(username);
		return maybeUser.filter(User::isEnabled).orElseThrow(() -> new NotFoundException(User.class, username));
	}

	@Cacheable
	Optional<User> findByUsername(String username);


	boolean existsByUsername(String username);
}

