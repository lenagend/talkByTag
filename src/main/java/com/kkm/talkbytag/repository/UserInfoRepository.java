package com.kkm.talkbytag.repository;

import com.kkm.talkbytag.domain.UserInfo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserInfoRepository extends ReactiveCrudRepository<UserInfo, Long> {
}
