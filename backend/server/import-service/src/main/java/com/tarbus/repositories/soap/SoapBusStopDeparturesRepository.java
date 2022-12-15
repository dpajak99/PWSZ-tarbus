package com.tarbus.repositories.soap;

import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import com.tarbus.models.SDayDetails;
import com.tarbus.models.SoapModel;
import com.tarbus.utilities.SoapMpkClient;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.util.ArrayList;
import java.util.List;

@Component
public class SoapBusStopDeparturesRepository extends SoapModel {
  public List<SDayDetails> fetchAll(int timetableVersion, long busStopId) throws SOAPException {
    init();
    SOAPElement params = soapBody.addChildElement("GetPlanedDeparutresFullInfo", "", SoapMpkClient.soapNamespace);
    SOAPElement pTimetableVersion = params.addChildElement("id_wersja");
    pTimetableVersion.setTextContent( String.valueOf(timetableVersion));
    SOAPElement pBusStopId = params.addChildElement("id");
    pBusStopId.setTextContent(String.valueOf(busStopId));
    SOAPMessage response = SoapMpkClient.callSoapWebService(soapMessage, "http://PublicService/GetPlanedDeparutresFullInfo");

    List<SDayDetails> dayDetails = new ArrayList<>();
    NodeList routeVariantsNodes = response.getSOAPBody().getFirstChild().getFirstChild().getFirstChild().getFirstChild().getChildNodes();
    for( int i = 0; i < routeVariantsNodes.getLength(); i++) {
      dayDetails.add(new SDayDetails(routeVariantsNodes.item(i)));
    }
    return dayDetails;
  }
}
