package com.tarbus.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tarbus.models.Role;
import com.tarbus.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByEmail(String email);

	Boolean existsByEmail(String email);

	List<User> getUsersByRolesContains(Role role);
}
