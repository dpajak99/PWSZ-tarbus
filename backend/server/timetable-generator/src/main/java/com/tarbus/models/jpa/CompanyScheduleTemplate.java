package com.tarbus.models.jpa;

import com.tarbus.models.Company;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "company_schedule_templates", schema = "schedule")
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class CompanyScheduleTemplate implements Serializable {
  @EmbeddedId
  private CompanyScheduleTemplateId id;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private TemplateStatus status;

  @Override
  public String toString() {
    return "CompanyScheduleTemplate{" +
      "id=" + id +
      '}';
  }
}