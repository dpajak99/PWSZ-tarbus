package com.tarbus.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(	name = "users", 
		uniqueConstraints = {
			@UniqueConstraint(columnNames = "email") 
		})
@Getter
@Setter
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	private String firstName;
	private String lastName;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

  @ManyToMany()
	@OnDelete(action = OnDeleteAction.CASCADE)
  @JoinTable(	name = "company_users",
    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"),
		foreignKey = @ForeignKey(name = "company_user_fk")
	)
  private Set<Company> companies = new java.util.LinkedHashSet<>();

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", roles=" + roles +
				'}';
	}

	public Map<String, Object> toPublicData() {
		Map<String, Object> publicUserData = new HashMap<>();
		publicUserData.put("firstName", firstName);
		publicUserData.put("lastName", lastName);
		return publicUserData;
	}
}
