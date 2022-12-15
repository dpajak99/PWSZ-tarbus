package com.tarbus.models.templates;

import com.tarbus.models.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class KM001SKABUS {
  public String busStopName;
  private String busLineName;
  private String destinationName;
  private String dateStart;
  private String dateEnd;
  private List<SkaBusStop> nextStops;
  private DepartureHolder departureHolder;
  private List<SkaDayDescription> descriptions;
  private List<String> destinations;
  
  private String imgReplacementSignUrl;
  private String imgLogoUrl;
  
  private boolean ignoreSymbols;
  
  public KM001SKABUS(RouteHtmlVariables routeHtmlVariables) {
    List<String> comments = new ArrayList<>(List.of(routeHtmlVariables.getRoute().getComments().split("\\|")));
    this.busStopName = routeHtmlVariables.getBusStop().getName();
    this.busLineName = comments.get(0);
    this.destinationName = routeHtmlVariables.getRoute().getName();
    this.dateStart = routeHtmlVariables.getRoute().getDateStart();
    this.dateEnd = routeHtmlVariables.getRoute().getDateEnd();
    this.departureHolder = routeHtmlVariables.getDepartures();
    
    this.imgReplacementSignUrl = routeHtmlVariables.getDriveUrl() + "ska_sign_no_border.png";
    this.imgLogoUrl = routeHtmlVariables.getDriveUrl() + "logo_hor.png";
    this.destinations = routeHtmlVariables.getDestinations();
    this.descriptions = new ArrayList<>();
//    Jasło -> Nowy Sącz 
    if (routeHtmlVariables.getRoute().getId() == 277L) {
      this.descriptions.add(new SkaDayDescription("10.XI, 18.XI, 25.XI", null, "100%", departureHolder.getRo()));
    }

//    Nowy Sącz -> Jasło 
    if (routeHtmlVariables.getRoute().getId() == 276L) {
      this.descriptions.add(new SkaDayDescription("6.XI, 13.XI, 20.XI, 27.XI", null, "100%", departureHolder.getRo()));
    }

//    Tarnów -> Jasło
    if (routeHtmlVariables.getRoute().getId() == 274L) {
      this.descriptions.add(new SkaDayDescription("10.XI, 18.XI, 25.XI", null, "100%", departureHolder.getRo()));
    }

//    Jasło -> Tarnów
    if (routeHtmlVariables.getRoute().getId() == 275L) {
      this.descriptions.add(new SkaDayDescription("6.XI, 13.XI, 20.XI, 27.XI", null, "100%", departureHolder.getRo()));
    }

//    Tarnów -> Nowy Sącz -> Tarnów
    if (routeHtmlVariables.getRoute().getId() == 272L || routeHtmlVariables.getRoute().getId() == 273L) {
      this.descriptions.add(new SkaDayDescription("CODZIENNIE", "EVERYDAY", "100%", departureHolder.getRo()));
    }

    // SKA1P ZKA
    if (routeHtmlVariables.getRoute().getId() == 278L || routeHtmlVariables.getRoute().getId() == 279L) {
      this.descriptions.add(new SkaDayDescription("21.XI", null, "30%", departureHolder.getRo()));
      this.descriptions.add(new SkaDayDescription("PONIEDZIAŁEK - PIĄTEK", "22.XI - 05.XII", "40%", departureHolder.getWs()));
      this.descriptions.add(new SkaDayDescription("SOBOTY, NIEDZIELE", "26.XI - 4.XII", "30%", departureHolder.getSw()));
    }

    // SKA1 ZKA
    if (routeHtmlVariables.getRoute().getId() == 280L || routeHtmlVariables.getRoute().getId() == 281L) {
      this.descriptions.add(new SkaDayDescription("21.XI", null, "50%", departureHolder.getRo()));
      this.descriptions.add(new SkaDayDescription("22.XI - 05.XII", null, "50%", departureHolder.getWs()));
    }

    // SKA2 ZKA
    if (routeHtmlVariables.getRoute().getId() == 284L || routeHtmlVariables.getRoute().getId() == 285L) {
      this.descriptions.add(new SkaDayDescription("07.XI - 03.XII", null, "100%", departureHolder.getRo()));
    }

    // SKA3 ZKA - Trzebinia
    if (new ArrayList<>(List.of(288L, 289L)).contains(routeHtmlVariables.getRoute().getId())) {
//      this.descriptions.add(new SkaDayDescription("SOBOTY, NIEDZIELE I ŚWIĘTA OPRÓCZ 24 XII - 8 I", "SATURDAYS, SUNDAYS AND HOLIDAYS EXCEPT 24 XII - 8 I", "100%", departureHolder.getRo()));
      this.descriptions.add(new SkaDayDescription("CODZIENNIE", "EVERYDAY", "100%", departureHolder.getWs()));
    }

    // SKA3 ZKA - Podłęże
    if (new ArrayList<>(List.of(292L, 293L)).contains(routeHtmlVariables.getRoute().getId())) {
      this.descriptions.add(new SkaDayDescription("PONIEDZIAŁEK–PIĄTEK W OKRESIE 12-22 XII oraz 9 I – 10 III", "MONDAY–FRIDAY 12-22 XII and 9 I – 10 III", "100%", departureHolder.getRo()));
    }

//    if (new ArrayList<>(List.of( 292L, 293L, 294L, 295L)).contains(routeHtmlVariables.getRoute().getId())) {
//      this.descriptions.add(new SkaDayDescription("06.XI - 10.XII", null, "100%", departureHolder.getRo()));
//    }
//    
    this.ignoreSymbols = new ArrayList<>(List.of(270L, 271L, 265L)).contains(routeHtmlVariables.getRoute().getId());
    
    this.nextStops = new ArrayList<>();
    for( int i = 0; i < routeHtmlVariables.getAllRouteConnections().size(); i++) {
      RouteConnection routeConnection = routeHtmlVariables.getAllRouteConnections().get(i);
      if( routeConnection.getLp() >= routeHtmlVariables.getRouteConnection().getLp() ) {
        int localLp = nextStops.size() + 1;
        nextStops.add(new SkaBusStop(localLp, routeConnection));
      }
      if( nextStops.size() == 7 ) {
        break;
      }
    }
    
  }

  @Getter
  @Setter
  public static class SkaDayDescription {
    private String namePL;
    private String nameEN;
    private String widthPercentage;
    private boolean empty;
    
    private List<SingleDeparture> departures;


    public SkaDayDescription(String namePL, String nameEN, String widthPercentage, List<SingleDeparture> departures) {
      this.namePL = namePL;
      this.nameEN = nameEN;
      this.widthPercentage = widthPercentage;
      this.departures = departures;
      this.empty = departures.isEmpty();
    }

    public List<SingleDeparture> getDepartures(int hour) {
      List<SingleDeparture> result = new ArrayList<>();
      for( SingleDeparture singleDeparture : departures ) {
        if( singleDeparture.getHourInt() == hour ) {
          result.add(singleDeparture);
        }
      }
      return result;
    }
  }

  @Getter
  @Setter
  public static class SkaBusStop {
    private int lp;
    private String name;
    private String description;

    public SkaBusStop( int lp, RouteConnection routeConnection ) {
      this.lp = lp;
      this.name = routeConnection.getBusStop().getName();
      this.description = routeConnection.getDescription();
    }
  }
}
