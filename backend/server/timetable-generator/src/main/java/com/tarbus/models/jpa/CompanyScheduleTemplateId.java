package com.tarbus.models.jpa;

import com.tarbus.models.Company;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class CompanyScheduleTemplateId implements Serializable {
  @OneToOne()
  @JoinColumn(name = "company_id", referencedColumnName = "id")
  private Company company;

  @OneToOne()
  @Enumerated(EnumType.STRING)
  @JoinColumn(name = "template_id", referencedColumnName = "id")
  private ScheduleTemplate template;

  @Override
  public String toString() {
    return "CompanyScheduleTemplateId{" +
      "company=" + company +
      ", scheduleTemplate=" + template +
      '}';
  }
}