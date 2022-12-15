package com.tarbus.models.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "bus_line_schedule_templates", schema = "schedule")
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class BusLineScheduleTemplate implements Serializable {
  @EmbeddedId
  private BusLineScheduleTemplateId id;

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