package com.tarbus.exceptions;

public class FileNotFoundException extends RuntimeException {
  public FileNotFoundException(String alert) {
    super(alert);
  }
}