package com.tarbus.repositories.soap;

import org.springframework.stereotype.Component;
import com.tarbus.models.SoapModel;
import com.tarbus.models.TimeTableInfo;
import com.tarbus.utilities.SoapClient;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

@Component
public class TimeTableInfoRepository extends SoapModel {
  public TimeTableInfo getTimeTableInfo() throws SOAPException {
    init();
    soapBody.addChildElement("getTimeTableInfo", "", SoapClient.soapNamespace);
    SOAPMessage response = SoapClient.callSoapWebService(soapMessage, "RG.Cnr2ApiTarnow/Cnr2ApiTarnow/getTimeTableInfo");

    TimeTableInfo timeTableInfo = new TimeTableInfo(response.getSOAPBody().getFirstChild().getFirstChild().getChildNodes().item(1).getFirstChild());
    return timeTableInfo;
  }
}
