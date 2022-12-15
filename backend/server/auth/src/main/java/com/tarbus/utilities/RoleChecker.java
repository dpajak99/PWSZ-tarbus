package com.tarbus.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.tarbus.models.ERole;
import com.tarbus.models.Role;
import com.tarbus.models.User;
import com.tarbus.repositories.jpa.UserRepository;
import com.tarbus.security.services.UserDetailsImpl;

@Service
public class RoleChecker {
  @Autowired
  UserRepository userRepository;

  public boolean containsRole(ERole searchedRole) {
    User user = getUser();
    for( Role role : user.getRoles() ) {
      if( role.getName() == searchedRole ) {
        return true;
      }
    }
    return false;
  }

  public boolean isAdmin() {
    User user = getUser();
    for( Role role : user.getRoles() ) {
      if( role.getName() == ERole.ROLE_ADMIN ) {
        return true;
      }
    }
    return false;
  }

  public User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    System.out.println(authentication.getPrincipal());
    String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
    return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
  }


}
