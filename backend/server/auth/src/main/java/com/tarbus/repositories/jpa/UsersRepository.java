package com.tarbus.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tarbus.models.User;

import java.util.List;

public interface UsersRepository extends JpaRepository<User, String> {
	List<User> findByEmailContaining(String email);
}
