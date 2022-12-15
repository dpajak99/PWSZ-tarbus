package com.tarbus.repositories.soap;

import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import com.tarbus.models.SoapModel;
import com.tarbus.models.SoapRouteVariant;
import com.tarbus.utilities.SoapMpkClient;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SoapRouteAndVariantsRepository extends SoapModel {
  public List<SoapRouteVariant> fetchAll(int timetableVersion, String lineName) throws SOAPException, IOException {
    init();
    SOAPElement params = soapBody.addChildElement("GetRouteAndVariants", "", SoapMpkClient.soapNamespace);
    SOAPElement pTimetableVersion = params.addChildElement("id_wersja");
    pTimetableVersion.setTextContent( String.valueOf(timetableVersion));
    SOAPElement pLineName = params.addChildElement("q");
    pLineName.setTextContent( lineName);
    SOAPMessage response = SoapMpkClient.callSoapWebService(soapMessage, "http://PublicService/GetRouteAndVariants");

    List<SoapRouteVariant> routeVariants = new ArrayList<>();
    NodeList routeVariantsNodes = response.getSOAPBody().getFirstChild().getFirstChild().getFirstChild().getChildNodes();
    for( int i = 0; i < routeVariantsNodes.getLength(); i++) {
      routeVariants.add(new SoapRouteVariant(routeVariantsNodes.item(i)));
    }
    return routeVariants;
  }
}
