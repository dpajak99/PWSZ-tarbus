package com.tarbus.models.templates;

import com.tarbus.models.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class KM001SKATRAINBUS {
  public String busStopName;
  private String busLineName;
  private String destinationName;
  private String dateStart;
  private String dateEnd;
  private List<SkaBusStop> nextStops;
  private DepartureHolder busDepartureHolder;
  private DepartureHolder trainDepartureHolder;
  private List<SkaDayDescription> busDescriptions;
  private List<SkaDayDescription> trainDescriptions;
  private List<String> destinations;

  private String imgReplacementSignUrl;
  private String imgLogoUrl;
  private String imgBus;
  private String imgQR;
  private String imgTrain;
  
  public KM001SKATRAINBUS(TableBusStopData tableBusStopData) {
    RouteHtmlVariables busHtmlVariables = tableBusStopData.getRoutes().get(0);
    RouteHtmlVariables trainHtmlVariables = tableBusStopData.getRoutes().size() > 1 ? tableBusStopData.getRoutes().get(1) : null;
    
    List<String> comments = new ArrayList<>(List.of(busHtmlVariables.getRoute().getComments().split("\\|")));
    this.busStopName = tableBusStopData.getBusStop().getName();
    this.busLineName = comments.get(0);
    this.destinationName = busHtmlVariables.getRoute().getName();
    this.dateStart = busHtmlVariables.getRoute().getDateStart();
    this.dateEnd = busHtmlVariables.getRoute().getDateEnd();
    this.busDepartureHolder = busHtmlVariables.getDepartures();
    this.trainDepartureHolder = trainHtmlVariables != null ? trainHtmlVariables.getDepartures() : new DepartureHolder(true);
    this.imgReplacementSignUrl = busHtmlVariables.getDriveUrl() + "ska_sign_no_border.png";
    this.imgLogoUrl = busHtmlVariables.getDriveUrl() + "logo_hor.png";
    this.imgBus = busHtmlVariables.getDriveUrl() + "ska_bus.png";
    this.imgTrain = busHtmlVariables.getDriveUrl() + "ska_train.png";
    this.imgQR = busHtmlVariables.getDriveUrl() + comments.get(1) + ".jpg";
    this.destinations = tableBusStopData.getDestinations();
    this.busDescriptions = new ArrayList<>();
    this.trainDescriptions = new ArrayList<>();
//    Jasło -> Nowy Sącz 
    if (busHtmlVariables.getRoute().getId() == 277L) {
      this.busDescriptions.add(new SkaDayDescription("10.XI, 18.XI, 25.XI", null, "50%", busDepartureHolder.getRo()));
      this.trainDescriptions.add(new SkaDayDescription("10.XI, 18.XI, 25.XI", null, "50%", trainDepartureHolder.getEmptyList()));
    }

//    Nowy Sącz -> Jasło 
    if (busHtmlVariables.getRoute().getId() == 276L) {
      this.busDescriptions.add(new SkaDayDescription("6.XI, 13.XI, 20.XI, 27.XI", null, "50%", busDepartureHolder.getRo()));
      this.trainDescriptions.add(new SkaDayDescription("6.XI, 13.XI, 20.XI, 27.XI", null, "50%", trainDepartureHolder.getEmptyList()));
    }

//    Tarnów -> Jasło
    if (busHtmlVariables.getRoute().getId() == 274L) {
      this.busDescriptions.add(new SkaDayDescription("10.XI, 18.XI, 25.XI", null, "50%", busDepartureHolder.getRo()));
      this.trainDescriptions.add(new SkaDayDescription("10.XI, 18.XI, 25.XI", null, "50%", trainDepartureHolder.getEmptyList()));
    }

//    Jasło -> Tarnów
    if (busHtmlVariables.getRoute().getId() == 275L) {
      this.busDescriptions.add(new SkaDayDescription("6.XI, 13.XI, 20.XI, 27.XI", null, "50%", busDepartureHolder.getRo()));
      this.trainDescriptions.add(new SkaDayDescription("6.XI, 13.XI, 20.XI, 27.XI", null, "50%", trainDepartureHolder.getEmptyList()));
    }

//    Tarnów -> Nowy Sącz -> Tarnów
    if (busHtmlVariables.getRoute().getId() == 272L || busHtmlVariables.getRoute().getId() == 273L) {
      this.busDescriptions.add(new SkaDayDescription("CODZIENNIE", "EVERYDAY", "50%", busDepartureHolder.getRo()));
      this.trainDescriptions.add(new SkaDayDescription("CODZIENNIE", "EVERYDAY", "50%", trainDepartureHolder.getEmptyList()));
    }

    // SKA1P ZKA
    if (busHtmlVariables.getRoute().getId() == 278L || busHtmlVariables.getRoute().getId() == 279L) {
      this.busDescriptions.add(new SkaDayDescription("21.XI", null, "13%", busDepartureHolder.getRo()));
      this.busDescriptions.add(new SkaDayDescription("PONIEDZIAŁEK - PIĄTEK", "22.XI - 05.XII", "24%", busDepartureHolder.getWs()));
      this.busDescriptions.add(new SkaDayDescription("SOBOTY, NIEDZIELE", "26.XI - 4.XII", "13%", busDepartureHolder.getSw()));
      this.trainDescriptions.add(new SkaDayDescription("21.XI", null, "50%", trainDepartureHolder.getRo()));
    }

    // SKA1 ZKA
    if (busHtmlVariables.getRoute().getId() == 280L || busHtmlVariables.getRoute().getId() == 281L) {
      this.busDescriptions.add(new SkaDayDescription("21.XI", null, "25%", busDepartureHolder.getRo()));
      this.busDescriptions.add(new SkaDayDescription("22.XI - 05.XII", null, "25%", busDepartureHolder.getWs()));
      this.trainDescriptions.add(new SkaDayDescription("21.XI", null, "25%", trainDepartureHolder.getRo()));
      this.trainDescriptions.add(new SkaDayDescription("22.XI - 05.XII", null, "25%", trainDepartureHolder.getEmptyList()));
    }

    // SKA2 ZKA
    if (busHtmlVariables.getRoute().getId() == 283L || busHtmlVariables.getRoute().getId() == 285L) {
      this.busDescriptions.add(new SkaDayDescription("07.XI - 03.XII", null, "50%", busDepartureHolder.getRo()));
      this.trainDescriptions.add(new SkaDayDescription("PONIEDZIAŁEK-PIĄTEK", "MONDAY-FRIDAY", "25%", trainDepartureHolder.getRo()));
      this.trainDescriptions.add(new SkaDayDescription("SOBOTY, NIEDZIELE I ŚWIĘTA", "SATURDAY, SUNDAY, HOLIDAY", "25%", trainDepartureHolder.getWs()));
    }


    // SKA3 ZKA - Trzebinia
    if (new ArrayList<>(List.of(288L, 289L, 291L, 290L)).contains(busHtmlVariables.getRoute().getId())) {
//      this.busDescriptions.add(new SkaDayDescription("SOBOTY, NIEDZIELE I ŚWIĘTA OPRÓCZ 24 XII - 8 I", null, "50%", busDepartureHolder.getRo()));
//      this.trainDescriptions.add(new SkaDayDescription("PONIEDZIAŁEK-PIĄTEK ORAZ W DNIACH 24-26 XII, 31 XII, 1 I, 6-8 I", null, "25%", trainDepartureHolder.getRo()));
//      this.trainDescriptions.add(new SkaDayDescription("SOBOTY, NIEDZIELE I ŚWIĘTA OPRÓCZ 24-26 XII, 31 XII, 1 I, 6-8, I", null, "25%", trainDepartureHolder.getWs()));

      this.busDescriptions.add(new SkaDayDescription("CODZIENNIE", "EVERYDAY", "50%", busDepartureHolder.getWs()));
      this.trainDescriptions.add(new SkaDayDescription("CODZIENNIE", "EVERYDAY", "50%", trainDepartureHolder.getSw()));
    }
    
    if (new ArrayList<>(List.of( 292L, 293L, 294L, 295L)).contains(busHtmlVariables.getRoute().getId())) {
      this.busDescriptions.add(new SkaDayDescription("PONIEDZIAŁEK–PIĄTEK W OKRESIE 12-22 XII oraz 9 I – 10 III", null, "50%", busDepartureHolder.getRo()));
      this.trainDescriptions.add(new SkaDayDescription("PONIEDZIAŁEK–PIĄTEK OPRÓCZ ŚWIĄT", null, "25%", trainDepartureHolder.getRo()));
      this.trainDescriptions.add(new SkaDayDescription("SOBOTY, NIEDZIELE I ŚWIĘTA", null, "25%", trainDepartureHolder.getWs()));
    }
    
    this.nextStops = new ArrayList<>();
    for( int i = 0; i < busHtmlVariables.getAllRouteConnections().size(); i++) {
      RouteConnection routeConnection = busHtmlVariables.getAllRouteConnections().get(i);
      if( routeConnection.getLp() >= busHtmlVariables.getRouteConnection().getLp() ) {
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

    public List<SingleDeparture> getDepartures() {
      return departures;
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

    @Override
    public String toString() {
      return "SkaDayDescription{" +
        "namePL='" + namePL + '\'' +
        ", nameEN='" + nameEN + '\'' +
        ", widthPercentage='" + widthPercentage + '\'' +
        ", empty=" + empty +
        ", departures=" + departures +
        '}';
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
