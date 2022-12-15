package com.tarbus.services;

import com.tarbus.models.ERole;
import com.tarbus.models.User;

import java.util.List;

public interface UserService {
  User getUserInfo(String id);
  List<User> getUsersByRole(List<ERole> roles);
}
