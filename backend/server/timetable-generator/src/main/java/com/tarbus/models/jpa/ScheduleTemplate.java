package com.tarbus.models.jpa;

import com.tarbus.models.TemplateType;
import com.tarbus.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "schedule_templates", schema = "schedule")
@Getter
@Setter
@NoArgsConstructor
public class ScheduleTemplate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private String id;

  @OneToOne()
  @JoinColumn(name = "uploader_id", referencedColumnName = "id")
  private User uploader;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private TemplateType type;

  public ScheduleTemplate(String id, User uploader, TemplateType type) {
    this.id = id;
    this.uploader = uploader;
    this.type = type;
  }

  public Map<String, Object> toJson() {
    Map<String, Object> result = new HashMap<>();
    result.put("id", id);
    result.put("type", type);
    return result;
  }
}
