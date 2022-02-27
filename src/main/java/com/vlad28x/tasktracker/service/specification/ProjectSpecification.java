package com.vlad28x.tasktracker.service.specification;

import com.vlad28x.tasktracker.dto.FilterDto;
import com.vlad28x.tasktracker.entity.Project;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProjectSpecification {

    Specification<Project> getSpecificationFromFilters(List<FilterDto> filters);

}
