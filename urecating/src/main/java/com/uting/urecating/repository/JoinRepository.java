package com.uting.urecating.repository;


import com.uting.urecating.domain.Join;
import com.uting.urecating.domain.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinRepository extends JpaRepository<Join, Long> {

    // 참여 목록 조회
    List<Join> findByUser(SiteUser user);
}
