package com.uting.urecating.repository;

import com.uting.urecating.domain.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    //Loginid로 찾기
    Optional<SiteUser> findByLogin(String login);
}
