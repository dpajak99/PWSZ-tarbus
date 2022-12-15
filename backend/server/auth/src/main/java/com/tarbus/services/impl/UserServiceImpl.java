package com.tarbus.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tarbus.models.ERole;
import com.tarbus.models.Role;
import com.tarbus.models.User;
import com.tarbus.repositories.jpa.RoleRepository;
import com.tarbus.repositories.jpa.UserRepository;
import com.tarbus.services.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  @Autowired
  RoleRepository roleRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  @Override
  public User getUserInfo(String id) {
    return userRepository.getById(id);
  }

  @Override
  public List<User> getUsersByRole(List<ERole> roles) {
    Set<User> users = new HashSet<>();
    for(ERole role : roles ) {
      Role findRole = roleRepository.findByName(role).get();
      users.addAll(userRepository.getUsersByRolesContains(findRole));
    }
    return new ArrayList<>(users);
  }
}
