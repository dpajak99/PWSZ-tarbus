package com.tarbus.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "company_users")
@Getter
@Setter
@NoArgsConstructor
public class CompanyUsers {
  @EmbeddedId
  private CompanyUsersId companyUsersId;

  public CompanyUsers(CompanyUsersId companyUsersId) {
    this.companyUsersId = companyUsersId;
  }

}
