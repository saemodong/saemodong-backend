package com.saemodong.api.repository.activity;

import com.saemodong.api.model.activity.Activity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ActivityCustomRepository {

  Page<Activity> getActivityBy(
      Integer pageNumber, String sorter, String registeredToday);

  List<Activity> findExtraBy(
      Integer page,
      String sorter,
      List<Long> typeId,
      List<Long> fieldId,
      List<Long> districtId,
      List<Long> organizerId);

  List<Activity> findExtraBy(
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      List<Long> typeId,
      List<Long> fieldId,
      List<Long> districtId,
      List<Long> organizerId);

  List<Activity> findContestBy(
      Integer page,
      String sorter,
      List<Long> typeId,
      List<Long> fieldId,
      List<Long> organizerId,
      List<Long> prizeId);

  List<Activity> findContestBy(
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      List<Long> typeId,
      List<Long> fieldId,
      List<Long> organizerId,
      List<Long> prizeId);
}
