package com.tarbus.repositories.soap;

import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import com.tarbus.models.SoapModel;
import com.tarbus.models.SoapNote;
import com.tarbus.utilities.SoapClient;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.util.ArrayList;
import java.util.List;

@Component
public class SoapNotesRepository extends SoapModel {
  public List<SoapNote> fetchAll(int timetableVersion) throws SOAPException {
    init();
    SOAPElement params = soapBody.addChildElement("getNotes", "", SoapClient.soapNamespace);
    SOAPElement pTimetableVersion = params.addChildElement("pTimetableVersion");
    pTimetableVersion.setTextContent( String.valueOf(timetableVersion));
    SOAPMessage response = SoapClient.callSoapWebService(soapMessage, "RG.Cnr2ApiTarnow/Cnr2ApiTarnow/getNotes");

    List<SoapNote> notes = new ArrayList<>();
    NodeList notesNodes = response.getSOAPBody().getFirstChild().getFirstChild().getChildNodes();
    for( int i = 0; i < notesNodes.getLength(); i++) {
      notes.add(new SoapNote(notesNodes.item(i)));
    }
    return notes;
  }
}
