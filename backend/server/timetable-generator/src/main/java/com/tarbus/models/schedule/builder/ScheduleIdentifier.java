package com.tarbus.models.schedule.builder;

import com.google.common.base.Objects;
import com.tarbus.models.RouteHtmlVariables;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleIdentifier {
  final RouteHtmlVariables routeHtmlVariables;

  public ScheduleIdentifier(RouteHtmlVariables routeHtmlVariables) {
    this.routeHtmlVariables = routeHtmlVariables;
  }

  String getTitle() {
    String companyName = routeHtmlVariables.getCompany().getName().replaceAll(" ", "_");
    return  companyName + "-" + routeHtmlVariables.getBusLine().getName() + "-" + routeHtmlVariables.getVersion().getId() + "-" + routeHtmlVariables.getRoute().getId();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ScheduleIdentifier)) return false;
    ScheduleIdentifier that = (ScheduleIdentifier) o;
    return Objects.equal(routeHtmlVariables.getBusLine().getId(), that.routeHtmlVariables.getBusLine().getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(routeHtmlVariables.getBusLine().getId());
  }
}
