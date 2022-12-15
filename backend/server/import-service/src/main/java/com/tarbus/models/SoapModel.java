package com.tarbus.models;

import javax.xml.soap.*;

public abstract class SoapModel {
  protected SOAPMessage soapMessage;
  protected SOAPPart soapPart;
  protected SOAPBody soapBody;
  protected SOAPHeader header;
  protected SOAPEnvelope envelope;

  public void init() throws SOAPException {
    MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
    soapMessage = messageFactory.createMessage();
    header = soapMessage.getSOAPHeader();
    soapPart = soapMessage.getSOAPPart();
    envelope = soapPart.getEnvelope();
    soapBody = envelope.getBody();

    envelope.removeNamespaceDeclaration(envelope.getPrefix());
    envelope.addNamespaceDeclaration("", "");
    envelope.setPrefix("");
    header.setPrefix("");
    soapBody.setPrefix("");
  }
}
