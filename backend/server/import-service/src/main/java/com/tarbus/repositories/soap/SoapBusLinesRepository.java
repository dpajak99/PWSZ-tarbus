package com.tarbus.repositories.soap;

import com.tarbus.models.SoapBusLine;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import com.tarbus.models.SoapModel;
import com.tarbus.utilities.SoapClient;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.util.ArrayList;
import java.util.List;

@Component
public class SoapBusLinesRepository extends SoapModel {
  public List<SoapBusLine> fetchAll(int timetableVersion) throws SOAPException {
    init();
    SOAPElement params = soapBody.addChildElement("getRoutes", "", SoapClient.soapNamespace);
    SOAPElement pTimetableVersion = params.addChildElement("pTimetableVersion");
    pTimetableVersion.setTextContent( String.valueOf(timetableVersion));
    SOAPMessage response = SoapClient.callSoapWebService(soapMessage, "RG.Cnr2ApiTarnow/Cnr2ApiTarnow/getRoutes");

    List<SoapBusLine> busLines = new ArrayList<>();
    NodeList busLinesNodes = response.getSOAPBody().getFirstChild().getFirstChild().getChildNodes();
    for( int i = 0; i < busLinesNodes.getLength(); i++) {
      busLines.add(new SoapBusLine(busLinesNodes.item(i)));
    }
    return busLines;
  }
}
