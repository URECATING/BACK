package com.uting.urecating.service;

import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.UserUpdateDto;
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
        SiteUser user = SiteUser.builder()
                .userName(userName)
                .login(login)
                .password(password)
                .team(team)
                .gender(gender)
                .phone(phone)
                .build();

        return userRepository.save(user);
    }

    // 로그인
    public SiteUser login(String login, String password){
        SiteUser user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));

        if(!user.getPassword().equals(password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    //마이페이지(사용자 정보 조회)
    public SiteUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    }

    //마이페이지 수정 - 이미지, 비밀번호, 전화번호
    public SiteUser updateUser(Long id, UserUpdateDto updateDto) {
        SiteUser user = getUserById(id);
        SiteUser updatedUser = SiteUser.builder()
                .id(user.getId())
                .login(user.getLogin())
                .userName(user.getUserName())
                .team(user.getTeam())
                .gender(user.getGender())
                .phone(updateDto.getPhone() != null ? updateDto.getPhone() : user.getPhone())
                .password(updateDto.getPassword() != null ? updateDto.getPassword() : user.getPassword())
                .image(updateDto.getImage() != null ? updateDto.getImage() : user.getImage())
                .build();
        return userRepository.save(updatedUser);
    }
}
