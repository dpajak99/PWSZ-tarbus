package com.tarbus.payload.response;

import com.tarbus.models.AccessToken;
import com.tarbus.models.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponse {
	private AccessToken token;
	private String id;
	private String email;
	private String firstName;
	private String lastName;
	private Set<Role> roles;

	public JwtResponse(AccessToken accessToken, String id, String email, String firstName,  String lastName,Set<Role> roles) {
		this.token = accessToken;
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roles = roles;
	}
}
