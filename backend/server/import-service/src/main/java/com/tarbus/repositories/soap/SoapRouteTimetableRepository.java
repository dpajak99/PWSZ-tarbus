package com.tarbus.repositories.soap;

import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import com.tarbus.models.SoapDeparture;
import com.tarbus.models.SoapModel;
import com.tarbus.utilities.SoapClient;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SoapRouteTimetableRepository extends SoapModel {
  public List<SoapDeparture> getRouteTimetable(int timetableVersion, String routeName) throws SOAPException {
    init();
    SOAPElement params = soapBody.addChildElement("getRouteTimetable", "", SoapClient.soapNamespace);

    SOAPElement pTimetableVersion = params.addChildElement("pTimetableVersion");
    pTimetableVersion.setTextContent( String.valueOf(timetableVersion));

    SOAPElement pRouteNumber = params.addChildElement("pRouteNumber");
    pRouteNumber.setTextContent(routeName);

    SOAPMessage response = SoapClient.callSoapWebService(soapMessage, "RG.Cnr2ApiTarnow/Cnr2ApiTarnow/getRouteTimetable");

    NodeList testNodes = response.getSOAPBody().getFirstChild().getFirstChild().getChildNodes().item(1).getFirstChild().getFirstChild().getChildNodes();
    List<SoapDeparture> allSoapDepartures = new ArrayList<>();
    for( int i = 0; i < testNodes.getLength(); i++) {
      NodeList daysNode = testNodes.item(i).getChildNodes();
      NodeList coursesNode = daysNode.item(0).getChildNodes();

      String dayId = daysNode.item(1).getTextContent();
      for( int j = 0; j < coursesNode.getLength(); j++ ) {
        NodeList courseNode = coursesNode.item(j).getChildNodes();
        String courseId = courseNode.item(0).getTextContent();
        String direction = courseNode.item(2).getTextContent();
        String driverNotes = courseNode.item(2).getTextContent();
        String passengerNotes = courseNode.item(3).getTextContent();
        NodeList departuresNodes = courseNode.item(1).getChildNodes();
        Set<SoapDeparture> soapDepartures = new HashSet<>();
        if (departuresNodes != null) {
          for( int k = 0; k < departuresNodes.getLength(); k++ ) {
            soapDepartures.add(new SoapDeparture(departuresNodes.item(k), dayId, courseId, direction, passengerNotes));
          }
        }
        allSoapDepartures.addAll(new ArrayList<>(soapDepartures));
      }
    }
    return allSoapDepartures;
  }
}
