package com.saemodong.api.controller.activity;

import com.saemodong.api.common.KeyValidator;
import com.saemodong.api.dto.ApiResponse;
import com.saemodong.api.dto.FailureResponse;
import com.saemodong.api.dto.ResultCode;
import com.saemodong.api.dto.SuccessResponse;
import com.saemodong.api.model.BookmarkType;
import com.saemodong.api.service.BookmarkService;
import com.saemodong.api.service.activity.ActivityPageResponse;
import com.saemodong.api.service.activity.ActivityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RestController
@RequestMapping("/api/v1/activity")
@Validated
public class ActivityController {

  private final ActivityService activityService;
  private final BookmarkService bookmarkService;
  private final KeyValidator keyValidator;

  @GetMapping("")
  public ResponseEntity<? extends ApiResponse> getActivity(
      @RequestParam String apiKey,
      @RequestParam Integer page,
      @ValuesAllowed(
              propName = "sorter",
              values = {"latestAsc", "ddayAsc"})
          @RequestParam(required = false, defaultValue = "latestAsc")
          String sorter,
      @ValuesAllowed(
              propName = "isToday",
              values = {"Y", "N"})
          @RequestParam(required = false, defaultValue = "N")
          String isToday) {

    if (!keyValidator.validate(apiKey)) {
      return new ResponseEntity(
          FailureResponse.of(ResultCode.NOT_FOUND, "사용자가 존재하지 않습니다."), HttpStatus.NOT_FOUND);
    }

    ActivityPageResponse activityPageResponse =
        activityService.getActivityList(page, sorter, isToday, apiKey);

    return ResponseEntity.ok(SuccessResponse.of(activityPageResponse));
  }

  @GetMapping("/extra")
  public ResponseEntity<? extends ApiResponse> getActivityExtra(
      @RequestParam String apiKey,
      @RequestParam Integer page,
      @ValuesAllowed(
              propName = "sorter",
              values = {"latestAsc", "ddayAsc"})
          @RequestParam(required = false, defaultValue = "latestAsc")
          String sorter,
      @RequestParam(required = false, defaultValue = "") String type,
      @RequestParam(required = false, defaultValue = "") String field,
      @RequestParam(required = false, defaultValue = "") String organizer,
      @RequestParam(required = false, defaultValue = "") String district) {

    if (!keyValidator.validate(apiKey)) {
      return new ResponseEntity(
          FailureResponse.of(ResultCode.NOT_FOUND, "사용자가 존재하지 않습니다."), HttpStatus.NOT_FOUND);
    }

    ActivityPageResponse activityPageResponse =
        activityService.getActivityExtraList(
            page, sorter, type, field, organizer, district, apiKey);

    return ResponseEntity.ok(SuccessResponse.of(activityPageResponse));
  }

  @GetMapping("/contest")
  public ResponseEntity<? extends ApiResponse> getActivityContest(
      @RequestParam String apiKey,
      @RequestParam Integer page,
      @ValuesAllowed(
              propName = "sorter",
              values = {"latestAsc", "ddayAsc"})
          @RequestParam(required = false, defaultValue = "latestAsc")
          String sorter,
      @RequestParam(required = false, defaultValue = "") String type,
      @RequestParam(required = false, defaultValue = "") String field,
      @RequestParam(required = false, defaultValue = "") String organizer,
      @RequestParam(required = false, defaultValue = "") String prize) {

    if (!keyValidator.validate(apiKey)) {
      return new ResponseEntity(
          FailureResponse.of(ResultCode.NOT_FOUND, "사용자가 존재하지 않습니다."), HttpStatus.NOT_FOUND);
    }

    ActivityPageResponse activityPageResponse =
        activityService.getActivityContestList(page, sorter, type, field, organizer, prize, apiKey);

    return ResponseEntity.ok(SuccessResponse.of(activityPageResponse));
  }

  @GetMapping("/{activityId}/mark")
  public ResponseEntity<? extends ApiResponse> markActivity(
      @PathVariable Long activityId, @RequestParam String apiKey) {

    if (!keyValidator.validate(apiKey)) {
      return new ResponseEntity(
          FailureResponse.of(ResultCode.NOT_FOUND, "사용자가 존재하지 않습니다."), HttpStatus.NOT_FOUND);
    }

    bookmarkService.mark(BookmarkType.ACTIVITY.getType(), activityId, apiKey);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{activityId}/unmark")
  public ResponseEntity<? extends ApiResponse> unmarkActivity(
      @PathVariable Long activityId, @RequestParam String apiKey) {

    if (!keyValidator.validate(apiKey)) {
      return new ResponseEntity(
          FailureResponse.of(ResultCode.NOT_FOUND, "사용자가 존재하지 않습니다."), HttpStatus.NOT_FOUND);
    }

    bookmarkService.unmark(BookmarkType.ACTIVITY.getType(), activityId, apiKey);
    return ResponseEntity.noContent().build();
  }
}
