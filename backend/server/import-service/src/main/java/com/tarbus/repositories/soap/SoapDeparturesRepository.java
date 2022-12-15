package com.tarbus.repositories.soap;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.tarbus.models.SoapDeparture;
import com.tarbus.models.SoapModel;
import com.tarbus.utilities.SoapClient;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SoapDeparturesRepository extends SoapModel {
  public Map<String, List<SoapDeparture>> fetchByBusStop(int timetableVersion, int busStopId) throws SOAPException {
    init();
    SOAPElement params = soapBody.addChildElement("getBusStopTimetable", "", SoapClient.soapNamespace);

    SOAPElement pTimetableVersion = params.addChildElement("pTimetableVersion");
    pTimetableVersion.setTextContent( String.valueOf(timetableVersion));

    SOAPElement pBusStopId = params.addChildElement("pBusStopId");
    pBusStopId.setTextContent( String.valueOf(busStopId));

    SOAPElement pIncludeCalendar = params.addChildElement("pIncludeCalendar");
    pIncludeCalendar.setTextContent("true");

    SOAPMessage response = SoapClient.callSoapWebService(soapMessage, "RG.Cnr2ApiTarnow/Cnr2ApiTarnow/getBusStopTimetable");

    Map<String, List<SoapDeparture>> departuresByDay = new HashMap<>();
    NodeList departuresByDayNode = response.getSOAPBody().getFirstChild().getFirstChild().getChildNodes().item(1).getChildNodes();

    for( int i = 0; i < departuresByDayNode.getLength(); i++) {
      Node day = departuresByDayNode.item(i);
      String dayName = day.getChildNodes().item(0).getTextContent();
      List<SoapDeparture> departures = new ArrayList<>();
      NodeList departuresNode = day.getChildNodes().item(2).getChildNodes();
      for( int j = 0; j < departuresNode.getLength(); j++ ) {
//        departures.add(new SoapDeparture(departuresNode.item(j)));
      }
      departuresByDay.put(dayName, departures);
    }
    return departuresByDay;
  }
}
