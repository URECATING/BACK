package com.uting.urecating.service;

import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.UserUpdateDto;
import com.uting.urecating.repository.UserRepository;
import com.uting.urecating.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    // 회원가입
    public SiteUser join(String userName, String login, String password,
                         String team, String gender, String phone) {
        if(userRepository.existsByLogin(login)){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        SiteUser user = SiteUser.builder()
                .userName(userName)
                .login(login)
                .password(passwordEncoder.encode(password))
                .team(team)
                .gender(gender)
                .phone(phone)
                .build();

        return userRepository.save(user);
    }

    // 로그인
    public SiteUser login(String login, String password) {
        SiteUser user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
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
    public SiteUser updateUser(String login, String password, String phone, String imageUrl) {
        SiteUser user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        //이미지 수정 시 기존 이미지 S3에서 삭제
        if(imageUrl != null && user.getImage()!=null){
            s3Service.delete(user.getImage());
        }

        String updatedPassword = password != null ? passwordEncoder.encode(password) : user.getPassword();

        SiteUser updatedUser = SiteUser.builder()
                .id(user.getId())
                .login(user.getLogin())
                .userName(user.getUserName())
                .team(user.getTeam())
                .gender(user.getGender())
                .phone(phone != null ? phone : user.getPhone())
                .password(updatedPassword)
                .image(imageUrl != null ? imageUrl : user.getImage())
                .build();

        return userRepository.save(updatedUser);
    }
}
