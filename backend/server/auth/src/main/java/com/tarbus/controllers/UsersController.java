package com.tarbus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.tarbus.models.PublicUserModel;
import com.tarbus.models.Role;
import com.tarbus.models.User;
import com.tarbus.repositories.jpa.RoleRepository;
import com.tarbus.repositories.jpa.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class UsersController {

  @Autowired
  UsersRepository usersRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @GetMapping("/users")
//  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<PublicUserModel>> getAllUsers(@RequestParam(required = false) String name, @RequestParam() int page) {
    try {
      List<User> users = new ArrayList<>();

      if (name == null)
        users.addAll(usersRepository.findAll());
      else
        users.addAll(usersRepository.findByEmailContaining(name));

      int elementsOnPage = 20;
      int pageNumber = page * elementsOnPage;
      if( pageNumber > users.size() ) {
        if( pageNumber + elementsOnPage < users.size() ) {
          users = users.subList(pageNumber, users.size() - 1);
        } else {
          users = users.subList(pageNumber, pageNumber + elementsOnPage - 1);
        }
      }
      List<PublicUserModel> publicUserModel = new ArrayList<>();
      for (User user : users) {
        publicUserModel.add(new PublicUserModel(user));
      }

      return new ResponseEntity<>(publicUserModel, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/users/{id}")
//  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
    Optional<User> userData = usersRepository.findById(id);

    return userData.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping("/users/{id}")
//  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody User user) {
    Optional<User> userData = usersRepository.findById(id);

    if (userData.isPresent()) {
      User _user = userData.get();
      _user.setEmail(user.getEmail());
      _user.setRoles(user.getRoles());

      return new ResponseEntity<>(usersRepository.save(_user), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/usersPassword/{id}")
//  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<User> updateUserPassword(@PathVariable("id") String id, @RequestBody User user) {
    Optional<User> userData = usersRepository.findById(id);

    if (userData.isPresent()) {
      User _user = userData.get();
      _user.setPassword(encoder.encode(user.getPassword()));

      return new ResponseEntity<>(usersRepository.save(_user), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/users/{id}")
//  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") String id) {
    try {
      usersRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }
  }

  @GetMapping("/roles")
//  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<Role>> getRoles() {
    try {
      List<Role> roles = roleRepository.findAll();
      return new ResponseEntity<>(roles, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }
  }
}
