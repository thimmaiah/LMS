package com.learnnow.service.mapper;

import com.learnnow.domain.*;
import com.learnnow.service.dto.CourseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class, CompanyMapper.class})
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    CourseDTO toDto(Course course);

    @Mapping(target = "removeSme", ignore = true)
    @Mapping(source = "companyId", target = "company")
    Course toEntity(CourseDTO courseDTO);

    default Course fromId(Long id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
