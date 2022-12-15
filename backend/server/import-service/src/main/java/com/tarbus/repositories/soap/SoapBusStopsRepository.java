package com.tarbus.repositories.soap;

import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import com.tarbus.models.SoapBusStop;
import com.tarbus.models.SoapModel;
import com.tarbus.utilities.SoapClient;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SoapBusStopsRepository extends SoapModel {
  public List<SoapBusStop> fetchAll(int timetableVersion) throws SOAPException, IOException {
    init();
    SOAPElement params = soapBody.addChildElement("getBusStops", "", SoapClient.soapNamespace);
    SOAPElement pTimetableVersion = params.addChildElement("pTimetableVersion");
    pTimetableVersion.setTextContent( String.valueOf(timetableVersion));
    SOAPMessage response = SoapClient.callSoapWebService(soapMessage, "RG.Cnr2ApiTarnow/Cnr2ApiTarnow/getBusStops");
    List<SoapBusStop> busStops = new ArrayList<>();
    NodeList busStopNodes = response.getSOAPBody().getFirstChild().getFirstChild().getChildNodes();
    for( int i = 0; i < busStopNodes.getLength(); i++) {
      busStops.add(new SoapBusStop(busStopNodes.item(i)));
    }
    return busStops;
  }
}
