package com.tarbus.models;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TemplateSettings {
  /**
   * Contains list of route IDs to create timetable
   */
  private List<Number> routes;

  private List<Number> additionalRoutes = Lists.newArrayList();

  private long companyId;

  /**
   * Defines whether to return the created schedules as zip with default false
   */
  private boolean wantsZip;

  private String customQrImageLink;

  private int routesPerPage = 3;

  private FileSortOption fileSortOption = FileSortOption.nameDesc;
  private TableLinesSortOption tableLinesSortOption = TableLinesSortOption.name;

  public enum FileSortOption {
    nameAsc,
    nameDesc,
  }

  public enum TableLinesSortOption {
    name
  }

  public List<Long> getRoutes() {
    List<Long> longs = new ArrayList<>();
    for( Number routeId : routes ) {
      longs.add(routeId.longValue());
    }
    return longs;
  }

  public List<Long> getAdditionalRoutes() {
    List<Long> longs = new ArrayList<>();
    for( Number routeId : additionalRoutes ) {
      longs.add(routeId.longValue());
    }
    return longs;
  }

  public List<Route> sortRoutes(List<Route> routes) {
    if (fileSortOption == FileSortOption.nameAsc) {
      routes.sort(Comparator.comparing((e) -> e.getBusLine().getName()));
      Collections.reverse(routes);
      return routes;
    } else if (fileSortOption == FileSortOption.nameDesc) {
      routes.sort(Comparator.comparing((e) -> e.getBusLine().getName()));
      return routes;
    }
    return routes;
  }


  @Override
  public String toString() {
    return "TemplateSettings{" +
      "routes=" + routes +
      ", companyId=" + companyId +
      ", wantsZip=" + wantsZip +
      ", customQrImageLink='" + customQrImageLink + '\'' +
      '}';
  }
}
