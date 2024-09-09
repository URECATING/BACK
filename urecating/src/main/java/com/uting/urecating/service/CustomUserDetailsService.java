package com.uting.urecating.service;

import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 정보를 가져옵니다.
        SiteUser siteUser = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저정보가 없습니다."));

        // 권한 정보 설정 (여기서는 예시로 빈 권한을 설정합니다. 필요에 따라 수정하세요.)
        List<GrantedAuthority> grantedAuthorities = Collections.emptyList();

        return User.builder()
                .username(siteUser.getLogin())
                .password(siteUser.getPassword())
                .authorities(grantedAuthorities)
                .build();
    }
}




