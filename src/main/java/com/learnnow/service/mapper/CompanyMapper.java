package com.learnnow.service.mapper;

import com.learnnow.domain.*;
import com.learnnow.service.dto.CompanyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {


    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "removeCourse", ignore = true)
    @Mapping(target = "profiles", ignore = true)
    @Mapping(target = "removeProfile", ignore = true)
    Company toEntity(CompanyDTO companyDTO);

    default Company fromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
