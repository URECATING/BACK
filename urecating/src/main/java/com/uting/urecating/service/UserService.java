package com.uting.urecating.service;

import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 회원가입
    public SiteUser join(String userName, String login, String password,
                         String team, String gender, String phone) {
        SiteUser user = new SiteUser();
        user.setUserName(userName);
        user.setLogin(login);
        user.setPassword(password);
        user.setTeam(team);
        user.setGender(gender);
        user.setPhone(phone);

        return userRepository.save(user);
    }
}
