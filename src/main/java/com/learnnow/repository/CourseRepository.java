package com.learnnow.repository;
import com.learnnow.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Course entity.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    @Query(value = "select distinct course from Course course left join fetch course.smes",
        countQuery = "select count(distinct course) from Course course")
    Page<Course> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct course from Course course left join fetch course.smes")
    List<Course> findAllWithEagerRelationships();

    @Query("select course from Course course left join fetch course.smes where course.id =:id")
    Optional<Course> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(value="select distinct course from Course course where course.company.id =:companyId",
    countQuery = "select count(distinct course) from Course course where course.company.id =:companyId")
    Page<Course> findAllWithCompanyId(@Param("companyId") Long companyId, Pageable pageable);


}
