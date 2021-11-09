package com.saemodong.api.model.activity.extra;

import com.saemodong.api.model.activity.Activity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ActivityExtraOrganizer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name="activityId")
  private Activity activity;

  @ManyToOne
  @JoinColumn(name="extraOrganizerId")
  private ExtraOrganizer extraOrganizer;
}
