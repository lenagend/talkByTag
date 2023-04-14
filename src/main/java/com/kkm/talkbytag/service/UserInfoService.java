package com.kkm.talkbytag.service;

import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.domain.UserInfo;
import com.kkm.talkbytag.repository.UserInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;

    public UserInfoService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public Mono<UserInfo> saveUserInfo(UserInfo userInfo){return this.userInfoRepository.save(userInfo);}

    public Mono<UserInfo> findByUsername(String username){return this.userInfoRepository.findByUsername(username);}

    public Mono<UserInfo> updateUserInfo(String username, UserInfo updatedUserInfo) {
        return userInfoRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.")))
                .flatMap(existingUserInfo -> {
                    if (!existingUserInfo.getNickname().equals(updatedUserInfo.getNickname())) {
                        return userInfoRepository.existsByNickname(updatedUserInfo.getNickname())
                                .flatMap(isNicknameExist -> {
                                    if (isNicknameExist) {
                                        return Mono.error(new IllegalArgumentException("중복된 닉네임이 존재합니다."));
                                    }
                                    existingUserInfo.setNickname(updatedUserInfo.getNickname());
                                    existingUserInfo.setProfileImage(updatedUserInfo.getProfileImage());
                                    // 필요한 경우 다른 필드도 업데이트하세요
                                    return userInfoRepository.save(existingUserInfo);
                                });
                    } else {
                        existingUserInfo.setProfileImage(updatedUserInfo.getProfileImage());
                        // 필요한 경우 다른 필드도 업데이트하세요
                        return userInfoRepository.save(existingUserInfo);
                    }
                });
    }


}
