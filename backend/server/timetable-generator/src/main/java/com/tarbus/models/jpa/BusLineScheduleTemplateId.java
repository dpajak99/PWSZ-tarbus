package com.tarbus.models.jpa;

import com.tarbus.models.BusLine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class BusLineScheduleTemplateId implements Serializable {
  @OneToOne()
  @JoinColumn(name = "bus_line_id", referencedColumnName = "id")
  private BusLine busLine;

  @OneToOne()
  @Enumerated(EnumType.STRING)
  @JoinColumn(name = "template_id", referencedColumnName = "id")
  private ScheduleTemplate template;

  @Override
  public String toString() {
    return "CompanyScheduleTemplateId{" +
      "busLine=" + busLine +
      ", scheduleTemplate=" + template +
      '}';
  }
}