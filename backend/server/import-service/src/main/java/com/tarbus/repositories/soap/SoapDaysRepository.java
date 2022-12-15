package com.tarbus.repositories.soap;

import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import com.tarbus.models.SoapDay;
import com.tarbus.models.SoapModel;
import com.tarbus.utilities.SoapClient;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.util.ArrayList;
import java.util.List;

@Component
public class SoapDaysRepository extends SoapModel {
  public List<SoapDay> fetchAll(int timetableVersion) throws SOAPException {
    init();
    SOAPElement params = soapBody.addChildElement("getDays", "", SoapClient.soapNamespace);
    SOAPElement pTimetableVersion = params.addChildElement("pTimetableVersion");
    pTimetableVersion.setTextContent( String.valueOf(timetableVersion));
    SOAPMessage response = SoapClient.callSoapWebService(soapMessage, "RG.Cnr2ApiTarnow/Cnr2ApiTarnow/getDays");

    List<SoapDay> days = new ArrayList<>();
    NodeList daysNodes = response.getSOAPBody().getFirstChild().getFirstChild().getChildNodes();
    for( int i = 0; i < daysNodes.getLength(); i++) {
      days.add(new SoapDay(daysNodes.item(i)));
    }
    return days;
  }
}
