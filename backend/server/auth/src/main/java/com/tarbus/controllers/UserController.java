package com.tarbus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.tarbus.models.User;
import com.tarbus.repositories.jpa.RoleRepository;
import com.tarbus.repositories.jpa.UsersRepository;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
  UsersRepository usersRepository;

	@Autowired
  RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@PostMapping()
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<User> updateUser(@RequestBody User user) {
    User fullUser = null;
	  if(user.getId() != null) {
      Optional<User> userData = usersRepository.findById(user.getId());
      if (userData.isPresent()) {
        fullUser = userData.get();
      }
    } else {
	    fullUser = new User();
    }
		if (fullUser != null) {
			if( user.getFirstName() != null ) {
			  fullUser.setFirstName(user.getFirstName());
      }
      if( user.getLastName() != null ) {
        fullUser.setLastName(user.getLastName());
      }
      if( user.getEmail() != null ) {
        fullUser.setEmail(user.getEmail());
      }
      if( user.getRoles() != null ) {
        fullUser.setRoles(user.getRoles());
      }
      if( user.getPassword() != null ) {
        fullUser.setPassword(encoder.encode(user.getPassword()));
      }
			return new ResponseEntity<>(usersRepository.save(fullUser), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
