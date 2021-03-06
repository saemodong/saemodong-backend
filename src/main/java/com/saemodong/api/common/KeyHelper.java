package com.saemodong.api.common;

import com.saemodong.api.exception.UserNotFoundException;
import com.saemodong.api.model.user.User;
import com.saemodong.api.repository.user.UserRepository;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class KeyHelper {

  private final UserRepository userRepository;
  private final Integer KEY_LENGTH = 15;

  public boolean validate(String apiKey) {
    Optional<User> user = userRepository.findByApiKey(apiKey);
    return user.isPresent();
  }

  public String getApiKey() {
    String apiKey = generateApiKey(KEY_LENGTH);
    while (checkIfExist(apiKey)) {
      apiKey = generateApiKey(KEY_LENGTH);
    }
    return apiKey;
  }

  private boolean checkIfExist(String apiKey) {
    return userRepository.existsByApiKey(apiKey);
  }

  private String generateApiKey(Integer length) {
    char[] tmp = new char[length];
    for (int i = 0; i < tmp.length; i++) {
      int div = (int) Math.floor(Math.random() * 2);
      if (div == 0) {
        tmp[i] = (char) (Math.random() * 10 + '0');
      } else {
        tmp[i] = (char) (Math.random() * 26 + 'A');
      }
    }
    return new String(tmp);
  }

  public Integer getKeyLength() {
    return KEY_LENGTH;
  }

  public User getUser(String apiKey) {
    User user =
        userRepository.findByApiKey(apiKey).orElseThrow(() -> new UserNotFoundException(apiKey));
    return user;
  }
}
