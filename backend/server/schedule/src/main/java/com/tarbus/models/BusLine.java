package com.tarbus.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bus_lines", schema="schedule")
public class BusLine implements DatabaseObject {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "lp")
  private int lp;
  @Column(name = "name")
  private String name;
  @Column(name = "comments")
  private String comments;
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "version_id", referencedColumnName = "id")
  private ScheduleVersion version;

  public BusLine(Long id) {
    this.id = id;
  }

  public BusLine(Long id, String name, String comments, ScheduleVersion version) {
    this.id = id;
    this.name = name;
    this.comments = comments;
    this.version = version;
  }

  @Override
  public Object getPropertyByParam(String param) {
    switch (param) {
      case "id":
        return id;
      case "name":
        return name;
      case "comments":
        return comments;
      case "version_id":
        return version.getId();
    }
    return null;
  }

  @Override
  public String toString() {
    return "BusLine{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", comments='" + comments + '\'' +
      ", version=" + version +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BusLine)) return false;
    BusLine busLine = (BusLine) o;
    return Objects.equals(name, busLine.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
