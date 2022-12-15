package com.tarbus.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor
public class Company implements DatabaseObject {
  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "name")
  private String name;
  @Column(name = "website")
  private String website;
  @Column(name = "phone")
  private String phone;
  @Column(name = "email")
  private String email;
  @Column(name = "timezone")
  private String timezone;
  @Column(name = "lang")
  private String lang;
  @Column(name = "avatar")
  private String avatar;
  @Column(name = "business_card")
  private String businessCard;

  @Override
  public Object getPropertyByParam(String param) {
    switch (param) {
      case "id":
        return id;
      case "name":
        return name;
      case "website":
        return website;
      case "phone":
        return phone;
      case "timezone":
        return timezone;
      case "lang":
        return lang;
      case "avatar":
        return avatar;
      case "business_card":
        return businessCard;
      case "email":
        return email;
    }
    return null;
  }
}
