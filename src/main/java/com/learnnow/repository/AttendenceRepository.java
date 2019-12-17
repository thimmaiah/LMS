package com.learnnow.repository;
import com.learnnow.domain.Attendence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Attendence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttendenceRepository extends JpaRepository<Attendence, Long>, JpaSpecificationExecutor<Attendence> {

    @Query("select attendence from Attendence attendence where attendence.user.login = ?#{principal.username}")
    Page<Attendence> findByUserIsCurrentUser(Specification<Attendence> spec, Pageable pageable);

}
