package com.tarbus.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccessToken {
  private String token;
  private String type;

  public AccessToken(String token, String type) {
    this.token = token;
    this.type = type;
  }
}
