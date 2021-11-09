package com.saemodong.api.model.activity.contest;

import com.saemodong.api.model.activity.Activity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class ActivityContestField {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "activityId")
  private Activity activity;

  @ManyToOne
  @JoinColumn(name = "contestFieldId")
  private ContestField contestField;
}
