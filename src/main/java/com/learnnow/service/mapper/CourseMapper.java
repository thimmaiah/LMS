package com.learnnow.service.mapper;

import com.learnnow.domain.*;
import com.learnnow.service.dto.CourseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {


    @Mapping(target = "removeSme", ignore = true)

    default Course fromId(Long id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
