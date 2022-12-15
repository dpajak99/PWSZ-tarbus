package com.tarbus.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PublicUserModel {
  String id;
  String firstName;
  String lastName;
  String email;
  Set<Role> roles;

  public PublicUserModel(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.email = user.getEmail();
    this.roles = user.getRoles();
  }
}
