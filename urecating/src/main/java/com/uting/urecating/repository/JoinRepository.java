package com.uting.urecating.repository;


import com.uting.urecating.domain.PostJoin;
import com.uting.urecating.domain.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinRepository extends JpaRepository<PostJoin, Long> {

}
