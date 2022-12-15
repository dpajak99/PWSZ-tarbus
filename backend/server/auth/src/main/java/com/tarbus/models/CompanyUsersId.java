package com.tarbus.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class CompanyUsersId implements Serializable {
  @OneToOne()
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "company_id", referencedColumnName = "id")
  private Company company;

  @OneToOne()
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;
}
