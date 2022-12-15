package com.tarbus.utilities;

import javax.xml.soap.*;

public class SoapClient {
  public static String soapEndpointUrl = "http://217.117.138.33:8090/cnr2api";
  public static String soapNamespace = "RG.Cnr2ApiTarnow";

  public static SOAPMessage callSoapWebService(SOAPMessage soapMessage, String soapAction) {
    try {
      // Create SOAP Connection
      SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
      SOAPConnection soapConnection = soapConnectionFactory.createConnection();

      // Send SOAP Message to SOAP Server
      SOAPMessage soapRequest = createSOAPRequest(soapMessage, soapAction);
      SOAPMessage soapResponse = soapConnection.call(soapRequest, soapEndpointUrl);
      soapConnection.close();
      return soapResponse;
    } catch (Exception e) {
      System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
      e.printStackTrace();
    }
    return null;
  }

  private static SOAPMessage createSOAPRequest(SOAPMessage soapMessage, String soapAction) throws Exception {
    MimeHeaders headers = soapMessage.getMimeHeaders();
    headers.addHeader("SOAPAction", soapAction);
    soapMessage.saveChanges();
    return soapMessage;
  }
}
