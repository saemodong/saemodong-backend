package com.saemodong.api.repository.activity;

import com.saemodong.api.model.activity.Activity;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
@Getter
public class ActivityCustomRepositoryImpl implements ActivityCustomRepository {

  @PersistenceContext private EntityManager em;

  @Override
  public List<Activity> findExtraBy(
      Integer page,
      String sorter,
      List<Long> typeId,
      List<Long> fieldId,
      List<Long> districtId,
      List<Long> organizerId) {

    String jpql = getExtraNativeQuery(sorter, typeId, fieldId, districtId, organizerId);
    Query query = em.createNativeQuery(jpql, Activity.class);

    return query.setFirstResult(0 + page * 10).setMaxResults(10).getResultList();
  }

  @Override
  public List<Activity> findContestBy(
      Integer page,
      String sorter,
      List<Long> typeId,
      List<Long> fieldId,
      List<Long> organizerId,
      List<Long> prizeId) {

    String jpql = getContestNativeQuery(sorter, typeId, fieldId, organizerId, prizeId);
    Query query = em.createNativeQuery(jpql, Activity.class);

    return query.setFirstResult(0 + page * 10).setMaxResults(10).getResultList();
  }

  public Integer getExtraTotalPage(
      String sorter,
      List<Long> typeId,
      List<Long> fieldId,
      List<Long> districtId,
      List<Long> organizerId) {

    String jpql = getExtraNativeQuery(sorter, typeId, fieldId, districtId, organizerId);
    Query query = em.createNativeQuery(jpql, Activity.class);
    query.setMaxResults(100);

    return (int) Math.ceil(query.getResultList().size() / 10d);
  }

  public Integer getContestTotalPage(
      String sorter,
      List<Long> typeId,
      List<Long> fieldId,
      List<Long> organizerId,
      List<Long> prizeId) {

    String jpql = getContestNativeQuery(sorter, typeId, fieldId, organizerId, prizeId);
    Query query = em.createNativeQuery(jpql, Activity.class);
    query.setMaxResults(100);

    return (int) Math.ceil(query.getResultList().size() / 10d);
  }

  private String getExtraNativeQuery(
      String sorter,
      List<Long> typeId,
      List<Long> fieldId,
      List<Long> districtId,
      List<Long> organizerId) {

    String sql1 =
        "select distinct activity.id, activity.name, activity.type, activity.url, activity.opened_at, activity.closed_at, activity.is_deleted, activity.deleted_at, activity.created_at, activity.updated_at from activity";
    String sql2 = "";

    if (!typeId.isEmpty()) {
      sql2 += " join activity_extra_type on activity_extra_type.activity_id = activity.id and";
      List<String> typeCondition =
          typeId.stream()
              .map(item -> " activity_extra_type.extra_type_id=" + item)
              .collect(Collectors.toList());
      String typeQuery = StringUtils.join(typeCondition, " or");
      sql2 = sql2 + " (" + typeQuery + " )";
    }
    if (!fieldId.isEmpty()) {
      sql2 += " join activity_extra_field on activity_extra_field.activity_id = activity.id and";
      List<String> fieldCondition =
          fieldId.stream()
              .map(item -> " activity_extra_field.extra_field_id=" + item)
              .collect(Collectors.toList());
      String fieldQuery = StringUtils.join(fieldCondition, " or");
      sql2 = sql2 + " (" + fieldQuery + " )";
    }
    if (!districtId.isEmpty()) {
      sql2 +=
          " join activity_extra_district on activity_extra_district.activity_id = activity.id and";
      List<String> districtCondition =
          districtId.stream()
              .map(item -> " activity_extra_district.extra_district_id=" + item)
              .collect(Collectors.toList());
      String districtQuery = StringUtils.join(districtCondition, " or");
      sql2 = sql2 + " (" + districtQuery + " )";
    }
    if (!organizerId.isEmpty()) {
      sql2 +=
          " join activity_extra_organizer on activity_extra_organizer.activity_id = activity.id and";
      List<String> organizerCondition =
          organizerId.stream()
              .map(item -> " activity_extra_organizer.extra_organizer_id=" + item)
              .collect(Collectors.toList());
      String organizerQuery = StringUtils.join(organizerCondition, " or");
      sql2 = sql2 + " (" + organizerQuery + " )";
    }

    String jpql = sql1 + sql2;

    if (sorter.equals("latestAsc")) {
      jpql +=
          " where activity.is_deleted='N' and activity.type=0 order by activity.created_at desc";
    } else {
      jpql +=
          " where date_format(activity.closed_at, '%Y-%m-%d') > curdate() and activity.is_deleted='N' and activity.type=0 order by activity.closed_at asc";
    }

    return jpql;
  }

  private String getContestNativeQuery(
      String sorter,
      List<Long> typeId,
      List<Long> fieldId,
      List<Long> organizerId,
      List<Long> prizeId) {

    String sql1 =
        "select distinct activity.id, activity.name, activity.type, activity.url, activity.opened_at, activity.closed_at, activity.is_deleted, activity.deleted_at, activity.created_at, activity.updated_at from activity";
    String sql2 = "";

    if (!typeId.isEmpty()) {
      sql2 += " join activity_contest_type on activity_contest_type.activity_id = activity.id and";
      List<String> typeCondition =
          typeId.stream()
              .map(item -> " activity_contest_type.contest_type_id=" + item)
              .collect(Collectors.toList());
      String typeQuery = StringUtils.join(typeCondition, " or");
      sql2 = sql2 + " (" + typeQuery + " )";
    }
    if (!fieldId.isEmpty()) {
      sql2 +=
          " join activity_contest_field on activity_contest_field.activity_id = activity.id and";
      List<String> fieldCondition =
          fieldId.stream()
              .map(item -> " activity_contest_field.contest_field_id=" + item)
              .collect(Collectors.toList());
      String fieldQuery = StringUtils.join(fieldCondition, " or");
      sql2 = sql2 + " (" + fieldQuery + " )";
    }
    if (!organizerId.isEmpty()) {
      sql2 +=
          " join activity_contest_organizer on activity_contest_organizer.activity_id = activity.id and";
      List<String> organizerCondition =
          organizerId.stream()
              .map(item -> " activity_contest_organizer.contest_organizer_id=" + item)
              .collect(Collectors.toList());
      String organizerQuery = StringUtils.join(organizerCondition, " or");
      sql2 = sql2 + " (" + organizerQuery + " )";
    }
    if (!prizeId.isEmpty()) {
      sql2 +=
          " join activity_contest_prize on activity_contest_prize.activity_id = activity.id and";
      List<String> prizeCondition =
          prizeId.stream()
              .map(item -> " activity_contest_prize.contest_prize_id=" + item)
              .collect(Collectors.toList());
      String prizeQuery = StringUtils.join(prizeCondition, " or");
      sql2 = sql2 + " (" + prizeQuery + " )";
    }

    String jpql = sql1 + sql2;

    if (sorter.equals("latestAsc")) {
      jpql +=
          " where activity.is_deleted='N' and activity.type=1 order by activity.created_at desc";
    } else {
      jpql +=
          " where date_format(activity.closed_at, '%Y-%m-%d') > curdate() and activity.is_deleted='N' and activity.type=1 order by activity.closed_at asc";
    }

    return jpql;
  }
}
