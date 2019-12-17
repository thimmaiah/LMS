package com.learnnow.service.mapper;

import com.learnnow.domain.*;
import com.learnnow.service.dto.AttendenceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Attendence} and its DTO {@link AttendenceDTO}.
 */
@Mapper(componentModel = "spring", uses = {CourseMapper.class, UserMapper.class})
public interface AttendenceMapper extends EntityMapper<AttendenceDTO, Attendence> {

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.name", target = "courseName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    AttendenceDTO toDto(Attendence attendence);

    @Mapping(source = "courseId", target = "course")
    @Mapping(source = "userId", target = "user")
    Attendence toEntity(AttendenceDTO attendenceDTO);

    default Attendence fromId(Long id) {
        if (id == null) {
            return null;
        }
        Attendence attendence = new Attendence();
        attendence.setId(id);
        return attendence;
    }
}
